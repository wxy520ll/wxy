package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnTrackListener;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.person.PersonActivity;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseMapActivity;
import cn.net.xinyi.xmjt.model.DevicesModel;
import cn.net.xinyi.xmjt.model.DevicesPositionModel;
import cn.net.xinyi.xmjt.model.HisTraceQueryModel;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.PloceMapModle;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.model.TaskModel;
import cn.net.xinyi.xmjt.model.UnitModel;
import cn.net.xinyi.xmjt.model.View.TaskListModel;
import cn.net.xinyi.xmjt.utils.BaiduMapUtil;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DateUtil;
import cn.net.xinyi.xmjt.utils.GsonUtils;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;
import cn.net.xinyi.xmjt.utils.StringUtils;

import static cn.net.xinyi.xmjt.activity.Main.TabMenuActivity.instace;
import static cn.net.xinyi.xmjt.config.AppContext.getDeviceListPostion;
import static cn.net.xinyi.xmjt.config.AppContext.getOverlays;
import static cn.net.xinyi.xmjt.config.AppContext.getUnitModelMap;


public class PlcDstrbtMpAty extends BaseMapActivity {

    private final static int NORMAL = 1;
    private final static int ABNORMAL = 2;
    private LinearLayout ll_polinfo;
    private TextView tv_sspcs;
    private TextView tv_account;
    private TextView tv_mz;
    private Button bt_call;
    private static Map<Integer, List<PloceMapModle>> maps = new HashMap<Integer, List<PloceMapModle>>();
    private FrameLayout fl_police_tab_header;
    private RadioGroup police_header_radio;
    private Button btn_showperson;
    private Button btn_time;
    private OnTrackListener trackListener;
    private String adminPhone;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private boolean checkbox1 = true;
    private boolean checkbox2 = false;
    private Handler handler;
    private HashMap<String, String> hashMap = new HashMap<>();

    Gson gson = new Gson();
    private  HashMap<String, DevicesModel.DataBean> devicesMap ;
    private  HashMap<String, TaskListModel.DataBean> taskMap;
    private  HashMap<String, UnitModel.DataBean> unitModelMap;


    {
        hashMap.put("1", "未开始");
        hashMap.put("2", "开始");
        hashMap.put("3", "结束");
        hashMap.put("4", "撤防");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
        checkBox2.setVisibility(View.VISIBLE);
        checkBox1.setVisibility(View.VISIBLE);
        trackListener = new OnTrackListener() {
            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                stopLoading();
            }

            // 查询历史轨迹回调接口
            @Override
            public void onQueryHistoryTrackCallback(String arg0) {
                super.onQueryHistoryTrackCallback(arg0);
                showHistoryTrack(arg0);
                stopLoading();
            }
        };


        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ll_polinfo.setVisibility(View.GONE);
                mMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {



                Serializable data = marker.getExtraInfo().getSerializable("data");
                if (data instanceof PloceMapModle) {
                    if (!checkBox1.isChecked())
                        return true;
                    final PloceMapModle ploice = (PloceMapModle) data;
                   /* tv_sspcs.setText(ploice.getZDZ());
                    tv_account.setText(ploice.getACCOUNTTYPE());
                    tv_mz.setText(ploice.getNAME() + "(" + ploice.getCJYH() + ")");
                    bt_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();// 创建一个意图
                            intent.setAction(Intent.ACTION_CALL);// 指定其动作为拨打电话 添加打电话的动作
                            intent.setData(Uri.parse("tel:" + ploice.getCJYH()));// 指定要拨出的号码
                            startActivity(intent);// 执行动作
                        }
                    });
                    ll_polinfo.setVisibility(View.VISIBLE);*/


                    //创建InfoWindow展示的view
                    Button button = new Button(getApplicationContext());
                    button.setTextColor(getResources().getColor(R.color.bthumbnail_font));
                    button.setText(ploice.getZDZ() + "\n" + ploice.getNAME() + "(" + ploice.getCJYH() + ")");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PlcDstrbtMpAty.this).setMessage("拨打" + ploice.getCJYH()).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + ploice.getCJYH());
                                    intent.setData(data);
                                    startActivity(intent);
                                }
                            });

                            builder.show();
                        }
                    });

                    button.setBackgroundResource(R.drawable.map_pickup);
                    //定义用于显示该InfoWindow的坐标点
                    LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                    //显示InfoWindow
                    mMap.showInfoWindow(mInfoWindow);

                } else if (data instanceof PoliceBoxModle) {

                    final PoliceBoxModle pb = (PoliceBoxModle) data;
                    //创建InfoWindow展示的view
                    Button button = new Button(getApplicationContext());
                    button.setTextColor(getResources().getColor(R.color.bthumbnail_font));
                    if (pb.getZSRY() != null) {
                        button.setText(pb.getUNIFIEDNO() + "\n" + pb.getADDRESS() + "\n" + pb.getZSRY());
                    } else {
                        button.setText(pb.getUNIFIEDNO() + "\n" + pb.getADDRESS());
                    }

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PlcDstrbtMpAty.this).setMessage("拨打" + pb.getLINKMAN() + "-" + pb.getPHONENO()).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + pb.getPHONENO());
                                    intent.setData(data);
                                    startActivity(intent);
                                }
                            });

                            builder.show();

                        }
                    });

                    button.setBackgroundResource(R.drawable.map_pickup);
                    //定义用于显示该InfoWindow的坐标点
                    LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                    //显示InfoWindow
                    mMap.showInfoWindow(mInfoWindow);
                } else if (data != null && data instanceof TaskListModel.DataBean) {
                    if (!checkBox2.isChecked())
                        return true;

                    TaskListModel.DataBean dataBean = (TaskListModel.DataBean) data;
                    //创建InfoWindow展示的view
                    Button button = new Button(getApplicationContext());
                    button.setTextColor(getResources().getColor(R.color.bthumbnail_font));

                    String unit = "";
                    if (unitModelMap.size()!=0){
                        UnitModel.DataBean d= unitModelMap.get(dataBean.getUnitId());
                        unit = d.getCity() + d.getDistrict() + d.getName();
                    }
                    button.setText(dataBean.getEscortType() + " " + dataBean.getDestination() + "(" + hashMap.get(dataBean.getState()) + ")" + "\n" + unit);
                    button.setBackgroundResource(R.drawable.map_pickup);
                    //定义用于显示该InfoWindow的坐标点
                    LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                    //显示InfoWindow
                    mMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

    }

    /**
     * 显示历史轨迹
     *
     * @param historyTrack
     */
    private void showHistoryTrack(String historyTrack) {

        Log.e("TEST", historyTrack);
        HisTraceQueryModel historyTrackData = GsonUtils.json2Object(historyTrack, HisTraceQueryModel.class);

        List<LatLng> latLngList = new ArrayList<LatLng>();
        if (historyTrackData != null && historyTrackData.getStatus() == 0) {
            if (historyTrackData.getListPoints() != null) {
                latLngList.addAll(historyTrackData.getListPoints2());
            }
            // 绘制历史轨迹
            drawHistoryTrack(latLngList);
        }

    }

    private MapStatusUpdate msUpdate = null;
    // 起点图标
    private static BitmapDescriptor bmStart;
    // 终点图标
    private static BitmapDescriptor bmEnd;

    // 起点图标覆盖物
    private static MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private static MarkerOptions endMarker = null;
    // 路线覆盖物
    public static PolylineOptions polyline = null;
    private static MarkerOptions markerOptions = null;

    private List<Overlay> policesOverlay = new ArrayList<>();

    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    private void drawHistoryTrack(final List<LatLng> points) {
        // 绘制新覆盖物前，清空之前的覆盖物
        mMap.clear();

        if (points.size() == 1) {
            points.add(points.get(0));
        }

        if (points == null || points.size() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast("当前查询无轨迹点");
                }
            });

        } else if (points.size() > 1) {

            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);

            bmStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);

            // 添加起点图标
            startMarker = new MarkerOptions()
                    .position(points.get(points.size() - 1)).icon(bmStart)
                    .zIndex(9).draggable(true);

            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(points);

            markerOptions = new MarkerOptions();
            markerOptions.flat(true);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding));
            markerOptions.position(points.get(points.size() - 1));
            addMarker();
        }
    }

    /**
     * 添加覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            mMap.animateMapStatus(msUpdate, 2000);
        }

        if (null != startMarker) {
            mMap.addOverlay(startMarker);
        }

        if (null != endMarker) {
            mMap.addOverlay(endMarker);
        }

        if (null != polyline) {
            mMap.addOverlay(polyline);
        }
    }

    private static LBSTraceClient client;

    private void initView() {
        //警力分布个人信息展示
        ll_polinfo = (LinearLayout) findViewById(R.id.ll_polinfo);
        tv_sspcs = (TextView) findViewById(R.id.tv_sspcs);//所属派出所
        tv_account = (TextView) findViewById(R.id.tv_account);//人员身份
        tv_mz = (TextView) findViewById(R.id.tv_mz);//联系人
        bt_call = (Button) findViewById(R.id.bt_call);//拔打电话
        checkBox1 = (CheckBox) findViewById(R.id.check1);//警力信息
        checkBox2 = (CheckBox) findViewById(R.id.check2);//脚镣信息
        //头部tab
        fl_police_tab_header = (FrameLayout) findViewById(R.id.fl_police_tab_header);
        fl_police_tab_header.setVisibility(View.VISIBLE);
        police_header_radio = (RadioGroup) fl_police_tab_header.findViewById(R.id.police_header_radio);
        police_header_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mMap.clear();
                switch (checkedId) {
                    case R.id.rb_jl:
                        //发送请求数据
                        checkBox1.setVisibility(View.VISIBLE);
                        checkBox2.setVisibility(View.VISIBLE);
                        policeNearBy(FLAG_NET_JLFB);
                        break;
                    case R.id.rb_fkd:
                        checkBox1.setVisibility(View.INVISIBLE);
                        checkBox2.setVisibility(View.INVISIBLE);
                        policeNearBy(FLAG_NET_FKDFB);
                        break;
                    case R.id.rb_guiji:
                        checkBox1.setVisibility(View.INVISIBLE);
                        checkBox2.setVisibility(View.INVISIBLE);
                        policeNearBy(FLAG_NET_GJCX);
                        break;
                }
            }
        });

        btn_showperson = (Button) findViewById(R.id.btn_showperson);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_showperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlcDstrbtMpAty.this, PersonActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseDataUtils.isAdminPcs() && StringUtils.isEmpty(adminPhone)) {
                    toast("请选择人员");
                } else {
                    pvTime.show();
                }
            }
        });

        if (client == null)
            client = new LBSTraceClient(AppContext.instance());
        btn_time.setText(DateUtil.date2String(new Date(), DateUtil.DATE_SMALL_STR));
        initTimePickerView();


        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mMap.hideInfoWindow();
                checkbox1 = b;
                if (b) {
                    for (Overlay o : policesOverlay
                            ) {
                        o.setVisible(true);
                    }
                } else {
                    for (Overlay o : policesOverlay
                            ) {
                        o.setVisible(false);
                    }
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mMap.hideInfoWindow();
                checkbox2 = b;
                if (b) {
                    if (getDeviceListPostion().size() != 0) {
                        if (handler == null) {
                            initHandler();
                        }
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    for (int i = 0; i < getOverlays().size(); i++) {
                        Marker o = getOverlays().get(i);
                        o.remove();
                    }
                }
            }
        });

        devicesMap=AppContext.getDevicesMap();
        unitModelMap= getUnitModelMap();
        taskMap=AppContext.getTaskMap();

        //在此获取任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDevices();
                getUnit();
                getTasks();
            }
        }).start();

    }

    public void getUnit() {
        HttpClient httpCient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://219.134.134.156:8088/xsmws-web/api/v1.0/yajie");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "getUnitList");
        jsonObject.put("apikey", "AEGCIOKMteD8Iu6G");
        StringEntity postingString = null;// json传递
        try {
            postingString = new StringEntity(jsonObject.toJSONString());
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpCient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String content = EntityUtils.toString(response.getEntity());
                UnitModel unitModel= gson.fromJson(content, UnitModel.class);
                if (unitModel.getError().equals("0")){
                    SharedPreferencesUtil.putString(this,"getUnitList",content);
                    Map<String,UnitModel.DataBean>map=AppContext.getUnitModelMap();
                    for (UnitModel.DataBean db:unitModel.getData()
                            ) {
                        map.put(""+db.getId(),db);
                    }
                }
            }else {
                String content= SharedPreferencesUtil.getString(this,"getUnitList","");
                if (!StringUtils.isEmpty(content)){
                    UnitModel d = gson.fromJson(content, UnitModel.class);
                    Map<String,UnitModel.DataBean>map=AppContext.getUnitModelMap();
                    for (UnitModel.DataBean db:d.getData()
                            ) {
                        map.put(""+db.getId(),db);
                    }
                }else {
                    Toast.makeText(instace, "任务获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的 任务，，初始值为null*/
    public void getTasks() {
        HttpClient httpCient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://219.134.134.156:8088/xsmws-web/api/v1.0/yajie");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "getTaskList");
        jsonObject.put("apikey", "AEGCIOKMteD8Iu6G");
        StringEntity postingString = null;// json传递
        try {
            postingString = new StringEntity(jsonObject.toJSONString());
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpCient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String content = EntityUtils.toString(response.getEntity());
                TaskListModel taskListModel=gson.fromJson(content, TaskListModel.class);
                if (taskListModel.getError().equals("0")){
                    Map<String,TaskListModel.DataBean>map=AppContext.getTaskMap();
                    for (TaskListModel.DataBean db:taskListModel.getData()
                            ) {
                        map.put(""+db.getId(),db);
                    }
                    SharedPreferencesUtil.putString(this,"getTaskList",content);
                }
            }else {
                String content= SharedPreferencesUtil.getString(this,"getTaskList","");
                if (!StringUtils.isEmpty(content)){
                    TaskListModel d = gson.fromJson(content, TaskListModel.class);
                    Map<String,TaskListModel.DataBean>map=AppContext.getTaskMap();
                    for (TaskListModel.DataBean db:d.getData()
                            ) {
                        map.put(""+db.getId(),db);
                    }
                }else {
                    Toast.makeText(instace, "任务获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void getDevices() {
        HttpClient httpCient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://219.134.134.156:8088/xsmws-web/api/v1.0/yajie");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "getDeviceList");
        jsonObject.put("apikey", "AEGCIOKMteD8Iu6G");
        StringEntity postingString = null;// json传递
        try {
            postingString = new StringEntity(jsonObject.toJSONString());
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpCient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String content = EntityUtils.toString(response.getEntity());
                DevicesModel d = gson.fromJson(content, DevicesModel.class);
                if (d.getError().equals("0")) {
                    SharedPreferencesUtil.putString(this,"getDeviceList",content);
                    Map<String,DevicesModel.DataBean>map=AppContext.getDevicesMap();
                    for (DevicesModel.DataBean db:d.getData()
                            ) {
                        map.put(""+db.getId(),db);
                    }

                }
            }else {
                String content= SharedPreferencesUtil.getString(this,"getDeviceList","");
                if (!StringUtils.isEmpty(content)){
                    DevicesModel d = gson.fromJson(content, DevicesModel.class);
                    Map<String,DevicesModel.DataBean>map=AppContext.getDevicesMap();
                    for (DevicesModel.DataBean db:d.getData()
                            ) {
                        map.put(""+db.getId(),db);
                    }
                }else {
                    Toast.makeText(instace, "设备获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TimePickerView pvTime;

    private void initTimePickerView() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTitle("请选择日期");
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {

                Calendar c1 = Calendar.getInstance();
                c1.setTime(date);
                int year = c1.get(Calendar.YEAR);
                int month = c1.get(Calendar.MONTH) + 1;
                int day = c1.get(Calendar.DAY_OF_MONTH);

                c1.set(Calendar.HOUR_OF_DAY, 0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);
                int hour = c1.get(Calendar.HOUR_OF_DAY);
                int minute = c1.get(Calendar.MINUTE);
                int second = c1.get(Calendar.SECOND);
                String st = year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";
                startTime = Integer.parseInt(DateUtil.getTimeToStamp(st));
                Calendar c2 = Calendar.getInstance();
                c2.setTime(date);
                c2.set(Calendar.HOUR_OF_DAY, 23);
                c2.set(Calendar.MINUTE, 59);
                c2.set(Calendar.SECOND, 59);
                int hour2 = c2.get(Calendar.HOUR_OF_DAY);
                int minute2 = c2.get(Calendar.MINUTE);
                int second2 = c2.get(Calendar.SECOND);

                String et = year + "年" + month + "月" + day + "日" + hour2 + "时" + minute2 + "分" + second2 + "秒";
                endTime = Integer.parseInt(DateUtil.getTimeToStamp(et));
                btn_time.setText(year + "-" + month + "-" + day);
                queryHistoryTrack(StringUtils.isEmpty(adminPhone) ? null : adminPhone);
            }

        });

    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_dstrbt_map);
    }

    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        super.onReceiveLoc(location, isSuccess, errMsg);
        police_header_radio.check(R.id.rb_jl);
    }

    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        if (BaseDataUtils.isAdminPcs()) {
            requestJson.put("PCSBM", "");
        } else {
            requestJson.put("PCSBM", userInfo.getOrgancode());
        }
        requestJson.put("SJ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return requestJson.toJSONString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 100:
                adminPhone = data.getStringExtra("phone");
                //查询轨迹
                queryHistoryTrack(adminPhone);
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取派出所下岗亭列表
     *
     * @return
     */
    private String getPlcSttnList() {
        JSONObject requestJson = new JSONObject();
        if (BaseDataUtils.isAdminCommit()) {
            requestJson.put("", "");
        } else {
            requestJson.put("POLICESTATION", userInfo.getPcs());
        }
        return requestJson.toJSONString();
    }

    private static final int FLAG_NET_JLFB = 0;
    private static final int FLAG_NET_FKDFB = 1;
    private static final int FLAG_NET_GJCX = 2;
    private Map<Integer, List<PoliceBoxModle>> fkdMaps = new HashMap<Integer, List<PoliceBoxModle>>();


    private void policeNearBy(int netFlag) {
        btn_showperson.setVisibility(View.GONE);
        btn_time.setVisibility(View.GONE);
        switch (netFlag) {
            case FLAG_NET_JLFB:
                showLoadding();
                for (Overlay o : policesOverlay
                        ) {
                    o.setVisible(false);
                    o.remove();
                }
                policesOverlay.clear();
                ApiResource.getGJData(getRequest(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        String result = new String(bytes);
                        List<PloceMapModle> polices = JSON.parseArray(result, PloceMapModle.class);
                        if (polices != null && polices.size() > 0) {
                            maps.clear();
                            mMap.clear();
                            for (int j = 0; j < polices.size(); j++) {
                                if (polices.get(j).getACCOUNTTYPE().equals("民警")) {
                                    if (maps.get(NORMAL) == null)
                                        maps.put(NORMAL, new ArrayList<PloceMapModle>());
                                    maps.get(NORMAL).add(polices.get(j));
                                } else {
                                    if (maps.get(ABNORMAL) == null)
                                        maps.put(ABNORMAL, new ArrayList<PloceMapModle>());
                                    maps.get(ABNORMAL).add(polices.get(j));
                                }
                            }
                            setPlcBxMarker(maps.get(NORMAL), R.drawable.map_jlfb);
                            setPlcBxMarker(maps.get(ABNORMAL), R.drawable.map_jlfb_no);
                            if (handler != null) {
                                handler.sendEmptyMessage(1);
                            }
                            stopLoading();
                        } else {
                            stopLoading();
                            if (handler != null) {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        stopLoading();
                        if (throwable.getMessage() != null && throwable.getMessage().contains("author")) {
                            toast("无权限,请重新登录");
                        } else {
                            toast("数据获取失败");
                        }
                    }
                });
                break;
            case FLAG_NET_FKDFB:
                showLoadding();
                ApiResource.getGtList(getPlcSttnList(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        stopLoading();
                        String result = new String(bytes);
                        try {
                            List<PoliceBoxModle> list = JSON.parseArray(result, PoliceBoxModle.class);
                            if (list != null) {
                                for (PoliceBoxModle p : list) {

                                    if (p.getLAT() <= 0 || p.getLNGT() <= 0) {
                                        continue; //如果没有经纬度信息则过滤
                                    }

                                    // if (p.getZSZT() == NORMAL || p.getZSZT() == NORMAL2) {//值守状态的岗亭信息
                                    if (Integer.valueOf(p.getZSZT()) > 0) {//值守状态的岗亭信息
                                        if (fkdMaps.get(NORMAL) == null)
                                            fkdMaps.put(NORMAL, new ArrayList<PoliceBoxModle>());
                                        fkdMaps.get(NORMAL).add(p);
                                    } else {
                                        if (fkdMaps.get(ABNORMAL) == null) //不在值守状态的岗亭
                                            fkdMaps.put(ABNORMAL, new ArrayList<PoliceBoxModle>());
                                        fkdMaps.get(ABNORMAL).add(p);
                                    }
                                }
                                setPlcBxMarker2(fkdMaps.get(NORMAL), R.drawable.plc_bx_online);
                                setPlcBxMarker2(fkdMaps.get(ABNORMAL), R.drawable.plc_bx_offline);
                                stopLoading();
                            }
                        } catch (JSONException e) {
                            toast("获取数据失败");
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        stopLoading();
                        toast("获取数据失败");
                    }
                });
                break;
            case FLAG_NET_GJCX:
                btn_time.setVisibility(View.VISIBLE);
                if (BaseDataUtils.isAdminPcs()) {
                    btn_showperson.setVisibility(View.VISIBLE);
                } else {
                    showLoadding();
                    queryHistoryTrack(null);
                }

                break;
        }
    }

    private int startTime = 0;
    private int endTime = 0;

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack(String entityName) {
        // 轨迹服务ID
        long serviceId = BaiduMapUtil.SERVICE_ID;
        // entity标识
        if (entityName == null)
            entityName = AppContext.instance().getLoginInfo().getUsername();
        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = 1;
        // 开始时间
        if (startTime == 0) {
            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        }
        if (endTime == 0) {
            endTime = (int) (System.currentTimeMillis() / 1000);
        }
        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;

        String processOption = "need_denoise=1,need_vacuate=1,need_mapmatch=1";

        client.queryHistoryTrack(serviceId, entityName, simpleReturn,
                isProcessed, processOption,
                startTime, endTime,
                pageSize,
                pageIndex,
                trackListener);
    }

    /**
     * 设置岗亭标注
     *
     * @param markers
     * @param drawableId
     */
    public void setPlcBxMarker2(List<PoliceBoxModle> markers, int drawableId) {

        if (markers == null)
            return;
        for (PoliceBoxModle pb : markers) {
            if (pb.getLAT() <= 0 || pb.getLNGT() <= 0)
                return;

            LatLng ll = new LatLng(pb.getLAT(), pb.getLNGT());

            if (checkbox1) {
//            LatLng latLng = new LatLng(ll.latitude, ll.longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(drawableId);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(ll)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                Overlay overlay = mMap.addOverlay(option);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", pb);
                overlay.setExtraInfo(bundle);
            }
        }
    }

    /**
     * 设置巡逻点标注
     *
     * @param markers
     * @param drawableId
     */
    public void setPlcBxMarker(List<PloceMapModle> markers, int drawableId) {
        if (markers == null || markers.size() == 0 || checkbox1 == false)
            return;
        for (PloceMapModle x : markers) {
            if (x.getWD() != null && mMapView != null) {
                LatLng ll = new LatLng(x.getWD(), x.getJD());
                LatLng latLng = new LatLng(ll.latitude, ll.longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(drawableId);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
                //在地图上添加Marker，并显示
                Overlay overlay = mMap.addOverlay(option);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", x);
                overlay.setExtraInfo(bundle);
                policesOverlay.add(overlay);
            }
        }
    }

    @Override
    public boolean needKeepLoc() {
        return super.needKeepLoc();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        //解除注册
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public synchronized void ReceviceMessage(final DevicesPositionModel eventBusStickyMessage) {
        boolean bl = false;
        for (int k = 0; k < getDeviceListPostion().size(); k++) {
            DevicesPositionModel devicesPositionModel = getDeviceListPostion().get(k);
            if (devicesPositionModel.getDeviceId().equals(eventBusStickyMessage.getDeviceId())) {
                if (Double.valueOf(eventBusStickyMessage.getLatitude()) < 10d) {
                    DevicesModel.DataBean d=devicesMap.get(eventBusStickyMessage.getDeviceId());
                    if (d!=null&&d.getType()!=2)
                        return;
                }
                devicesPositionModel.setEscape(eventBusStickyMessage.getEscape());
                devicesPositionModel.setCommand(eventBusStickyMessage.getCommand());
                devicesPositionModel.setLatitude(eventBusStickyMessage.getLatitude());
                devicesPositionModel.setLongitude(eventBusStickyMessage.getLongitude());
                devicesPositionModel.setSingledrop(eventBusStickyMessage.getSingledrop());
                devicesPositionModel.setLowpower(eventBusStickyMessage.getLowpower());
                devicesPositionModel.setTapebreak(eventBusStickyMessage.getTapebreak());
                devicesPositionModel.setTime(eventBusStickyMessage.getTime());
                bl = true;
            }
        }
        if (!bl) {
            getDeviceListPostion().add(eventBusStickyMessage);
        }
        for (Marker o : getOverlays()
                ) {
            o.remove();
        }
        getOverlays().clear();
        if (handler == null) {
            initHandler();
        }
        handler.sendEmptyMessage(1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public synchronized void ReceviceTask(TaskModel taskModel) {
        if (handler != null) {
            for (Marker o : getOverlays()
                    ) {
                o.remove();
            }
            handler.sendEmptyMessage(1);
        }
    }

    public synchronized void initHandler() {
        handler = new Handler() {
            @Override
            public synchronized void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    mMap.hideInfoWindow();
                    List<DevicesPositionModel>devicesPositionModels=AppContext.getDeviceListPostion();
                    for (int n = 0; n < devicesPositionModels.size(); n++) {
                        DevicesPositionModel event = devicesPositionModels.get(n);
                        if (devicesMap.size() != 0) {
                            DevicesModel.DataBean db = devicesMap.get(event.getDeviceId());;
                            if (db!=null) {
                                int type=db.getType();
                                TaskListModel.DataBean dataBean = null;
                                Iterator<Map.Entry<String,TaskListModel.DataBean>>it=taskMap.entrySet().iterator();
                                while (it.hasNext()){
                                    Map.Entry<String, TaskListModel.DataBean> entry = it.next();
                                    TaskListModel.DataBean dataBean1=entry.getValue();
                                    for (Integer t : dataBean1.getDevices()
                                            ) {
                                        if (event.getDeviceId().equals(String.valueOf(t))) {
                                            dataBean =dataBean1;
                                            break;
                                        }
                                    }
                                }
                                //勾选了
                                if (checkbox2) {
                                    double[]d1=gaoDeToBaidu(Double.valueOf(event.getLongitude()),Double.valueOf(event.getLatitude()));
                                    double lati = d1[1];
                                    double lon = d1[0];
                                    LatLng ll = new LatLng(lati, lon);
                                    LatLng latLng = new LatLng(ll.latitude, ll.longitude);
                                    //构建Marker图标
                                    BitmapDescriptor bitmap = null;
                                    Marker overlay = null;
                                    if ((event.getEscape().equals("1")||event.getTapebreak().equals("1")||event.getLowpower().equals("1")||
                                    event.getSingledrop().equals("1"))&&type==2&&dataBean!=null&&!dataBean.getState().equals("4")) {
                                        bitmap = BitmapDescriptorFactory
                                                .fromResource(R.drawable.suspect);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        OverlayOptions option = markerOptions.position(latLng).icon(bitmap);
                                        //在地图上添加Marker，并显示
                                        if (dataBean != null && !StringUtil.isEmpty(dataBean.getState())) {
                                            overlay = (Marker) mMap.addOverlay(option);
                                        }
                                    } else if (event.getEscape().equals("0")&&type==1&&dataBean!=null&&!dataBean.getState().equals("4")){
                                        bitmap = BitmapDescriptorFactory
                                                .fromResource(R.drawable.polices_icon);
                                        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
                                        if (dataBean != null && !StringUtil.isEmpty(dataBean.getState()) && !dataBean.getState().equals("4")) {
                                            overlay = (Marker) mMap.addOverlay(option);
                                        }
                                    }

                                    if (overlay != null) {
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("data", dataBean);
                                        overlay.setExtraInfo(bundle);
                                        getOverlays().add(overlay);//重新添加 脚镣+终端信息
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }


    public double[] gaoDeToBaidu(double gd_lon, double gd_lat)
    {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);

        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }
}

