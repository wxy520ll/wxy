package cn.net.xinyi.xmjt.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.trace.OnGeoFenceListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.comm.core.utils.SharePrefUtils;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.AppStart;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.HisTraceQueryModel;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.PlcBxWorkLog;
import cn.net.xinyi.xmjt.model.UserPlcBxInfo;
import cn.net.xinyi.xmjt.model.XinyiLatLng;
import cn.net.xinyi.xmjt.model.XlJpNewModel;
import cn.net.xinyi.xmjt.rxbus.RxBus;
import cn.net.xinyi.xmjt.rxbus.event.QueryHisTraceEvent;
import cn.net.xinyi.xmjt.utils.BaiduMapUtil;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.GsonUtils;
import cn.net.xinyi.xmjt.utils.NotificationHelper;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.utils.XinyiLog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static cn.net.xinyi.xmjt.utils.BaiduTraceFacade.userInfo;

/**
 * Created by hao.zhou on 2016/5/5.
 */
public class BaiduTraceService extends Service {

    public static String TAG = "BaiduTraceService";
    public static final int ID_OF_TRCE_NOTIFICATION = 10001;
    public static final String FENCE_ACTION = "cn.net.xinyi.xmjt.service.BaiduTraceService.Fence";

    /**
     * 1-19地理围栏
     * 20-29巡逻
     * 30-39 岗亭
     */
    public static final int FENCE_CREATE = 1; //地理围栏创建
    public static final int FENCE_CLEAR = 2; //地理围栏创建
    public static final int FENCE_ALARM = 3; //地理围栏监听
    public static final int FENCE_LIST = 4; //地理围栏获取
    public static final int FENCE_DELETE = 5; //删除地理围栏
    public static final int FENCE_REALTIME_LOC = 6; //获取到实时位置

    public static final int XLID_SET = 20; //设置巡逻记录id

    public static final int GT_START = 30; //岗亭值守开始

    private static final int CHECK_IS_NEED_BAIDU_TRACE = 11; //检查是否需要鹰眼服务
    private static final int STOP_ALL = 200;

    public String fenceName;
    LocationClient mLocationClient;

    //岗亭值守
    public static final int TYPE_GT = 1;
    private XinyiLatLng mPlcBxLocation;
    private int GTID;
    private int GTZSID;

    //巡逻记录ID
    String xljlid;
    FenceReceiver mFenceReceiver = null;
    PowerReceiver mPowerReceiver = null;
    public static final int TYPE_XL = 2;

    private int option_type;//当前类型（巡逻or岗亭）
    private Subscription subscription;


    @Override
    public void onCreate() {
        super.onCreate();

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new BaseLocationListener());
        initLocationOptions(); //初始化定位参数

        if (mFenceReceiver == null)
            mFenceReceiver = new FenceReceiver();

        if (mPowerReceiver == null)
            mPowerReceiver = new PowerReceiver();

        IntentFilter fenceFilter = new IntentFilter(FENCE_ACTION);
        registerReceiver(mFenceReceiver, fenceFilter);

        IntentFilter powerFilter = new IntentFilter(); //电量锁监听
        powerFilter.addAction(Intent.ACTION_SCREEN_OFF);
        powerFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mPowerReceiver, powerFilter);


        registerRxbus();



        // getLastXlGtStatus(true); //第一次获取全部状态
        //Toast.makeText(BaiduTraceService.this, "BaiduTraceService create", //Toast.LENGTH_SHORT).show();
    }
    private SharedPreferences config_position_size;
    String key = DateUtil.date2String(new Date(), "yyyy-MM-dd");
    private void registerRxbus() {
        if (config_position_size == null) {
            config_position_size = SharePrefUtils.getSharePrefEdit(AppContext.instance, "config_position_size");
        }

        subscription = RxBus.getDefault().toObserverable(QueryHisTraceEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QueryHisTraceEvent>() {
                    @Override
                    public void call(QueryHisTraceEvent hisTraceQueryModel) {
                        XinyiLog.e("rxbus", "service接受到了消息===" + Thread.currentThread().getName());

                        if (hisTraceQueryModel.getType() == QueryHisTraceEvent.EVENT_TRACEING) {
                            final List<HisTraceQueryModel.PointsBean> pointList = hisTraceQueryModel.getHisTraceQueryModel().getListPoints();
                            //如果有新的点位就上传
                            Integer oldSize = config_position_size.getInt(key, 0);

                            if (oldSize < pointList.size()) {

                                List<XlJpNewModel> xlJpNewModels = new ArrayList<>();
                                //坐标按照采集时间倒叙排列
                                String json = null;
                                for (int i = 0, len = pointList.size() - oldSize; i < len; i++) {
                                    HisTraceQueryModel.PointsBean pointsBean = pointList.get(i);
                                    XlJpNewModel xlJpNewModel = new XlJpNewModel();
                                    xlJpNewModel.setJD(String.valueOf(pointsBean.getLocation().get(0)));
                                    xlJpNewModel.setWD(String.valueOf(pointsBean.getLocation().get(1)));
                                    xlJpNewModel.setCJYH(userInfo.getUsername());
                                    xlJpNewModel.setCJSJ(DateUtil.unixTimestampToDate(pointsBean.getLoc_time()));
                                    xlJpNewModels.add(0, xlJpNewModel);
                                }
                                json = GsonUtils.obj2Json(xlJpNewModels);
                                XinyiLog.e("BaiduTraceFacade", json);
                                //發送請求
                                for (XlJpNewModel x : xlJpNewModels) {
                                    ApiResource.postXljpNew(GsonUtils.obj2Json(x), new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                            Log.d("BaiduTraceFacade", "onSuccess");
                                            SharedPreferences.Editor edit = config_position_size.edit();
                                            edit.clear().commit();
                                            edit.putInt(key, pointList.size());
                                            edit.commit();
                                        }

                                        @Override
                                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                            Log.d("BaiduTraceFacade", "onFailure");
                                        }
                                    });
                                }

                            }
                        } else if (hisTraceQueryModel.getType() == QueryHisTraceEvent.EVENT_TRACE_STOP) {
                            ////轨迹停止
                            SharedPreferences.Editor edit = config_position_size.edit();
                            edit.clear().commit();
                        }

                    }
                });
    }

    /**
     * 初始化定位服务
     * 使用是国家坐标系
     */
    protected void initLocationOptions() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true); //需要地址
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //高精度定位
        //option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors); //高精度定位
        // option.setIsNeedLocationDescribe(true);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        //bd09ll  表示百度经纬度坐标，
        //gcj02   表示经过国测局加密的坐标，
        //wgs84   表示gps获取的坐标。
        option.setCoorType("bd09ll");
        option.setScanSpan(0);  //定位一次
        mLocationClient.setLocOption(option);
    }


    /**
     * 关闭所有鹰眼服务
     *
     * @param applicationContext
     */
    public static void stopAll(Context applicationContext) {
        BaiduTraceFacade.stop();
        Intent intent = new Intent(FENCE_ACTION);
        intent.putExtra("type", STOP_ALL);
        applicationContext.sendBroadcast(intent);


    }

    /**
     * 百度定位回调
     */
    public class BaseLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //检测是否连接网络
            onReceiveLoc(location, true, null);
        }
    }

    protected void onReceiveLoc(BDLocation location, boolean success, Throwable errmsg) {
        if (mLocationClient != null)
            mLocationClient.stop();
        if (AppContext.instance().getLoginInfo().getUsername() != null) {
//            TraceLocation location = JSON.parseObject(lo, TraceLocation.class);
            Intent intent = new Intent(BaiduTraceService.FENCE_ACTION);
            intent.putExtra("data", JSON.toJSONString(BaiduMapUtil.bdLocation2LocationInfo(location)));
            intent.putExtra("type", BaiduTraceService.FENCE_REALTIME_LOC);
            AppContext.instance().sendBroadcast(intent);

        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this);
        Intent i = new Intent(this, AppStart.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        builder.setContentText("勤务中").setSmallIcon(R.drawable.app_icon).setContentTitle(getText(R.string.app_name));
        builder.setContentIntent(pi);
        Notification note = builder.getNotification();
        note.flags |= Notification.FLAG_NO_CLEAR;

        startForeground(ID_OF_TRCE_NOTIFICATION, note);
        flags = START_STICKY;

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * /**
     * 开启轨迹服务
     */
    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), getApplicationContext());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        stopForeground(true);
        try {
            if (mFenceReceiver != null)
                unregisterReceiver(mFenceReceiver);
            if (mPowerReceiver != null)
                unregisterReceiver(mPowerReceiver);
        } catch (Exception e) {

        }

        if(subscription!=null&&!subscription.isUnsubscribed())
        {
            subscription.unsubscribe();;
        }

        super.onDestroy();
    }


    /**
     * 地理围栏监听
     */
    public class FenceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("data");
            XinyiLog.e("TEST", "BaiduTraceServiceBaiduTraceService,onReceive" + msg);
            int type = intent.getIntExtra("type", 0);
            switch (type) {
                case FENCE_ALARM:
                    if (!TextUtils.isEmpty(msg)) {
                        try {
                            JSONObject js = JSON.parseObject(msg);
                            //   UI.toast(getApplicationContext(),"SERVICE"+(js.getIntValue("action") == 1 ? "进入" : "离开"));
                            if (1 == js.getIntValue("action") && isXLFence(fenceName)) { //进入围栏内
                                fenceName = js.getString("fence");
                                option_type = TYPE_XL;
                                if (mLocationClient != null) {
                                    if (mLocationClient.isStarted())
                                        mLocationClient.stop();
                                    mLocationClient.start();
                                }
                                NotificationHelper.createXLDK(BaiduTraceService.this, getXLDID(fenceName));
                            } else if (2 == js.getIntValue("action") && isGTFence(fenceName)) { //离岗
                                //  UI.toast(getApplicationContext(),"isGTFence(fenceName)"+isGTFence(fenceName));
                                option_type = TYPE_GT;
                            }
                        } catch (JSONException e) {

                        }
                    }
                    break;
                case FENCE_CREATE:
                    //Toast.makeText(getApplicationContext(), "create fence", //Toast.LENGTH_SHORT).show();
                    break;
                case FENCE_CLEAR:
                    //Toast.makeText(getApplicationContext(), "clear fence", //Toast.LENGTH_SHORT).show();
                    break;
                case FENCE_LIST:
                    //Toast.makeText(getApplicationContext(), "get fence" + msg, //Toast.LENGTH_SHORT).show();
                    break;
                case FENCE_REALTIME_LOC:
                    UI.toast(getApplicationContext(), "获取到实时位置" + msg);
                    LocaionInfo location = JSON.parseObject(msg, LocaionInfo.class);
                    if (option_type == TYPE_XL) {
                        if (TextUtils.isEmpty(fenceName) || TextUtils.isEmpty(xljlid)) //如果fenceNameH或者巡逻id为空，返回不处理
                            return;
                    } else if (option_type == TYPE_GT) {
                        //  UI.toast(getApplicationContext(),"doPlcBxLeaving");
                        doPlcBxLeaving(4, 1, 1, location.getLat(), location.getLongt(), null);
                    }
                    break;
                case XLID_SET:
                    xljlid = msg;
                    // Toast.makeText(getApplicationContext(), "XLID SET" + msg,Toast.LENGTH_SHORT).show();
                    if (TextUtils.isEmpty(msg) && AppContext.instance().getLoginInfo() != null) {
                        BaiduTraceFacade.clearXLFence(AppContext.instance().getLoginInfo().getUsername());
                        BaiduTraceFacade.stop();//停止鹰眼轨迹
                    }
                    break;
                case GT_START:
                    if (intent.getSerializableExtra("latLngt") != null) {
                        mPlcBxLocation = (XinyiLatLng) intent.getSerializableExtra("latLngt");
                        GTID = intent.getIntExtra("GTID", 0);
                        GTZSID = intent.getIntExtra("GTZSID", 0);
                    } else {
                        mPlcBxLocation = null;
                        GTID = 0;
                        GTZSID = 0;
                        BaiduTraceFacade.clearGTFence(AppContext.instance().getLoginInfo().getUsername());
                    }
                    break;

                case CHECK_IS_NEED_BAIDU_TRACE:
                    //  getLastXlGtStatus();
                    break;

                case STOP_ALL:
                    xljlid = msg;
                    if (TextUtils.isEmpty(msg) && AppContext.instance().getLoginInfo() != null) {
                        BaiduTraceFacade.clearXLFence(AppContext.instance().getLoginInfo().getUsername());
                    }
                    mPlcBxLocation = null;
                    GTID = 0;
                    GTZSID = 0;
                    BaiduTraceFacade.clearGTFence(AppContext.instance().getLoginInfo().getUsername());
                    break;
            }
        }
    }

    /**
     * 开始巡逻
     * 后台开启自动打卡功能
     *
     * @param xljlid
     */
    public static void startXl(Context context, String xljlid) {
        BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), context);
        Intent itStart = new Intent(context, BaiduTraceService.class);
        context.startService(itStart);
        Intent intent = new Intent(FENCE_ACTION);
        intent.putExtra("type", XLID_SET);
        intent.putExtra("data", xljlid);
        context.sendBroadcast(intent);
    }


    /**
     * 结束巡逻
     */
    public static void stopXl(Context context) {
        Intent intent = new Intent(FENCE_ACTION);
        intent.putExtra("type", XLID_SET);
        intent.putExtra("data", "");
        context.sendBroadcast(intent);
    }

    /**
     * 岗亭值守--离岗
     *
     * @param lx
     * @param GTID
     * @param GTZSID
     * @param LAT
     * @param LNGT
     * @param ms
     */
    private void doPlcBxLeaving(final int lx, int GTID, int GTZSID, double LAT, double LNGT, String ms) {
        ApiResource.postUpdatePlcBxUserStatus(getWorkStatusJson(lx, GTID, GTZSID, LAT, LNGT, ms), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if ("true".equals(result)) {
                    UI.toast(getApplicationContext(), "doPlcBxLeaving  is true");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    /**
     * 岗亭--更新用户状态json
     *
     * @return
     */
    private String getWorkStatusJson(int LX, int GTID, int GTZSID, double LAT, double LNGT, String MS) {
        JSONObject requestJson = new JSONObject();
        requestJson.put("LX", LX + "");
        requestJson.put("GTID", GTID + "");
        requestJson.put("DW", AppContext.instance().getLoginInfo().getPcs());
        requestJson.put("GTZSID", GTZSID + "");
        requestJson.put("YH", AppContext.instance().getLoginInfo().getUsername());
        requestJson.put("LAT", LAT + "");
        requestJson.put("LNGT", LNGT + "");
        requestJson.put("LOCTYPE", "161");
        if (!TextUtils.isEmpty(MS)) {
            requestJson.put("MS", MS);
        }
        return requestJson.toJSONString();
    }


    /**
     * @param context
     * @param gtLoc   岗亭经纬度
     * @param gtid    岗亭id
     * @param gtzsid  岗亭值守id
     */
    public static void startPlcBxCheck(Context context, XinyiLatLng gtLoc, int gtid, int gtzsid) {
        BaiduTraceFacade.start(AppContext.instance().getLoginInfo().getUsername(), context);
        Intent itStart = new Intent(context, BaiduTraceService.class);
        context.startService(itStart);
        Intent intent = new Intent(FENCE_ACTION);
        intent.putExtra("GTID", gtid);
        intent.putExtra("latLngt", gtLoc);
        intent.putExtra("GTZSID", gtzsid);
        intent.putExtra("type", GT_START);
        context.sendBroadcast(intent);
    }


    /**
     * 结束岗亭值守
     *
     * @param context
     */
    public static void stopPlcBxCheck(Context context) {
        Serializable loc = null;
        Intent intent = new Intent(FENCE_ACTION);
        intent.putExtra("GTID", 0);
        intent.putExtra("latLngt", loc);
        intent.putExtra("GTZSID", 0);
        intent.putExtra("type", GT_START);
        context.sendBroadcast(intent);

    }

    /**
     * 是否岗亭围栏
     *
     * @param fenceName
     * @return
     */
    public static boolean isGTFence(String fenceName) {
        if (TextUtils.isEmpty(fenceName))
            return false;
        return fenceName.startsWith("g");
    }

    /**
     * 是否巡逻围栏
     *
     * @param fenceName
     * @return
     */
    public static boolean isXLFence(String fenceName) {
        if (TextUtils.isEmpty(fenceName))
            return false;
        return fenceName.startsWith("x");
    }


    /**
     * 检查是否需要开启鹰眼轨迹
     *
     * @return
     */
    public static void checkAll(Context context) {
        Intent intent = new Intent(FENCE_ACTION);
        intent.putExtra("type", CHECK_IS_NEED_BAIDU_TRACE);
        context.sendBroadcast(intent);
    }

    /**
     * 获取巡逻点id
     *
     * @param fenceName
     * @return
     */
    public static String getXLDID(String fenceName) {
        if (TextUtils.isEmpty(fenceName))
            return null;
        return fenceName.substring(1);

    }

    /**
     * 创建岗亭地理围栏
     * 只作用于创建那天
     */
    public static void createGTSimpleFence(String username, String fenceName, double lngt, double lat, int radius) {
        BaiduTraceFacade.createGTSimpleFence(username, fenceName, lngt, lat, radius);
    }

    /**
     * 创建巡逻地理围栏
     * 只作用于创建那天
     */
    public static void createXLSimpleFence(String username, String fenceName, double lngt, double lat, int radius) {
        BaiduTraceFacade.createXLSimpleFence(username, fenceName, lngt, lat, radius);
    }


    /**
     * 创建地理围栏
     * 只作用于创建那天
     * 50范围米
     */
    public static void createGTSimpleFence(String username, String fenceName, double lngt, double lat) {
        BaiduTraceFacade.createGTSimpleFence(username, fenceName, lngt, lat);
    }

    /**
     * 创建地理围栏
     * 只作用于创建那天
     * 50范围米
     */
    public static void createXLSimpleFence(String username, String fenceName, double lngt, double lat) {
        BaiduTraceFacade.createXLSimpleFence(username, fenceName, lngt, lat);
    }


    public static void delFence(int fenceId, OnGeoFenceListener listener) {
        BaiduTraceFacade.delFence(fenceId, listener);
    }

    public static void delFence(int fenceId) {
        BaiduTraceFacade.delFence(fenceId);
    }

    public static void getFenceList(String username, OnGeoFenceListener listener) {
        BaiduTraceFacade.getFenceList(username, listener);
    }

    public static void getFenceList(String username) {
        BaiduTraceFacade.getFenceList(username);

    }

    public static void getFenceList(String username, BaiduTraceFacade.OnXinyiGeoFenceListener listener) {
        BaiduTraceFacade.getFenceList(username, listener);
    }

    /**
     * 删除所有围栏
     *
     * @param username
     */
    public static void clearFence(String username) {
        BaiduTraceFacade.clearFence(username);

    }

    /**
     * 删除岗亭围栏
     *
     * @param username
     */
    public static void clearGTFence(String username) {
        BaiduTraceFacade.clearGTFence(username);
    }


    /**
     * 删除巡逻围栏
     *
     * @param username
     */
    public static void clearXLFence(String username) {
        BaiduTraceFacade.clearXLFence(username);
    }


    /**
     * 获取实时位置信息
     */
    public static void getRealLoc() {
        BaiduTraceFacade.getRealLoc();
    }


    /**
     * 获取用户当前值守状态的请求json
     *
     * @return
     */
    private String getUserInfoJson() {
        if (AppContext.instance().getLoginInfo() == null)
            return "";
        JSONObject requestJson = new JSONObject();
        requestJson.put("YH", AppContext.instance().getLoginInfo().getUsername());
        return requestJson.toJSONString();
    }


    /**
     * 获取用户最新的岗亭状态
     */
    private void getUserPlcBxStatus() {
        ApiResource.getUserGtZt(getUserInfoJson(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    List<UserPlcBxInfo> userPlcBxInfos = JSON.parseArray(result, UserPlcBxInfo.class);
                    if (userPlcBxInfos.size() > 0) {
                        UserPlcBxInfo plcBXInfo = userPlcBxInfos.get(0);
                        plcBXInfo.setLists(JSON.parseArray(plcBXInfo.getDATALIST(), PlcBxWorkLog.class));
                        if (plcBXInfo.getZSZT() == 1 || plcBXInfo.getZSZT() == 5) { //值守中
                            XinyiLatLng ll = new XinyiLatLng(plcBXInfo.getLists().get(0).getLAT(), plcBXInfo.getLists().get(0).getLNGT());
                            startPlcBxCheck(getApplicationContext(), ll, plcBXInfo.getGTID(), plcBXInfo.getID());
                        }
                    } else {

                    }

                } catch (JSONException e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }


    class PowerReceiver extends BroadcastReceiver {
        PowerManager pm = null;
        PowerManager.WakeLock wakeLock = null;

        @SuppressLint("Wakelock")
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                if (AppContext.instance().getLoginInfo() != null) {
                    if (!TextUtils.isEmpty(xljlid) || (mPlcBxLocation != null && GTID > 0 && GTZSID > 0)) {
                        if (pm == null) {
                            pm = (PowerManager) getSystemService(POWER_SERVICE);
                            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BaiduTrace");
                        }

                        XinyiLog.d("screen off,acquire wake lock!");
                        try {
                            if (wakeLock != null) {
                                wakeLock.acquire();
                            }
                        } catch (RuntimeException e) {

                        }
                    }
                }


            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                XinyiLog.d("screen on,release wake lock!");
                try {
                    if (wakeLock != null && wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                } catch (RuntimeException e) {

                }

            }
        }

    }

}
