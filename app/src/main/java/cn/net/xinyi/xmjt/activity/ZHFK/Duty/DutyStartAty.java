package cn.net.xinyi.xmjt.activity.ZHFK.Duty;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.pickerview.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseMapActivity;
import cn.net.xinyi.xmjt.model.DutyBoxModle;
import cn.net.xinyi.xmjt.model.DutyOperationModle;
import cn.net.xinyi.xmjt.model.HisTraceQueryModel;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.rxbus.RxBus;
import cn.net.xinyi.xmjt.rxbus.event.QueryHisTraceEvent;
import cn.net.xinyi.xmjt.service.BaiduTraceService;
import cn.net.xinyi.xmjt.utils.BaiduMapUtil;
import cn.net.xinyi.xmjt.utils.BaiduTraceFacade;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.DialogHelper;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.utils.UI;
import cn.net.xinyi.xmjt.utils.XinyiLog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static cn.net.xinyi.xmjt.service.BaiduTraceService.FENCE_ALARM;

/**
 * Created by hao.zhou on 2016/4/26.
 */
public class DutyStartAty extends BaseMapActivity implements View.OnClickListener {
    public static final String XL_STATUS_ACTION = "cn.net.xinyi.xmjt.activity.Work.XL.XLDKSAty.XL_STATUS";

    //地图下部列表布局
    private ListView mListview;//listview
    private Button btn_js;//结束巡逻
    private Button btn_ks;//开始巡逻
    private Button btn_bb;//开始报备
    private Button btn_bb_end;//结束报备
    private LinearLayout ll_empty_data;

    //巡逻类型
    public final static String TYPE_BEATS_START = "01";//巡逻开始
    public final static String TYPE_BEATS_START_A = "01A";//巡逻开始
    public final static String TYPE_BEATS_END = "01B";//巡逻结束
    public final static String TYPE_BOXS = "02";//签到
    public final static String TYPE_REPORTS_START = "03";//报备开始
    public final static String TYPE_REPORTS_START_A = "03A";//报备开始
    public final static String TYPE_REPORTS_END = "03B";//报备结束

    //巡逻段ID
    private String DUTY_BEATS_ID;
    protected static AppContext mContext;

    //定位信息
    private LocaionInfo loc;
    private double bWD;//纬度
    private double bJD;//经度
    private String dz;//地址

    //巡逻数据分类存储到不同的MAP中：  0 为未打卡状态  1为已经打卡
    private Map<Integer, ArrayList<DutyBoxModle>> maps = new HashMap<>();
    private DutyOperationModle operationInfo = new DutyOperationModle();
    public final static int NORMAL = 0;
    public final static int ABNORMAL = 1;

    private String LAST_TYPE;//最新巡逻类型

    private FenceReiver mReceiver;//广播

    private List<DutyOperationModle> operationInfos;//打卡记录信息

    private List<DutyBoxModle> list;

    private String XLJLID;
    private DutyStartAdp mAdapter;
    /**
     * 打卡状态
     * false为手动打卡
     * true位自动打卡
     */
    private boolean isAutoType;
    private String distance;
    private String LAST_ID;
    private boolean createBaiduTranFace;
    private TimePickerView pvTime;
    private int totalTime;
    private String createTime;
    private Subscription subscription;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        //SharePreference初始化
        settings = this.getSharedPreferences("PLATENO", 0);
        //巡逻段ID
        TypeUtils.compatibleWithJavaBean = true;
        DUTY_BEATS_ID = getIntent().getStringExtra("data");
        getDutyOpetationData();//获取点位数据 地图展示
        initMapClick();//地图点击事件初始化
        initTimePickerView();
        //广播通知开启自动打卡
        IntentFilter filter = new IntentFilter(BaiduTraceService.FENCE_ACTION);
        mReceiver = new FenceReiver();
        registerReceiver(mReceiver, filter);
        initDialog();
        initTraceListener();
    }

    private static Overlay overlay = null;
    private static MapStatusUpdate msUpdate;
    private static OverlayOptions overlayOptions;
    private static PolylineOptions polyline;

    private void initTraceListener() {
        //在地图上绘制轨迹
//路线添加
// 添加路线（轨迹）
        subscription = RxBus.getDefault().toObserverable(QueryHisTraceEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QueryHisTraceEvent>() {
                    @Override
                    public void call(QueryHisTraceEvent queryHisTraceEvent) {
                        XinyiLog.e("rxbus", "activity接受到了消息===" + Thread.currentThread().getName());
                        if (queryHisTraceEvent.getType() == QueryHisTraceEvent.EVENT_TRACEING) {
                            //在地图上绘制轨迹
                            HisTraceQueryModel hisTraceQueryModel = queryHisTraceEvent.getHisTraceQueryModel();
                            LatLng endPos = hisTraceQueryModel.getEndLatLng();
                            List<LatLng> mapList = hisTraceQueryModel.getListPoints2();

                            //路线添加
                            if (null != overlay) {
                                overlay.remove();
                            }

                            MapStatus mMapStatus = new MapStatus.Builder().target(endPos).build();
                            msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                            BitmapDescriptor realtimeBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);

                            overlayOptions = new MarkerOptions().position(endPos).icon(realtimeBitmap).zIndex(9).draggable(true);
                            if (mapList.size() >= 2 && mapList.size() <= 10000) {
                                // 添加路线（轨迹）
                                polyline = new PolylineOptions().width(10).color(Color.RED).points(mapList);
                            }
                            addMarker();
                        }
                    }
                });
    }


    /**
     * 添加地图覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            mMap.setMapStatus(msUpdate);
        }

        // 路线覆盖物
        if (null != polyline) {
            mMap.addOverlay(polyline);
        }

        // 实时点覆盖物
        if (null != overlayOptions) {
            overlay = mMap.addOverlay(overlayOptions);
        }
    }

    private void initTimePickerView() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTitle("请选择勤务时间");
        pvTime.setTime(DateUtil.string2Date("2016-10-10 08:00:00", "yyyy-MM-dd HH:mm:ss"));
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (DateUtil.Date2Minute(date) == 0) {
                    UI.toast(DutyStartAty.this, "勤务时间应该大于0");
                }else {
                    UI.toast(DutyStartAty.this, "勤务时间到期,将自动结束当前巡逻工作！");
                    operationInfo.setTOTALTIME(DateUtil.Date2Minute(date));
                    setBaseData(TYPE_BEATS_START, null, DUTY_BEATS_ID, XLJLID, null);
                    DutyBeatsListAty.intence.finish();
                }
            }
        });
    }

    View  myAddView;
    EditText et_plate;
    Button btn_canel;
    Button btn_add;
    AlertDialog alertDialog;

    //初始化自定义添加巡段AlertDialog
    private void initDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        myAddView = layoutInflater.inflate(R.layout.aty_duty_start_plateno, null);
        et_plate= (EditText) myAddView.findViewById(R.id.et_plate);
        btn_canel= (Button)myAddView.findViewById(R.id.btn_canel);
        btn_add= (Button)myAddView.findViewById(R.id.btn_add);
        btn_canel.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    private void showDialogAlert() {
        alertDialog=new AlertDialog.Builder(DutyStartAty.this)
                .setView(myAddView)
                .setCancelable(false).show();
        //初始化最后一次车牌号码值
        if (!StringUtils.isEmpty(settings.getString("PLATENO",""))){
            et_plate.setText(settings.getString("PLATENO",""));
        }
    }


    private void dismissAlert(boolean b) {//控制对话框是否关闭
        try {
            Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing" );
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(alertDialog,b);
            alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMapClick() {
        //地图点击事件初始化
        setMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker==null){
                    return true;
                }
                final DutyBoxModle NORMAL = (DutyBoxModle) marker.getExtraInfo().getSerializable("NORMAL");
                    final DutyBoxModle ABNORMAL = (DutyBoxModle) marker.getExtraInfo().getSerializable("ABNORMAL");
                    if (NORMAL != null) {
                        new AlertDialog.Builder(DutyStartAty.this).setTitle("手动打卡")
                                .setMessage("名称：" + NORMAL.getSID_NAME() + "\n地址：" + NORMAL.getSIGNBOX_DESCRIBE())
                                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        loc();
                                        if (((AppContext) getApplication()).getNetworkType() == 0) {
                                            toast(getString(R.string.network_not_connected));
                                        } else if (LAST_TYPE.equals(null) || LAST_TYPE.equals(TYPE_BEATS_END) || LAST_TYPE.equals("0")) {
                                            BaseUtil.showDialog("请开始巡逻！", DutyStartAty.this);
                                        } else if (DistanceUtil.getDistance(new LatLng(NORMAL.getBAIDU_COORDINATE_X(),
                                                NORMAL.getBAIDU_COORDINATE_Y()), new LatLng(bWD, bJD)) > 50) {

                                            toast("当前距离点位" + formatString.format(DistanceUtil.getDistance(new LatLng(NORMAL.getBAIDU_COORDINATE_X(),
                                                    NORMAL.getBAIDU_COORDINATE_Y()), new LatLng(bWD, bJD))) + "米，请到附近打卡！");
                                        } else {
                                            isAutoType = false;
                                            setBaseData(TYPE_BOXS, NORMAL.getSID(), DUTY_BEATS_ID, XLJLID, null);
                                        }
                                    }
                                })
                                .setNegativeButton(getString(R.string.cancel), null).show();
                    } else if (ABNORMAL != null) {
                        try {
                            long date = new Date(System.currentTimeMillis()).getTime() - df.parse(ABNORMAL.getCREATETIME()).getTime();
                            if (date > 1000 * 60 * 5) {
                                new AlertDialog.Builder(DutyStartAty.this).setTitle("手动打卡")
                                        .setMessage("巡逻点：" + ABNORMAL.getSID_NAME() + "\n地址：" + ABNORMAL.getSIGNBOX_DESCRIBE())
                                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                loc();
                                                if (((AppContext) getApplication()).getNetworkType() == 0) {
                                                    toast(getString(R.string.network_not_connected));
                                                } else if (LAST_TYPE.equals(null) || LAST_TYPE.equals(TYPE_BEATS_END) || LAST_TYPE.equals("0")) {
                                                    toast("请开始巡逻！");
                                                } else if (DistanceUtil.getDistance(new LatLng(ABNORMAL.getBAIDU_COORDINATE_X(),
                                                        ABNORMAL.getBAIDU_COORDINATE_Y()), new LatLng(bWD, bJD)) > 50) {
                                                    toast("当前距离点位" + formatString.format(DistanceUtil.getDistance(new LatLng(ABNORMAL.getBAIDU_COORDINATE_X(),
                                                            ABNORMAL.getBAIDU_COORDINATE_Y()), new LatLng(bWD, bJD))) + "米，请到附近打卡！");
                                                } else {
                                                    loc();
                                                    isAutoType = false;
                                                    setBaseData(TYPE_BOXS, ABNORMAL.getSID(), DUTY_BEATS_ID, XLJLID, null);
                                                }
                                            }
                                        })
                                        .setNegativeButton(getString(R.string.cancel), null).show();
                            } else {
                                BaseUtil.showDialog("请在" + (5 - (date / 60000)) + "分钟后打卡！", DutyStartAty.this);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                return true;
            }
        });

    }

    //动态加载地图 下部分listView
    @Override
    public void setupFooterView(LinearLayout parent) {
        super.setupFooterView(parent);
        View addView = LayoutInflater.from(this).inflate(R.layout.aty_duty_start, parent);
        ll_empty_data = (LinearLayout) addView.findViewById(R.id.ll_empty_data);
        mListview = (ListView) addView.findViewById(R.id.lv_list);
        btn_js = (Button) addView.findViewById(R.id.btn_js);
        btn_js.setOnClickListener(this);
        btn_ks = (Button) addView.findViewById(R.id.btn_ks);
        btn_ks.setOnClickListener(this);
        btn_bb = (Button) addView.findViewById(R.id.btn_bb);
        btn_bb.setOnClickListener(this);
        btn_bb_end = (Button) addView.findViewById(R.id.btn_bb_end);
        btn_bb_end.setOnClickListener(this);
        parent.setVisibility(View.VISIBLE);
    }


    //定位参数
    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess)
            super.onReceiveLoc(location, isSuccess, errMsg);
        loc = location;
        bWD = loc.getLat();
        bJD = loc.getLongt();
        dz = loc.getAddress();
    }

    //获得打卡数据  显示在ListView上
    private void getDutyOpetationData() {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("TEL_NUMBER", userInfo.getUsername());
        jo.put("DUTY_BEATS_ID", DUTY_BEATS_ID);
        ApiResource.getDutyOperationData(jo.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        String result = new String(bytes);
                        JSONArray js = JSON.parseArray(result);
                        if (js.size() > 0) {
                            LAST_TYPE = ((JSONObject) js.get(0)).getString("LAST_TYPE");  //获取当前最新状态
                        }
                        /**0 为 从未打过卡、TYPE_BEATS_END 为 结束巡逻状态***/
                        if (LAST_TYPE.equals(null)
                                || LAST_TYPE.equals("0")
                                || LAST_TYPE.equals(TYPE_BEATS_END)) {
                            getDutyBoxAllList();
                            initAdapter();//初始化列表；
                            BaiduTraceFacade.clearXLFence(getUsername());
                            BaiduTraceService.stopXl(getApplicationContext());
                        } else {
                            if (js.size() > 0) {
                                XLJLID = ((JSONObject) js.get(0)).getString("ID"); //获取巡逻记录ID
                                totalTime = ((JSONObject) js.get(0)).getInteger("TOTALTIME") == null ? 0 : ((JSONObject) js.get(0)).getInteger("TOTALTIME"); //勤务时间
                                createTime = ((JSONObject) js.get(0)).getString("CREATETIME"); //创建时间
                                //巡逻记录数据 在listview显示
                                operationInfos = JSON.parseObject(((JSONObject) js.get(0)).getString("DATALIST"), new TypeReference<List<DutyOperationModle>>() {
                                });
                            }
                            getXLDWList();
                            initAdapter();//初始化列表；
                            BaiduTraceService.startXl(getApplicationContext(), XLJLID + "");
                            LAST_ID = operationInfos.get(operationInfos.size() - 1).getID();
                            if (TYPE_BEATS_START_A.equals(LAST_TYPE)//开始巡逻
                                    || TYPE_BOXS.equals(LAST_TYPE)//打卡
                                    || DutyPoliceAty.TYPE_POLICE_END.equals(LAST_TYPE)//结束处警
                                    || TYPE_REPORTS_END.equals(LAST_TYPE)) {//结束报备
                                btn_js.setVisibility(View.VISIBLE);
                                btn_bb.setVisibility(View.VISIBLE);
                                btn_ks.setVisibility(View.GONE);
                                btn_bb_end.setVisibility(View.GONE);
                            } else if (TYPE_REPORTS_START_A.equals(LAST_TYPE)) {//开始报备
                                operationInfo.setDESCRIPTION("");//清空备注信息
                                btn_bb_end.setVisibility(View.VISIBLE);
                                btn_bb.setVisibility(View.GONE);
                                btn_js.setVisibility(View.GONE);
                                btn_ks.setVisibility(View.GONE);
                            }

                            //判断勤务时间是否过期，如果过期自动结束当前巡逻
                            if (totalTime > 0 && DateUtil.minuteBetween(DateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"), createTime) > totalTime) {
                                DialogHelper.showAlertDialog(DutyStartAty.this, "当前勤务时间已到，自动结束当前巡逻！", new DialogHelper.OnOptionClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, Object o) {
                                        getDistance(TYPE_BEATS_END);
                                    }
                                });
                            }
                        }
                        stopLoading();
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                initAdapter();//初始化列表；
                showToast("获取打卡数据失败");
            }
        });
    }

    //onClick 点击时间
    @Override
    public void onClick(View view) {
        //检测是否连接网络
        int networkType = ((AppContext) getApplication()).getNetworkType();
        if (BaseDataUtils.isFastClick()) {
        } else if (networkType == 0) {
            toast(getString(R.string.network_not_connected));
        } else {
            switch (view.getId()) {

                case R.id.btn_canel:
                    dismissAlert(true);
                    ((FrameLayout)myAddView.getParent()).removeView(myAddView);
                    break;

                case R.id.btn_add:
                    if (TextUtils.isEmpty(et_plate.getText().toString())) {
                        toast("车牌号不能为空！");
                        dismissAlert(false);//控制点击 diaAlert是否出现
                    }else {
                        dismissAlert(true);
                        ((FrameLayout)myAddView.getParent()).removeView(myAddView);//移除view,否则会报错
                        pvTime.show();
                    }

                    break;

                case R.id.btn_ks://开始巡逻
                    if (getIntent().getStringExtra("BEAT_TYPE").equals("02")) {//车巡
                        showDialogAlert();
                    }else {
                        pvTime.show();
                    }
                    break;

                case R.id.btn_js://结束巡逻
                    try {
                        if (totalTime > 0 && DateUtil.minuteBetween(DateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"), createTime) < totalTime) {
                            DialogHelper.showAlertDialog("当前勤务时间未到，是否结束本次巡逻？", DutyStartAty.this, new DialogHelper.OnOptionClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, Object o) {
                                    getDistance(TYPE_BEATS_END);
                                }
                            });
                        } else {
                            DialogHelper.showAlertDialog("确定结束本次巡逻", DutyStartAty.this, new DialogHelper.OnOptionClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, Object o) {
                                    getDistance(TYPE_BEATS_END);
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    new AlertDialog.Builder(DutyStartAty.this).setTitle(getString(R.string.xl_tjs))
//                            .setMessage("确定结束本次巡逻")
//                            .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    getDistance(TYPE_BEATS_END);
//                                }
//                            })
//                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            }).show();
                    break;

                case R.id.btn_bb://开始报备
//                    DialogHelper.showAlertDialog(this,getString(R.string.bbtips), new DialogHelper.OnOptionClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which, Object o) {
//
//                        }
//                    });
                    LinearLayout container = new LinearLayout(DutyStartAty.this);
                    container.setOrientation(LinearLayout.VERTICAL);
                    final Spinner spinner = new Spinner(DutyStartAty.this);
                    spinner.setBackgroundColor(getResources().getColor(R.color.white));
                    String reasons[] = new String[4];
                    reasons[0] = "吃饭";
                    reasons[1] = "车辆故障";
                    reasons[2] = "下雨";
                    reasons[3] = "其它";
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DutyStartAty.this, R.layout.simple_spinner_dropdown_item, reasons);
                    spinner.setAdapter(adapter);

                    final TextView tv = new TextView(DutyStartAty.this);
                    tv.setHeight(2);
                    tv.setBackgroundColor(getResources().getColor(R.color.bg_dark));

                    final EditText editText = new EditText(DutyStartAty.this);
                    editText.setLines(3);
                    editText.setHint(getString(R.string.bz));
                    editText.setTextSize(14);
                    editText.setBackgroundColor(getResources().getColor(R.color.white));

                    container.addView(spinner);
                    container.addView(tv);
                    container.addView(editText);
                    new AlertDialog.Builder(DutyStartAty.this).setTitle("离岗报备").setView(container).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setBaseData(TYPE_REPORTS_START, null, DUTY_BEATS_ID, XLJLID, spinner.getSelectedItem() + "," + editText.getText().toString());
                        }
                    }).show();
                    break;

                case R.id.btn_bb_end://结束报备
                    new AlertDialog.Builder(DutyStartAty.this).setTitle("结束报备")
                            .setMessage("结束报备继续巡逻")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (((AppContext) getApplication()).getNetworkType() == 0) {
                                        toast(getString(R.string.network_not_connected));
                                    } else {
                                        updateData(TYPE_REPORTS_END);
                                    }
                                }
                            }).show();
                    break;
            }
        }
    }

    /**
     * 根据开始结束经纬度  鹰眼获取 距离
     */
    private void getDistance(final String dutyType) {
        if (TYPE_BEATS_END.equals(dutyType) && DateUtil.string2Date(operationInfos.get(0).getCREATETIME(), "yyyy-MM-dd HH:mm:ss") != null) { //结束巡逻且巡逻时间不为null
            showLoadding();
            Date startDate = DateUtil.string2Date(operationInfos.get(0).getCREATETIME(), "yyyy-MM-dd HH:mm:ss");
            ApiResource.getTraceHistroy(AppContext.instance().getLoginInfo().getUsername(), startDate.getTime() / 1000, new Date().getTime() / 1000, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = new String(bytes);
                    if (BaiduMapUtil.isOptionSuceess(result)) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(result);
                            DecimalFormat format = new DecimalFormat("##0.00");//保留2位小数
                            distance = Double.parseDouble(format.format(jsonObject.getDouble("distance"))) + "";
                        } catch (JSONException e) {

                        } finally {
                            updateData(dutyType);
                        }
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    stopLoading();
                    updateData(dutyType);
                }
            });
        } else {
            updateData(dutyType);
        }
    }

    /**
     * @param status   当前最新巡逻状态
     * @param box_id   签到箱ID
     * @param beats_id 巡逻段ID
     * @param XLJLID   巡逻记录ID
     * @param des      描述
     **/

    private void setBaseData(String status, String box_id, String beats_id, String XLJLID, String des) {
        operationInfo.setDUTY_OPR_TYPE(status);//当前最新状态代码
        operationInfo.setDUTY_OPR_NAME(BaseDataUtils.dutyNumToDutyType(status));//当前最新状态中文
        operationInfo.setLON(bJD);//经度
        operationInfo.setLAT(bWD);//纬度
        operationInfo.setADDRESS(dz);//地址
        operationInfo.setPARENT_ID(XLJLID);//巡逻记录ID
        operationInfo.setDUTY_BEATS_ID(beats_id);//巡段ID
        operationInfo.setDUTY_SIGNBOX_ID(box_id);//签到箱ID
        operationInfo.setDESCRIPTION(des);//报备内容
        if (!StringUtils.isEmpty(et_plate.getText().toString())){
            operationInfo.setPLATENO(et_plate.getText().toString());//车牌号
            SharedPreferences.Editor localEditor = settings.edit();
            localEditor.clear().commit();//清除历史记录
            localEditor.putString("PLATENO",et_plate.getText().toString());//记录车牌号码
            localEditor.commit();
        }
        // 巡逻开始、打卡、报备开始、处警开始 执行上传数据操作
        uploadInfo(operationInfo);
    }

    //异步上传采集数据到服务端
    public void uploadInfo(final DutyOperationModle info) {
        showLoadding();
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        jo.put("USERID", userInfo.getId());
        jo.put("SHOWNAME", userInfo.getName());
        jo.put("TEL_NUMBER", userInfo.getUsername());
        String json = jo.toJSONString();
        ApiResource.addDutyOpraation(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty() && result.length() > 4) {
                    stopLoading();
//                    Intent refreshStateIntent = new Intent(XL_STATUS_ACTION); //通知MainQW界面更新状态
//                    sendBroadcast(refreshStateIntent);
                    if (TYPE_BEATS_START.equals(info.getDUTY_OPR_TYPE())//开始巡逻
                            || TYPE_BOXS.equals(info.getDUTY_OPR_TYPE())//打卡
                            || TYPE_REPORTS_START.equals(info.getDUTY_OPR_TYPE())//报备开始
                            || TYPE_REPORTS_END.equals(info.getDUTY_OPR_TYPE())) {//报备结束
                        if (info.getDUTY_OPR_TYPE().equals(TYPE_BEATS_START)) {
                            showToast(getString(R.string.xl_tks));
                            createBaiduTranFace = true;
                        } else if (info.getDUTY_OPR_TYPE().equals(TYPE_BOXS)) {
                            toast("您当前在" + info.getADDRESS() + (isAutoType == true ? "自动" : "手动") + "打卡成功！");
                        }
                        getDutyOpetationData();//刷新数据
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                showToast("打卡失败，请稍候重试！");
            }
        });
    }

    /**
     * 巡逻结束、 报备结束、处警结束  执行对数据的更新操作
     */
    public void updateData(final String dutyType) {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("DUTY_OPR_TYPE", dutyType.substring(0, 2));
        jo.put("ID", dutyType.equals(TYPE_BEATS_END) ? XLJLID : LAST_ID);
        jo.put("PARENT_ID", XLJLID);
        jo.put("DISTANCE", distance);
        String json = jo.toJSONString();
        ApiResource.updateDutyOpraation(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (!result.isEmpty() && result.equals("true")) {
                    stopLoading();
//                    Intent refreshStateIntent = new Intent(XL_STATUS_ACTION);
//                    sendBroadcast(refreshStateIntent);
                    if (TYPE_BEATS_END.equals(dutyType)) {
                        showToast(getString(R.string.xl_tjs));//提示巡逻结束
                        BaiduTraceFacade.clearXLFence(getUsername());//清除鹰眼记录
                        BaiduTraceService.stopXl(getApplicationContext());//停止鹰眼轨迹服务
                        showActivity(DutyOperHistoryAty.class);//跳转巡逻记录历史界面
                        DutyStartAty.this.finish();//巡逻界面关闭
                    } else if (TYPE_REPORTS_END.equals(dutyType)) {//报备结束
                        getDutyOpetationData();//刷新数据
                    }
                } else {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (bytes != null) {
                    stopLoading();
                    showToast("打卡失败，请稍候重试！");
                }
            }
        });
    }

    //adapter初始化
    private void initAdapter() {
        if (null == operationInfos || operationInfos.size() == 0) {//没有数据
            mListview.setVisibility(View.GONE);
            ll_empty_data.setVisibility(View.VISIBLE);
        } else {
            mListview.setVisibility(View.VISIBLE);
            ll_empty_data.setVisibility(View.GONE);
            mAdapter = new DutyStartAdp(mListview, operationInfos, R.layout.aty_dutyhistory_item_adp, DutyStartAty.this);
            mListview.setAdapter(mAdapter);
        }
    }

    /**
     * 巡逻段下属的巡逻点列表
     * 区别打卡和未打卡数据
     *
     * @return
     */
    private String getPlcSttnLists() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("DUTY_BEATS_ID", DUTY_BEATS_ID);
        requestJson.put("TEL_NUMBER", userInfo.getUsername());
        return requestJson.toJSONString();
    }

    private void getXLDWList() {
        showLoadding();
        ApiResource.getUserLastXldData(getPlcSttnLists(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                list = JSON.parseArray(result, DutyBoxModle.class);

                if (list != null) {
                    for (DutyBoxModle y : list) {
                        if (y.getISXL() == NORMAL) {//未打卡数据
                            if (maps.get(NORMAL) == null)
                                maps.put(NORMAL, new ArrayList<DutyBoxModle>());
                            maps.get(NORMAL).add(y);
                        } else {//已经打卡数据
                            try {
                                //如果距离上一次打卡时间超过5分钟 可以再次打卡
                                if (new Date(System.currentTimeMillis()).getTime() - df.parse(y.getCREATETIME()).getTime() > 1000 * 60 * 5) {
                                    if (maps.get(NORMAL) == null)
                                        maps.put(NORMAL, new ArrayList<DutyBoxModle>());
                                    maps.get(NORMAL).add(y);
                                } else {
                                    if (maps.get(ABNORMAL) == null)
                                        maps.put(ABNORMAL, new ArrayList<DutyBoxModle>());
                                    maps.get(ABNORMAL).add(y);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    setPlcBxMarker(maps.get(NORMAL), R.drawable.map_xl);
                    setPlcBxMarker(maps.get(ABNORMAL), R.drawable.map_xl_no);
                    stopLoading();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }


    /**
     * 巡逻段下属的巡逻点列表
     * 未区别 打卡和未打卡数据
     *
     * @return
     */
    private String getPlcSttnList() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("F_BID", DUTY_BEATS_ID);
        return requestJson.toJSONString();
    }

    private void getDutyBoxAllList() {
        ApiResource.getDutyBoxList(getPlcSttnList(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                list = JSON.parseArray(result, DutyBoxModle.class);
                if (list != null) {
                    setPlcBxMarker(list, R.drawable.map_xl);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }


    /**
     * 设置巡逻点标注
     *
     * @param markers
     * @param drawableId
     */
    public void setPlcBxMarker(List<DutyBoxModle> markers, int drawableId) {
        if (markers == null || markers.size() == 0)
            return;

        XinyiLog.e("TEST", "DutyBoxModleLIST" + Arrays.toString(markers.toArray()));

        for (DutyBoxModle x : markers) {

            if (x.getBAIDU_COORDINATE_X() > 0 && mMapView != null) {
                LatLng ll = new LatLng(x.getBAIDU_COORDINATE_X(), x.getBAIDU_COORDINATE_Y());
                LatLng latLng = new LatLng(ll.latitude, ll.longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(drawableId);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
                //在地图上添加Marker，并显示
                Overlay overlay = mMap.addOverlay(option);
                Bundle bundle = new Bundle();
                /**点击开始巡逻 会创建电子围栏  其余不创建*/
                if (createBaiduTranFace) {
                    BaiduTraceFacade.createXLSimpleFence(getUsername(), x.getSID() + "", x.getBAIDU_COORDINATE_Y(), x.getBAIDU_COORDINATE_X());
                }
                if (createBaiduTranFace || x.getISXL() == NORMAL) {
                    bundle.putSerializable("NORMAL", x);
                } else {
                    bundle.putSerializable("ABNORMAL", x);
                }
                overlay.setExtraInfo(bundle);
            }
        }
        createBaiduTranFace = false;
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_beats);
    }

    @Override
    public boolean needKeepLoc() {
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if(subscription!=null&&!subscription.isUnsubscribed())
        {
            subscription.unsubscribe();;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DutyStartAty.this.finish();
                break;
            case R.id.action_list:
                showActivity(DutyOperHistoryAty.class);
                break;
            default:
                break;
        }
        return true;
    }


    class FenceReiver extends BroadcastReceiver {
        long lastClockInTime = 0l;
        int MIN_SPAN_TIME = 3 * 1000; //30s不能再次接受到广播不做处理
        int MIN_SPAN_TIME_CLOCK_TIME = 5 * 6 * 1000; //5分钟重复一个打卡点位忽略
        String lastPatrolId = ""; //上次巡逻打卡ID

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("data");
            int type = intent.getIntExtra("type", 0);
            XinyiLog.e("TEST", "DutyStartAty,onReceive222" + msg + "  " + type);
            switch (type) {
                case FENCE_ALARM:
                    try {
                        JSONObject js = JSON.parseObject(msg);
                        // toast(js.toJSONString());
                        if (null != js) {
                            String mPerson = js.getString("monitored_person");
                            String action = js.getIntValue("action") == 1 ? "进入" : "离开";
                            toast("[" + mPerson + "]于" + new BaseDataUtils().getNowData() + "【" + action + "】点位");
                            if (LAST_TYPE.equals(null) || LAST_TYPE.equals(TYPE_BEATS_END) || LAST_TYPE.equals("0")) {
                                BaseUtil.showDialog("自动打卡失败：请开始巡逻", DutyStartAty.this);
                            } else if (js.getIntValue("action") == 1) {
                                long now = new Date().getTime();
                                if (AppContext.lastTime == 0l || now - AppContext.lastTime > MIN_SPAN_TIME) { //忽视30秒内重复发送的广播事件
                                    AppContext.lastTime = now;
                                    //签到点ID
                                    String duty_box_id = BaiduTraceService.getXLDID(js.getString("fence"));
                                    //如果是不同点位，则不做拦截，如果是5分钟内是同一个点位，只允许打一次卡
                                    if (!lastPatrolId.equals(duty_box_id) || (lastPatrolId.equals(BaiduTraceService.getXLDID(js.getString("fence"))) && now - lastClockInTime > MIN_SPAN_TIME_CLOCK_TIME)) { //忽视5分钟内同一个巡逻ID打卡的事件
                                        loc();
                                        lastClockInTime = now;
                                        isAutoType = true;
                                        setBaseData(TYPE_BOXS, duty_box_id, DUTY_BEATS_ID, XLJLID, null);
                                        lastPatrolId = duty_box_id;
                                        if (lastPatrolId == null)
                                            lastPatrolId = "";
                                    } else {
                                        toast("自动打卡失败,该点位打卡时间间隔不足5分钟");
                                    }
                                }
                            } else {
                                toast("打卡间隔小于30");
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                    }
                    break;
            }
        }
    }
}