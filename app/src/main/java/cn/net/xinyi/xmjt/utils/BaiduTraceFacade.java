package cn.net.xinyi.xmjt.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnGeoFenceListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.comm.core.utils.SharePrefUtils;

import org.apache.http.Header;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.BaiduFence;
import cn.net.xinyi.xmjt.model.HisTraceQueryModel;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.rxbus.RxBus;
import cn.net.xinyi.xmjt.rxbus.event.QueryHisTraceEvent;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import static cn.net.xinyi.xmjt.utils.BaiduMapUtil.SERVICE_ID;


/**
 * Created by  on 2016/4/13.
 */
public class BaiduTraceFacade {
    public static final String TAG = "BaiduTraceFacade";

    public static final String TYPE_XL = "1"; //巡逻围栏标识
    public static final String TYPE_GT = "2"; //岗亭围栏标识
    public static final String TYPE_NONE = "0"; //未指向
    public static String type_fence = TYPE_NONE;
    // 围栏半径
    private static int xlradius = 50;
    private static int gtradius = 350;
    //轨迹服务类型（0 : 不上传位置数据，也不接收报警信息； 1 : 不上传位置数据，但接收报警信息；2 : 上传位置数据，且接收报警信息）
    public static final int TRACE_TYPE = 2;
    //位置采集间隔时间单位s
    public static final int GATHER_INTERVAL = 5;
    //位置采集上传间隔时间单位s
    public static final int PACK_INTERVAL = 15;

    static OnTrackListener onTrackListener;
    // entity标识
    public static final UserInfo userInfo = AppContext.instance.getLoginInfo();

    private static LBSTraceClient client;
    private static Trace trace;
    private static boolean isDelete = false;

    private static OnXinyiGeoFenceListener mGeoFenceListener;

    protected static OnGeoFenceListener mOnGeoFenceListener = new OnGeoFenceListener() {
        //请求失败回调接口
        @Override
        public void onRequestFailedCallback(String arg0) {
            Log.d(TAG, "geoFence请求失败 : " + arg0);
        }

        //创建圆形围栏回调接口
        @Override
        public void onCreateCircularFenceCallback(String arg0) {
            Log.d(TAG, "创建圆形围栏回调接口消息 : " + arg0);
            Intent intent = new Intent(BaiduTraceService.FENCE_ACTION);
            intent.putExtra("data", arg0);
            intent.putExtra("type", BaiduTraceService.FENCE_CREATE);
            AppContext.instance().sendBroadcast(intent);
        }

        //更新圆形围栏回调接口
        @Override
        public void onUpdateCircularFenceCallback(String arg0) {
            Log.d(TAG, "更新圆形围栏回调接口消息 : " + arg0);
        }


        //删除围栏回调接口
        @Override
        public void onDeleteFenceCallback(String arg0) {
            Log.d(TAG, " 删除围栏回调接口消息 : " + arg0);
            Intent intent = new Intent(BaiduTraceService.FENCE_ACTION);
            intent.putExtra("data", arg0);
            intent.putExtra("type", BaiduTraceService.FENCE_DELETE);
            AppContext.instance().sendBroadcast(intent);
            if (mGeoFenceListener != null) {
                mGeoFenceListener.callBack(1, arg0);
            }
        }

        //查询围栏列表回调接口
        @Override
        public void onQueryFenceListCallback(String arg0) {
            Log.d(TAG, "查询围栏列表回调接口消息 : " + arg0);
            if (mGeoFenceListener != null) {
                mGeoFenceListener.callBack(1, arg0);
            }
            if (isDelete) {
                isDelete = false;
                if (BaiduMapUtil.isOptionSuceess(arg0)) {
                    List<BaiduFence> fences = JSON.parseArray(JSON.parseObject(arg0).getString("fences"), BaiduFence.class);
                    if (fences != null) {
                        for (BaiduFence f : fences) {
                            if (type_fence.equals(TYPE_NONE) || type_fence.equals(f.getDescription())) {
                                delFence(f.getFence_id());
                            }
                        }
                    }
                }
            } else {
                Intent intent = new Intent(BaiduTraceService.FENCE_ACTION);
                intent.putExtra("data", arg0);
                intent.putExtra("type", BaiduTraceService.FENCE_LIST);
                AppContext.instance().sendBroadcast(intent);
            }

        }

        //查询历史报警回调接口
        @Override
        public void onQueryHistoryAlarmCallback(String arg0) {
            Log.d(TAG, " 查询历史报警回调接口消息 : " + arg0);
            if (mGeoFenceListener != null) {
                mGeoFenceListener.callBack(1, arg0);
            }
        }

        //查询监控对象状态回调接口
        @Override
        public void onQueryMonitoredStatusCallback(String arg0) {
            Log.d(TAG, " 查询监控对象状态回调接口消息 : " + arg0);
            if (mGeoFenceListener != null) {
                mGeoFenceListener.callBack(1, arg0);
            }
        }
    };
    private static int startTime;
    private static Subscription subscribe;

    public static void init(Context applactionContext) {
        client = new LBSTraceClient(applactionContext);
        if (AppContext.instance().getLoginInfo() != null) {
            addEntity(applactionContext, AppContext.instance().getLoginInfo().getUsername(), AppContext.instance().getLoginInfo().getName(), AppContext.instance().getLoginInfo().getCellphone(), new OnEntityListener() {
                @Override
                public void onRequestFailedCallback(String s) {

                }
            });
        }
    }

    public static LBSTraceClient getClient() {
        return client;
    }

    private static boolean isStart = false;

    public static void start(String username, final Context applactionContext) {

        XinyiLog.e("TEST","isStart=="+isStart);


        //服务开启了就不在开启
        if (isStart) return;
        isStart = true;

        //初始化开始时间
        queryHistoryTrack();

        if (username == null)
            throw new NullPointerException("username can't be null");
        if (client == null)
            client = new LBSTraceClient(applactionContext);

        if (!BaseDataUtils.notLeader()) {
            return;
        }

        trace = new Trace(applactionContext, BaiduMapUtil.SERVICE_ID, username, TRACE_TYPE);
        client.setInterval(GATHER_INTERVAL, PACK_INTERVAL); //设置采集和上传周期
        //client.setLocationMode(LocationMode.Device_Sensors);

        XinyiLog.d("轨迹服务开启...");
        //实例化开启轨迹服务回调接口
        OnStartTraceListener startTraceListener = new OnStartTraceListener() {
            //开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int arg0, String arg1) {
                XinyiLog.e("TEST", "onTraceCallback");
                if (0 == arg0 || 10006 == arg0 || 10008 == arg0) {
                    Log.d(TAG, arg1);
                    //  UI.toast(applactionContext, "轨迹服务开启成功" + arg0 + arg1);
                    updateEntity(applactionContext, userInfo.getName(), userInfo.getName(), userInfo.getName(), new OnEntityListener() {
                        @Override
                        public void onRequestFailedCallback(String s) {

                        }
                    });
                } else {
                    Log.d(TAG, arg1);
                    //  UI.toast(applactionContext, "轨迹服务开启失败" + arg0 + arg1);
                }

            }


            // 轨迹服务推送接口（用于接收服务端推送消息，type : 消息类型，content : 消息内容，详情查看类参考）
            public void onTracePushCallback(byte type, String content) {
                Log.d(TAG, content);
                XinyiLog.e("TEST", "onTracePushCallback==" + content);
                if (0x03 == type) { //电子围栏消息
                    try {
                        JSONObject js = JSON.parseObject(content);
                        //   UI.toast(AppContext.instance,"电子围栏消息"+js.toJSONString());
                        if (null != js) {
                            //  UI.toast(applactionContext,js.toJSONString());
                            Intent intent = new Intent(BaiduTraceService.FENCE_ACTION);
                            intent.putExtra("data", content);
                            intent.putExtra("type", BaiduTraceService.FENCE_ALARM);
                            AppContext.instance().sendBroadcast(intent);
                            getRealLoc();
                        }
                    } catch (JSONException e) {
                    }
                } else {
                }
            }
        };

        initFence();
//
//        client.queryRealtimeLoc(SERVICE_ID, new OnEntityListener() {
//            @Override
//            public void onRequestFailedCallback(String s) {
//                UI.toast(applactionContext,"queryRealtimeLoc"+s);
//            }
//        });

        onTrackListener = new OnTrackListener() {
            @Override
            public void onRequestFailedCallback(String s) {
                XinyiLog.d(s);
            }

            @Override
            public void onQueryHistoryTrackCallback(String s) {
                Log.d("BaiduTraceFacade111", s);
                HisTraceQueryModel hisTraceQueryModel = GsonUtils.json2Object(s, HisTraceQueryModel.class);

                if (hisTraceQueryModel.getSize() > 0 && hisTraceQueryModel.getStatus() == 0) {
                    QueryHisTraceEvent queryHisTraceEvent = new QueryHisTraceEvent(QueryHisTraceEvent.EVENT_TRACEING);
                    queryHisTraceEvent.setHisTraceQueryModel(hisTraceQueryModel);
                    RxBus.getDefault().post(queryHisTraceEvent);
                }
            }

            @Override
            public Map onTrackAttrCallback() {
                Log.d(TAG, "onTrackAttrCallback");
                if (AppContext.instance().getLoginInfo() != null) {
                    Map<String, String> maps = new HashMap<>();
                    maps.put("phone", AppContext.instance().getLoginInfo().getCellphone());
                    maps.put("name", AppContext.instance().getLoginInfo().getName());
                    return maps;
                }
                return super.onTrackAttrCallback();
            }
        };
        client.setOnTrackListener(onTrackListener);
        client.startTrace(trace, startTraceListener);  //开启轨迹服务
    }

    private static SharedPreferences first_trace_time;

    private static void getStartTime() {
        if(first_trace_time==null)
        {
            first_trace_time = SharePrefUtils.getSharePrefEdit(AppContext.instance, "first_trace_time");
        }

        String key = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        String trace_time = first_trace_time.getString(key, "");
        if (StringUtils.isEmpty(trace_time)) {
            startTime =Integer.valueOf((int) (new Date().getTime()/ 1000)) ;

            SharedPreferences.Editor edit = first_trace_time.edit();
            edit.clear().commit();
            edit.putString(key, startTime + "");
            edit.commit();
        } else {
            startTime = Integer.parseInt(trace_time);
        }

        XinyiLog.e("TEST","START_TIME"+DateUtil.unixTimestampToDate(startTime));
    }


    private static void queryHistoryTrack() {
        getStartTime();
        XinyiLog.e("TEST", "queryHistoryTrack");
        //避免开启多个任务
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        // 轨迹服务ID
        final long serviceId = BaiduMapUtil.SERVICE_ID;
        // entity标识
        final String entityName = AppContext.instance().getLoginInfo().getUsername();
        // 是否返回精简结果
        final int simpleReturn = 0;
        // 是否纠偏
        final int isProcessed = 1;
        // 纠偏选项
        final String processOption = "need_denoise=1,need_vacuate=1,need_mapmatch=1";

        // 分页大小
        final int pageSize = 1000;
        // 分页索引
            final int pageIndex = 1;
        //延時
        subscribe = Observable.interval(0,40, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        XinyiLog.e("TEST", "callcallcall");
                        // 结束时间
                        int endTime = (int) (System.currentTimeMillis() / 1000);
                        if (client!=null) {
                            client.queryHistoryTrack(serviceId, entityName, simpleReturn, isProcessed,
                                    processOption, startTime, endTime, pageSize, pageIndex, onTrackListener);
                        }
                    }
                });

    }


    public static void addEntity(Context context, String entityName, String nickName, String phoneNo, OnEntityListener listener) {
        if (client == null)
            client = new LBSTraceClient(context);
        client.addEntity(SERVICE_ID, entityName, "name=" + nickName + ",phone=" + phoneNo, listener);
    }


    /**
     * 更新entity信息
     *
     * @param context
     * @param entityName
     * @param nickName
     * @param phoneNo
     * @param listener
     */
    public static void updateEntity(Context context, String entityName, String nickName, String phoneNo, OnEntityListener listener) {
        if (client == null)
            client = new LBSTraceClient(context);
        client.updateEntity(SERVICE_ID, entityName, "name=" + nickName + ",phone=" + phoneNo, listener);
    }


    public static void stop() {
        if (client == null)
            return;
        OnStopTraceListener stopTraceListener = new OnStopTraceListener() {

            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                //todo 处理成功
                if (subscribe != null && !subscribe.isUnsubscribed()) {
                    subscribe.unsubscribe();
                }
                isStart = false;
                SharedPreferences.Editor edit = first_trace_time.edit();
                edit.clear().commit();

                QueryHisTraceEvent queryHisTraceEvent = new QueryHisTraceEvent(QueryHisTraceEvent.EVENT_TRACE_STOP);
                RxBus.getDefault().post(queryHisTraceEvent);
            }

            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                //todo 处理失败
            }
        };

        //开启轨迹服务
        client.stopTrace(trace, stopTraceListener);
    }


    /**
     * 初始化地理围栏
     */
    public static void initFence() {
        if (client == null)
            init(AppContext.instance);
    }


    /**
     * 创建岗亭地理围栏
     * 只作用于创建那天
     */
    public static void createGTSimpleFence(String username, String fenceName, double lngt, double lat, int radius) {
        if (client == null)
            init(AppContext.instance);
        mGeoFenceListener = null;
        //  client.createCircularFence(SERVICE_ID, username, "g" + fenceName, TYPE_GT, username, username, "0600,2359", 1, DateUtil.date2String(new Date(), "yyyyMMdd"), "1,2,3,4,5,6,7", 3, lngt + "," + lat, radius, 3, mOnGeoFenceListener);
        ApiResource.createYinyanFence(username, "g" + fenceName, TYPE_GT, "0600,2359", 4, "3", lat, lngt, radius, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                XinyiLog.d(new String(bytes));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                //     XinyiLog.d(new String(bytes));
            }
        });

    }

    /**
     * 创建巡逻地理围栏
     * 只作用于创建那天
     */
    public static void createXLSimpleFence(String username, String fenceName, double lngt, double lat, int radius) {
        if (client == null)
            init(AppContext.instance);
        mGeoFenceListener = null;
        ApiResource.createYinyanFence(username, "x" + fenceName, TYPE_XL, "0600,2359", 4, "3", lat, lngt, radius, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                XinyiLog.d(new String(bytes));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
//        client.createCircularFence(SERVICE_ID, username, "x" + fenceName, TYPE_XL, username, username, "0600,2359", 1, DateUtil.date2String(new Date(), "yyyyMMdd"), "1,2,3,4,5,6,7", 3, lngt + "," + lat, radius, 3, mOnGeoFenceListener);
    }


    /**
     * 创建地理围栏
     * 只作用于创建那天
     * 50范围米
     */
    public static void createGTSimpleFence(String username, String fenceName, double lngt, double lat) {
        createGTSimpleFence(username, fenceName, lngt, lat, gtradius);
    }

    /**
     * 创建地理围栏
     * 只作用于创建那天
     * 50范围米
     */
    public static void createXLSimpleFence(String username, String fenceName, double lngt, double lat) {
        createXLSimpleFence(username, fenceName, lngt, lat, xlradius);
    }


    public static void delFence(int fenceId, OnGeoFenceListener listener) {
        if (client == null)
            init(AppContext.instance);
        mGeoFenceListener = null;
        client.deleteFence(SERVICE_ID, fenceId, listener);
    }

    public static void delFence(int fenceId) {
        if (client == null)
            init(AppContext.instance);
        mGeoFenceListener = null;
        client.deleteFence(SERVICE_ID, fenceId, mOnGeoFenceListener);
    }

    public static void getFenceList(String username, OnGeoFenceListener listener) {
        if (client == null)
            init(AppContext.instance);
        mGeoFenceListener = null;
        client.queryFenceList(SERVICE_ID, username, null, listener);
    }

    public static void getFenceList(String username) {
        if (client == null)
            init(AppContext.instance);
        mGeoFenceListener = null;
        client.queryFenceList(SERVICE_ID, username, null, mOnGeoFenceListener);
    }

    public static void getFenceList(String username, OnXinyiGeoFenceListener listener) {
        if (client == null)
            init(AppContext.instance);
        mGeoFenceListener = listener;
        client.queryFenceList(SERVICE_ID, username, null, mOnGeoFenceListener);
    }

    /**
     * 删除所有围栏
     *
     * @param username
     */
    public static void clearFence(String username) {
        isDelete = true;
        type_fence = TYPE_NONE;
        mGeoFenceListener = null;
//        getFenceList(username); //// TODO: 2016/6/14  这里改用服务端，客户端调不通
        clearFenceInServiceApi(username);
    }

    /**
     * 删除地理围栏
     *
     * @param username
     */
    private static void clearFenceInServiceApi(String username) {
        ApiResource.getYinyanFenceList(username, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (BaiduMapUtil.isOptionSuceess(new String(bytes))) {
                    List<BaiduFence> fences = JSON.parseArray(JSON.parseObject(new String(bytes)).getString("fences"), BaiduFence.class);
                    if (fences != null) {
                        for (BaiduFence f : fences) {
                            if (type_fence.equals(TYPE_NONE) || type_fence.equals(f.getDescription())) {
                                ApiResource.delYinyanFence(f.getFence_id() + "", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        XinyiLog.d(new String(bytes));
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        XinyiLog.d(throwable.getMessage());
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                XinyiLog.d(throwable.getMessage());
            }
        });
    }

    /**
     * 删除岗亭围栏
     *
     * @param username
     */
    public static void clearGTFence(String username) {
        isDelete = true;
        type_fence = TYPE_GT;
        mGeoFenceListener = null;
//        getFenceList(username); //todo 客户端调不通，改用服务端接口
        clearFenceInServiceApi(username);
    }


    /**
     * 删除巡逻围栏
     *
     * @param username
     */
    public static void clearXLFence(String username) {
        isDelete = true;
        type_fence = TYPE_XL;
        mGeoFenceListener = null;
        clearFenceInServiceApi(username);
//        getFenceList(username); //todo 客户端调不通，改用服务端接口

    }

    public static OnEntityListener mOnEntityListener = new OnEntityListener() {
        @Override
        public void onRequestFailedCallback(String s) {

        }

        @Override
        public void onAddEntityCallback(String s) {
            super.onAddEntityCallback(s);
        }

        @Override
        public void onQueryEntityListCallback(String s) {
            super.onQueryEntityListCallback(s);
        }

        @Override
        public void onReceiveLocation(TraceLocation traceLocation) {
            super.onReceiveLocation(traceLocation); //实时定位接口
            Intent intent = new Intent(BaiduTraceService.FENCE_ACTION);
            intent.putExtra("data", JSON.toJSONString(BaiduMapUtil.traceLocation2LocationInfo(traceLocation)));
            intent.putExtra("type", BaiduTraceService.FENCE_REALTIME_LOC);
            AppContext.instance().sendBroadcast(intent);
        }

        @Override
        public void onUpdateEntityCallback(String s) {
            super.onUpdateEntityCallback(s);
        }
    };


    /**
     * 获取实时位置信息
     */
    public static void getRealLoc() {
        if (client == null)
            throw new NullPointerException("you must excute the start mehtod first");
        mGeoFenceListener = null;
        client.queryRealtimeLoc(SERVICE_ID, mOnEntityListener);
    }


    /**
     * 地理围栏监听
     */
    public interface OnXinyiGeoFenceListener {
        void callBack(int type, String result);
    }


}
