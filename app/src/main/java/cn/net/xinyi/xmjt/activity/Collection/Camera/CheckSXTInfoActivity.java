package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter.ShowsSXTAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.model.SXTInfoModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;

/**
 * Created by hao.zhou on 2015/9/18.
 * 核查信息
 */
public class CheckSXTInfoActivity extends BaseActivity2 implements View.OnClickListener {
    private ListView lv_upload_sxt;
    private ShowsSXTAdapter mAdapter;
    private TextView empty_data;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapDataModle mapdata;
    private RelativeLayout rl_top;
    private List<SXTInfoModle> sXTInfo=new ArrayList<SXTInfoModle>();
    private LinearLayout makerLayout_red;
    public static CheckSXTInfoActivity intence;
    private ProgressDialog mProgressDialog = null;
    private MarkerOptions oo;
    private InfoWindow mInfoWindow;
    private List<String> sq = new ArrayList<String>();
    private TextView tv_choose_jd ,tv_choose_sq;
    private String jd_bm=null;
    private MenuItem action_list,action_map;
    private BDLocation location;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_camera_info);
        intence=this;
        /** *百度地图请求当前位置信息  */
        new GetLocation(this, mHandle).startLocation();
        initView();//控件初始化
        //如果资料中 街道、社区  有值 直接请求数据
        if (!tv_choose_jd.getText().toString().trim().isEmpty()&&
                !tv_choose_sq.getText().toString().trim().isEmpty()){
            DSHSXTList();
        }
    }


    @Override
    public String getAtyTitle() {
        return getResources().getString(R.string.SXTinfo_check);
    }

    private void initView() {

        lv_upload_sxt = (ListView) findViewById(R.id.lv_upload_camera_room);//listview
        empty_data = (TextView)findViewById(R.id.empty_data_sxt);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        //选择街道
        tv_choose_jd=(TextView)findViewById(R.id.tv_choose_jd);
        //选择社区
        tv_choose_sq=(TextView)findViewById(R.id.tv_choose_sq);
        //设置街道默认值
        if (!AppContext.instance.getLoginInfo().getSsjd().isEmpty()){
            tv_choose_jd.setText(AppContext.instance.getLoginInfo().getSsjd()); }
        //设置社区默认值
        if (!AppContext.instance.getLoginInfo().getSssq().isEmpty()){
            tv_choose_sq.setText(AppContext.instance.getLoginInfo().getSssq());}

        /***根据街道信息转换成街道编码*/
        jd_bm=zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.ZZJG_JD,tv_choose_jd.getText().toString().trim());
        setListener();//事件监听
        initMapData();//加载地图数据
    }

    private void initMapData(){
        makerLayout_red = (LinearLayout) LayoutInflater.from(CheckSXTInfoActivity.this).
                inflate(R.layout.map_marker_red_layout, null);
        //地图相关设置
        mapdata = new MapDataModle();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    //待审核摄像头
    private void DSHSXTList() {
        mProgressDialog = new ProgressDialog(this);
        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);

        JSONObject jo = new JSONObject();
        jo.put("pcs",AppContext.instance.getLoginInfo().getOrgancode());
        jo.put("sssq",tv_choose_sq.getText().toString().trim());
        String json=jo.toJSONString();

        ApiResource.HCSXTInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        String result = new String(bytes);
                        sXTInfo = JSON.parseObject(result, new TypeReference<List<SXTInfoModle>>() {
                        });
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


    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在获取本派出所需要核查的数据");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;
                case 1:
                    if (mProgressDialog != null && mProgressDialog.isShowing()){
                        mProgressDialog.cancel();
                    }
                    if (sXTInfo.size()==0){
                        dataNull();
                    }else {
                        //初始化待审核监控室列表；
                        initListViewData();
                    }
                    break;
                case 2:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.cancel();
                    BaseUtil.showDialog("获取数据失败", CheckSXTInfoActivity.this);
                    dataNull();
                    break;
                case 3:
                    if (mProgressDialog != null && mProgressDialog.isShowing()){
                        mProgressDialog.cancel();
                    }
                    break;

                case -100:
                    location = (BDLocation) msg.obj;
                    mapdata.setLatitude("" + location.getLatitude());
                    mapdata.setLongitude("" + location.getLongitude());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GeneralUtils.WatchSXTInfo) {
            DSHSXTList();//加载待审核摄像头数据
        }
    }

    private void setListener() {
        tv_choose_jd.setOnClickListener(this);
        tv_choose_sq.setOnClickListener(this);
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


    //设置地图模式下地图数据
    private void setMapOverlay() {
        //摄像头图标
        BitmapDescriptor bdSxt = BitmapDescriptorFactory.fromView(makerLayout_red);
        if(sXTInfo!=null) {
            //添加摄像头标注
            for (int i = 0; i < sXTInfo.size(); i++) {
                LatLng ll = new LatLng(Double.valueOf(sXTInfo.get(i).getWD()), Double.valueOf(sXTInfo.get(i).getJD()));
                oo = new MarkerOptions().position(ll).icon(bdSxt).zIndex(sXTInfo.size()).title("SXT");
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("SXT", sXTInfo.get(i));
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

                if (marker.getTitle().equals("SXT")) {
                    Bundle mBundle = marker.getExtraInfo();
                    final SXTInfoModle sxt = (SXTInfoModle) mBundle.get("SXT");
                    popText.setText("所属监控室：" + sxt.getJKSMC());
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showSxtInfo(sxt);
                        }
                    });
                }
                return true;
            }
        });

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

    //查看摄像头
    public void showSxtInfo(SXTInfoModle sxt) {
        Intent intent = new Intent(CheckSXTInfoActivity.this, WatchSXTInfoActivity.class);
        intent.putExtra(GeneralUtils.SXTInfo, sxt);
        intent.setFlags(GeneralUtils.CheckSXTInfo_flag);
        startActivity(intent);
    }

    //加载数据
    private void initListViewData() {
        lv_upload_sxt.setVisibility(View.VISIBLE);
        empty_data.setVisibility(View.GONE);

        mAdapter = new ShowsSXTAdapter(lv_upload_sxt,sXTInfo,R.layout.upload_camera_adapter,this);
        lv_upload_sxt.setAdapter(mAdapter);

        //点击item到  查看信息页面  需要传递 摄像头的信息
        lv_upload_sxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CheckSXTInfoActivity.this, WatchSXTInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(GeneralUtils.SXTInfo, sXTInfo.get(i));
                intent.putExtras(mBundle);
                intent.setFlags(GeneralUtils.CheckSXTInfo_flag);
                startActivityForResult(intent, 201);
            }
        });
    }

    //当没有数据，布局的显示
    private void dataNull() {
        empty_data.setText("目前没有待审核的信息");
        empty_data.setVisibility(View.VISIBLE);
        lv_upload_sxt.setVisibility(View.GONE);
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
            //选择所属街道
            case R.id.tv_choose_jd:
                //需要对 已有的网格 社区 集合数据清空
                sq.clear();
                tv_choose_sq.setText("");
                tv_choose_sq.setHint("请选择社区");

                final Map<String,String> jdMaps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_JD);
                new AlertDialog.Builder(this).setItems(jdMaps.values().toArray(new String[jdMaps.values().size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_choose_jd.setText(jdMaps.values().toArray(new String[jdMaps.values().size()])[which]);
                                jd_bm=zdUtils.getMapKey(jdMaps,tv_choose_jd.getText().toString());
                            }
                        }).create().show();
                break;

            //选择所属社区
            case R.id.tv_choose_sq:
                //需要对 已有的 社区 集合数据清空 避免重复添加
                sq.clear();
                if (tv_choose_jd.getText().toString().trim().isEmpty()){
                    BaseUtil.showDialog("所属街道不能为空",CheckSXTInfoActivity.this);
                }else if (jd_bm!=null&&!tv_choose_jd.getText().toString().trim().isEmpty()){

                    final Map<String,String> maps=zdUtils.getZdlbToZdz(GeneralUtils.ZZJG_SQ);
                    new AlertDialog.Builder(this).setItems(maps.values().toArray(new String[maps.values().size()]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_choose_sq.setText(maps.values().toArray(new String[maps.values().size()])[which]);
                                }
                            }).create().show();
                }

                    break;
        }
    }
}