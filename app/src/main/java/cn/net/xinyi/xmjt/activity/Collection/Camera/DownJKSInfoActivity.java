package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter.ShowsJKSAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;

/**
 * Created by hao.zhou on 2015/9/18.
 * 已上传监控室信息列表
 */
public class DownJKSInfoActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_upload_jks;
    private ShowsJKSAdapter mAdapter;
    private TextView empty_data;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapDataModle mapdata;
    private RelativeLayout rl_top;
    private LinearLayout makerLayout;
    private List<JKSInfoModle> jKSInfo=new ArrayList<JKSInfoModle>();
    public static DownJKSInfoActivity intence;
    private ProgressDialog mProgressDialog = null;
    private MarkerOptions oo;
    private InfoWindow mInfoWindow;
    private TextView tv_choose_start_date;
    private TextView tv_choose_end_date;
    private int now_year, now_month, now_day;
    private MenuItem action_list,action_map;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_camera_room_info);
        intence=this;

        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getResources().getString(R.string.JKSinfo_upload_Manage));
        getActionBar().setHomeButtonEnabled(true);

        /** *百度地图请求当前位置信息  */
        new GetLocation(this, mHandle).startLocation();

        initView();//控件初始化
        UploadJKSList();//联网获取已经上传的监控室列表
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        lv_upload_jks = (ListView) findViewById(R.id.lv_upload_camera_room); //listview
        empty_data = (TextView)findViewById(R.id.empty_data);//textview 数据为空显示
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);//地图模式隐藏当前布局
        //开始时间
        tv_choose_start_date = (TextView) findViewById(R.id.tv_choose_start_date);
        //结束时间
        tv_choose_end_date = (TextView) findViewById(R.id.tv_choose_end_date);
        //初始化数据，设置默认值
        setData();
        setListener();//点击事件监听
        initMapData();//加载地图数据
    }
    private void setData() {
        /***获得当前时间的年月日
         *
         * 获取的数值比实际小1  用的时候需要+1   */
        now_year = c_start.get(Calendar.YEAR);
        now_month =c_start.get(Calendar.MONTH);
        now_day = c_start.get(Calendar.DAY_OF_MONTH);

        //和当前的日期进行比较，如果小于25日，显示上个月26-现在时间
        if (now_day<=25){
            /***如果实际的月份 是1月  需要设置成为前一年的12月*/
            if (now_month==0){
                tv_choose_start_date.setText( new StringBuilder().append(now_year-1).append("-").
                        append(12)
                        .append("-").append(26));
                tv_choose_end_date.setText( new StringBuilder().append(now_year).append("-").
                        append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                        .append("-").append((now_day < 10) ? "0" + now_day : now_day));

            }else {
                tv_choose_start_date.setText( new StringBuilder().append(now_year).append("-").
                        append(now_month  < 10 ? "0" + (now_month ) : (now_month ))
                        .append("-").append(26));
                tv_choose_end_date.setText( new StringBuilder().append(now_year).append("-").
                        append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                        .append("-").append((now_day < 10) ? "0" + now_day : now_day));

            }
        }else{
            //和当前的日期进行比较，如果大于25日，显示本月26-现在时间
            //正则表达式  如果月份不足10，补0；
            tv_choose_start_date.setText(new StringBuilder().append(now_year).append("-").
                    append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                    .append("-").append(26));
            tv_choose_end_date.setText( new StringBuilder().append(now_year).append("-").
                    append((now_month + 1) < 10 ? "0" + (now_month + 1) : (now_month + 1))
                    .append("-").append((now_day < 10) ? "0" + now_day : now_day));
        }
    }

    //加载地图数据
    private void initMapData(){
        makerLayout = (LinearLayout) LayoutInflater.from(DownJKSInfoActivity.this).
                inflate(R.layout.map_marker_layout, null);
        //地图相关设置
        mapdata = new MapDataModle();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }


    //监控室 上传信息列表
    private void UploadJKSList() {
        final String kssj = tv_choose_start_date.getText().toString().trim();
        final String jssj = tv_choose_end_date.getText().toString().trim();

        JSONObject jo = new JSONObject();
        jo.put("kssj",kssj);
        jo.put("jssj",jssj);
        jo.put("username", AppContext.instance.getLoginInfo().getUsername());
        String json = jo.toJSONString();

        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);
        ApiResource.postJKSInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        String result = new String(bytes);
                        jKSInfo = JSON.parseObject(result, new TypeReference<List<JKSInfoModle>>() {
                        });
                        //根据业务类型编码匹配 业务类型
                        if (jKSInfo != null && jKSInfo.size() != 0) {
                            for (int j = 0; j < jKSInfo.size(); j++) {
                                jKSInfo.get(j).setJKSYWFL(zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.XXCJ_JKSYWFL,jKSInfo.get(j).getJKSYWFLBM()));

                            }
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        mHandle.sendMessage(msg);
                    } else {
                        onFailure(i, headers, bytes, null);
                    }

                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 2;
                mHandle.sendMessage(msg);
            }
        });
    }


    //事件监听
    private void setListener() {
        tv_choose_end_date.setOnClickListener(this);
        tv_choose_start_date.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        action_list = menu.findItem(R.id.action_list);
        action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.action_list:
                /**点击列表  切换为地图模式
                 * 1.图标和布局的切换
                 * **/
                action_map.setVisible(true);
                action_list.setVisible(false);
                rl_top.setVisibility(View.GONE);
                mMapView.setVisibility(View.VISIBLE);
                /**
                 * *地图上标识当前所处的位置
                 * 如果没有获取到坐标  默认定位到龙岗中心城*/
                new GetLocation(this).mapLocation(location, mBaiduMap);
                setMapOverlay();
                break;

            case R.id.action_map://点击地图  显示列表
                action_map.setVisible(false);
                action_list.setVisible(true);
                rl_top.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.GONE);

                break;
            default:
                break;
        }
        return true;
    }

    private BDLocation location;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在获取您已上传的监控室的数据");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing()){
                        mProgressDialog.cancel();
                    }
                    if (jKSInfo.size()==0){
                        dataNull();
                    }else {
                        initListViewData();//初始化待审核监控室列表；
                    }
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("获取数据失败", DownJKSInfoActivity.this);
                    dataNull();
                    break;

                case -100:
                    location = (BDLocation) msg.obj;
                    mapdata.setLatitude(""+location.getLatitude());
                    mapdata.setLongitude(""+location.getLongitude());
                    break;

                default:
                    break;
            }
        }
    };

    //设置地图模式下地图数据
    private void setMapOverlay() {
        //监控室图标
        BitmapDescriptor bdJks = BitmapDescriptorFactory.fromView(makerLayout);
        if(jKSInfo!=null) {
            //添加监控室标注
            for (int i = 0; i < jKSInfo.size(); i++) {
                LatLng ll = new LatLng(Double.valueOf(jKSInfo.get(i).getWD()), Double.valueOf(jKSInfo.get(i).getJD()));
                oo = new MarkerOptions().position(ll).icon(bdJks).zIndex(jKSInfo.size()).title("JKS");
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("JKS", jKSInfo.get(i));
                oo.extraInfo(mBundle);
                mBaiduMap.addOverlay(oo);
            }
        }
        //添加标注点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                View viewCatch = LayoutInflater.from(getApplicationContext()).inflate(R.layout.caiji_pop_layout, null);
                final TextView popText = ((TextView) viewCatch.findViewById(R.id.location_tips));
                //获取地址保存按钮
                final Button mButton = ((Button) viewCatch.findViewById(R.id.location_save));

                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(viewCatch, ll, -60);
                mBaiduMap.showInfoWindow(mInfoWindow);

                if (marker.getTitle().equals("JKS")) {
                    Bundle mBundle = marker.getExtraInfo();
                    final JKSInfoModle jks = (JKSInfoModle) mBundle.get("JKS");
                    popText.setText("监控室名称：" + jks.getJKSMC());
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showJksInfo(jks);
                        }
                    });
                }
                return true;
            }
        });
        //点击其他空白部分  消失
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mInfoWindow != null) {
                    mBaiduMap.hideInfoWindow();
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }


    //查看监控室
    public void showJksInfo(JKSInfoModle jks) {
        Intent intent = new Intent(DownJKSInfoActivity.this, WatchJKSInfoActivity.class);
        intent.putExtra(GeneralUtils.JKSInfo, jks);
        intent.setFlags(GeneralUtils.DownJKSInfoActivity);
        startActivity(intent);
    }

    //加载数据
    private void initListViewData() {
        //当数据不为空 列表 出现，text隐藏
        lv_upload_jks.setVisibility(View.VISIBLE);
        empty_data.setVisibility(View.GONE);
        //设置adapter
        mAdapter = new ShowsJKSAdapter(lv_upload_jks,jKSInfo,R.layout.upload_camera_room_adapter,this);
        lv_upload_jks.setAdapter(mAdapter);
        //点击item到查看信息页面
        lv_upload_jks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DownJKSInfoActivity.this, WatchJKSInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(GeneralUtils.JKSInfo, jKSInfo.get(i));
                intent.putExtras(mBundle);
                intent.setFlags(GeneralUtils.DownJKSInfoActivity);
                startActivityForResult(intent, 200);
            }
        });
    }


    //当没有数据，布局的显示
    private void dataNull() {
        lv_upload_jks.setVisibility(View.GONE);
        empty_data.setVisibility(View.VISIBLE);
        empty_data.setText("目前没有上传的信息");
        return;
    }

    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 在2级菜单用
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //点击时间获得开始时间
            case  R.id.tv_choose_start_date:
                String start_date=tv_choose_start_date.getText().toString().trim();
                new DatePickerDialog(
                        DownJKSInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_start.set(year, monthOfYear, dayOfMonth);

                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_start);
                        //判断起始时间不能大于结束时间
                        try {
                            //获得结束时间转为秒
                            long  choose_end_date = (format.parse(tv_choose_end_date.getText().toString().trim()).getTime())/1000;
                            //获得输入的开始时间转为秒
                            long  choose_start_date = (format.parse(forma.toString()).getTime())/1000;
                            //开始日期为2015-08-26
                            long  init_date=format.parse("2015-10-26").getTime()/1000;
                            //开始时间不能小于2015-08-26
                            if (choose_start_date<init_date){
                                Toast.makeText(DownJKSInfoActivity.this, "开始日期不能小于2015-10-26",Toast.LENGTH_SHORT).show();
                                //结束时间和开始时间进行比较
                            } else if (choose_end_date<choose_start_date){
                                Toast.makeText(DownJKSInfoActivity.this, "开始日期不能大于结束日期",Toast.LENGTH_SHORT).show();
                            }else{
                                tv_choose_start_date.setText(forma);
                                UploadJKSList();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },Integer.valueOf(start_date.substring(0, 4).toString()), (Integer.valueOf(start_date.substring(5, 7).toString()) - 1),
                        Integer.valueOf(start_date.substring(8, 10).toString())).show();
                break;


            //点击时间获得结束时间
            case  R.id.tv_choose_end_date:
                new DatePickerDialog(
                        DownJKSInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_end.set(year, monthOfYear, dayOfMonth);
                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_end);
                        //判断起始时间不能大于结束时间
                        try {
                            //获得开始时间转为秒
                            long  choose_start_date = (format.parse(tv_choose_start_date.getText().toString().trim()).getTime())/1000;
                            //获得结束时间 转为秒
                            long  choose_end_date = (format.parse(forma.toString()).getTime())/1000;
                            //结束时间应该大于开始时间
                            if (choose_end_date<choose_start_date){
                                Toast.makeText(DownJKSInfoActivity.this, "开始日期不能大于结束日期",Toast.LENGTH_SHORT).show();
                            }else{
                                tv_choose_end_date.setText(forma);
                                UploadJKSList();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, c_end.get(Calendar.YEAR), c_end .get(Calendar.MONTH), c_end
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }
}