package cn.net.xinyi.xmjt.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.utils.DistanceUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Main.Fragment.MainMenuFragment;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.PlcBxAty;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.model.XinyiLatLng;
import cn.net.xinyi.xmjt.utils.BaiduMapUtil;

/**
 * Created by studyjun on 2016/3/29.
 */
public class LocationService extends Service{

    public static final int ID_OF_NOTIFICATION=10000;

    LocationClient mLocationClient;
    GeoCoder mGeoCoder;
    BDLocation mLocation;
    public static final int LOC_SPAN=1000*30; //3分钟定位异常
    private int networkType;
    private Message msg;

    private XinyiLatLng mPlcBxLocation;
    private int GTID;
    private int GTZSID;
    private long mPlcBxOverTime=0; //0为没有超时设定
    PlcBxReceiver mPlcBxReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new BaseLocationListener());
        initLocationOptions(); //初始化定位参数

        mPlcBxReceiver = new PlcBxReceiver();
        IntentFilter filter = new IntentFilter(PlcBxAty.PLC_BC_ACTION);
        registerReceiver(mPlcBxReceiver,filter);

    }


    /**
     * 初始化定位服务
     * 使用是国家坐标系
     */
    protected void initLocationOptions() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true); //需要地址
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //高精度定位
        // option.setIsNeedLocationDescribe(true);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        //bd09ll  表示百度经纬度坐标，
        //gcj02   表示经过国测局加密的坐标，
        //wgs84   表示gps获取的坐标。
        option.setCoorType("bd09ll");
        option.setScanSpan(LOC_SPAN);  //1分钟定位一次
        mLocationClient.setLocOption(option);
    }


    /**
     * 百度定位回调
     */
    public class BaseLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //检测是否连接网络
            networkType = ((AppContext) getApplication()).getNetworkType();
            if (networkType != 0) {
                onReceiveLoc(location, true, null);
            }

        }
    }

    protected void onReceiveLoc(BDLocation locaionInfo, boolean success, Throwable errmsg) {
        //  UserManager uM=UserManager.get();
        if (AppContext.instance().getLoginInfo().getUsername()!=null){
            //   Toast.makeText(getApplicationContext(), "定位成功",Toast.LENGTH_LONG).show();
//            postLocation(locaionInfo.getLatitude(), locaionInfo.getLongitude(), locaionInfo.getAddrStr()); //上传位置信息
//            updateLBSPoi(locaionInfo);


            if (mPlcBxLocation!=null){
                if (DistanceUtil.getDistance(new LatLng(locaionInfo.getLatitude(),locaionInfo.getLongitude()), BaiduMapUtil.xinLatlngt2LatLngt(mPlcBxLocation)) >= 500) {
                    Toast.makeText(getApplicationContext(),"您当前已超出岗亭范围",Toast.LENGTH_SHORT).show();
                    if (mPlcBxOverTime==0){
                        mPlcBxOverTime= new Date().getTime()+30*60*1000; //30分钟超时
                    }
                    if (mPlcBxOverTime>0&&mPlcBxOverTime<new Date().getTime()){
                        doPlcBxLeaving(4,1,1,locaionInfo.getLatitude(),locaionInfo.getLongitude(),null);
                    }
                } else {
                    mPlcBxOverTime=0;
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "请登录",Toast.LENGTH_LONG).show();
        }
    }

    private void postLocation(double lat, double longt, String addresse) {
        startUploadThread(lat,longt,addresse);

    }


    private void startUploadThread(final double lat, final double longt, final String addresse) {
        // 保存与上传
        new Thread() {
            public void run() {
                // 将数据上传到服务器
                uploadInfo(lat,longt);
            }
        }.start();
    }
    //同步上传采集数据到服务端
    public void uploadInfo(double lat, double longt) {
        //json处理
        JSONObject jo = new JSONObject();
        jo.put("CJYH", AppContext.instance.getLoginInfo().getUsername());
        jo.put("WD",lat);
        jo.put("JD",longt);
        String json = jo.toJSONString();
        ApiResource.addGJInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty() && result.startsWith("true")) {
                    msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } else {
                    onFailure(i, headers, bytes, null);
                    msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    String result = new String(bytes);
                }
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:// 上传之后
                    Log.i("mjtgj","上传成功");
                    break;

                case 2:// 上传失败
                    Log.i("mjtgj", "上传失败");
                    break;
            }
        }
    };


    /**
     * 获取定位详情
     * @return
     */
    public BDLocation getLocation() {
        return mLocation;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mLocationClient!=null&&!mLocationClient.isStarted()){
            mLocationClient.start();
        }
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLocationClient!=null&&!mLocationClient.isStarted()){
            mLocationClient.start();
        }
        Notification.Builder builder = new Notification.Builder(this);
        Intent i = new Intent(this, MainMenuFragment.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        builder.setContentText("正在巡逻").setSmallIcon(R.drawable.app_icon).setContentTitle(getText(R.string.app_name));
        builder.setContentIntent(pi);
        Notification note = builder.getNotification();
        note.flags |= Notification.FLAG_NO_CLEAR;

        startForeground(ID_OF_NOTIFICATION, note);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        if (mLocationClient!=null&&mLocationClient.isStarted()){
            mLocationClient.stop();
        }
        if (mPlcBxReceiver!=null){ //退出时销毁
            unregisterReceiver(mPlcBxReceiver);
        }
        stopForeground(true);
        super.onDestroy();
    }

    /**
     * 更新用户状态
     *
     * @return
     */
    private String getWorkStatusJson(int LX, int GTID, int GTZSID, double LAT, double LNGT, String MS) {
        JSONObject requestJson = new JSONObject();
        requestJson.put("LX", LX+"");
        requestJson.put("GTID", GTID+"");
        requestJson.put("DW", AppContext.instance().getLoginInfo().getPcs());
        requestJson.put("GTZSID", GTZSID+"");
        requestJson.put("YH", AppContext.instance().getLoginInfo().getUsername());
        requestJson.put("LAT", LAT+"");
        requestJson.put("LNGT", LNGT+"");
        requestJson.put("LOCTYPE", 161+"");
        if (!TextUtils.isEmpty(MS)) {
            requestJson.put("MS", MS);
        }
        return requestJson.toJSONString();
    }


    class PlcBxReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int i= intent.getIntExtra("type",-1);
            switch (i){
                case 1:
                    startPlcBxCheck( intent);
                    break;
                case 2:
                    stopPlcBxCheck(intent);
                    break;
                case 3:
                    stopPlcBxCheck(intent);
                    break;
                case 5:
                    startPlcBxCheck( intent);
                    break;
            }
        }
    }

    /**
     * 关闭岗亭检测
     * @param intent
     */
    private void stopPlcBxCheck(Intent intent) {
        mPlcBxLocation=null;
        GTID=0;
        GTZSID=0;
    }

    /**
     * 开始岗亭检测
     * @param intent
     */
    private void startPlcBxCheck(Intent intent) {
        if (intent.getSerializableExtra("latLngt")!=null){
            mPlcBxLocation= (XinyiLatLng) intent.getSerializableExtra("latLngt");
            GTID=intent.getIntExtra("GTID",0);
            GTZSID=intent.getIntExtra("GTZSID",0);
        }
    }

    private void doPlcBxLeaving(final int lx, int GTID, int GTZSID, double LAT, double LNGT, String ms) {
        ApiResource.postUpdatePlcBxUserStatus(getWorkStatusJson(lx, GTID, GTZSID, LAT, LNGT, ms), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if ("true".equals(result)) {
                    //成功
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });


    }


    /**
     * 更新点位信息
     * @param
     */
    protected void updateLBSPoi(BDLocation locaionInfo) {
        final Map<String, String> maps = new HashMap<>();
        UserInfo user = AppContext.instance().getLoginInfo();
        if (user==null||locaionInfo==null)
            return;

        maps.put("title",user.getName());
        maps.put("address", locaionInfo.getAddrStr()==null?"":locaionInfo.getAddrStr());
        maps.put("tags", "巡逻");
        maps.put("user_phone", user.getCellphone()==null?"":user.getCellphone());
        maps.put("latitude",  locaionInfo.getLatitude()+"");
        maps.put("longitude", locaionInfo.getLongitude()+"");
        maps.put("coord_type", "2");
        maps.put("user", user.getUsername());
        maps.put("last_time", new Date().getTime() + "");
        maps.put("user_status", "1");
        ApiResource.postUpdatetLbsYunPosition(maps, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
//                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                if (!BaiduMapUtil.isOptionSuceess(result)){
                    if (BaiduMapUtil.RESULT_CODE_NO_POI==BaiduMapUtil.getResultCode(result)){ //点位信息不存在，则插入点位
                        addLBSPoi(maps);
//                        Toast.makeText(getApplicationContext(),"add",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("LocationService",result);
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d("LocationService",throwable.getMessage()==null?"update lbs poi error":throwable.getMessage());

            }
        });
    }


    /**
     * 添加新的位置信息
     * @param maps
     */
    protected void addLBSPoi(Map<String, String> maps) {
        ApiResource.postAddLbsYunPosition(maps, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String result = new String(bytes);
//                Toast.makeText(getApplicationContext(),"add result"+result,Toast.LENGTH_SHORT).show();
                if (!BaiduMapUtil.isOptionSuceess(result)){//插入失败
                    Log.d("LocationService",result);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d("LocationService",throwable.getMessage()==null?"add lbs poi error":throwable.getMessage());
            }
        });
    }


}
