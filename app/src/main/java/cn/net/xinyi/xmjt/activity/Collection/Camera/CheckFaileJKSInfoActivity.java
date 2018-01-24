package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter.ShowsJKSAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;

/**
 * Created by hao.zhou on 2015/9/18.
 *
 */
public class CheckFaileJKSInfoActivity extends BaseActivity2 {
    private ListView lv_upload_jks;
    private ShowsJKSAdapter mAdapter;
    private TextView empty_data;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapDataModle mapdata;
    public BDLocation curLocation;
    private RelativeLayout rl_top;
    private LinearLayout makerLayout;
    private List<JKSInfoModle> jKSInfo=new ArrayList<JKSInfoModle>();
    public static CheckFaileJKSInfoActivity intence;
    private ProgressDialog mProgressDialog = null;
    private MarkerOptions oo;
    private InfoWindow mInfoWindow;
    private MenuItem action_list,action_map;
    private BDLocation location;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_faile);
        intence=this;


        /** *百度地图请求当前位置信息  */
        new GetLocation(this, mHandle).startLocation();
        initView();//控件初始化
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        lv_upload_jks = (ListView) findViewById(R.id.lv_upload_camera_room); //listview
        empty_data = (TextView)findViewById(R.id.empty_data);//textview 数据为空显示
        empty_data.setText("目前没有核查不通过的信息");
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);//地图模式隐藏当前布局
        initMapData();//加载地图数据
        UploadJKSList();//核查不通过的监控室列表
    }


    //加载地图数据
    private void initMapData(){
        makerLayout = (LinearLayout) LayoutInflater.from(CheckFaileJKSInfoActivity.this).
                inflate(R.layout.map_marker_layout, null);
        //地图相关设置
        mapdata = new MapDataModle();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }


    /** *获取核查失败的监控室列表*/
    private void UploadJKSList() {
        JSONObject jo = new JSONObject();
        jo.put("username", AppContext.instance.getLoginInfo().getUsername());
        String json = jo.toJSONString();

        Message msg = new Message();
        msg.what = 0;
        mHandle.handleMessage(msg);
        ApiResource.postCheckFaileJKS(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        String result = new String(bytes);
                        jKSInfo = JSON.parseObject(result, new TypeReference<List<JKSInfoModle>>() {
                        });
                        //????????????????? ???????
                        if (jKSInfo.size() != 0) {
                            for (int j = 0; j < jKSInfo.size(); j++) {
                                jKSInfo.get(j).setJKSYWFL(zdUtils.getZdlbAndZdbmToZdz(GeneralUtils.XXCJ_JKSYWFL,jKSInfo.get(j).getJKSYWFLBM()));
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


    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在获取核查失败的摄像头信息");
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
                    BaseUtil.showDialog("获取数据失败", CheckFaileJKSInfoActivity.this);
                    dataNull();
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



    /**actionbar????
     * ???????????
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        action_list = menu.findItem(R.id.action_list);
        action_map = menu.findItem(R.id.action_map);
        action_map.setVisible(false);
        return true;
    }

    /**
     * actionbar???????
     * */
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

            case R.id.action_map://??????  ????б?
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
        //????????
        BitmapDescriptor bdJks = BitmapDescriptorFactory.fromView(makerLayout);
        if(jKSInfo!=null) {
            //?????????
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
                    popText.setText("??????????" + jks.getJKSMC());
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
        //?????????????  ???
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
    public void showJksInfo(JKSInfoModle jks) {
        Intent intent = new Intent(CheckFaileJKSInfoActivity.this, WatchJKSInfoActivity.class);
        intent.putExtra(GeneralUtils.JKSInfo, jks);
        intent.setFlags(GeneralUtils.DownJKSInfoActivity);
        startActivity(intent);
    }

    //加载数据
    private void initListViewData() {
        lv_upload_jks.setVisibility(View.VISIBLE);
        empty_data.setVisibility(View.GONE);
        //????adapter
        mAdapter =new ShowsJKSAdapter(lv_upload_jks,jKSInfo,R.layout.upload_camera_room_adapter,this);
        lv_upload_jks.setAdapter(mAdapter);
        //点击item到  查看信息页面  需要传递 摄像头的信息
        lv_upload_jks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CheckFaileJKSInfoActivity.this, WatchJKSInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(GeneralUtils.JKSInfo, jKSInfo.get(i));
                intent.putExtras(mBundle);
                intent.setFlags(GeneralUtils.CheckFaileJKSInfoActivity);
                startActivity(intent);
            }
        });
    }


    //当没有数据，布局的显示
    private void dataNull() {
        lv_upload_jks.setVisibility(View.GONE);
        empty_data.setVisibility(View.VISIBLE);
        empty_data.setText("目前没有核查不通过的信息");
        return;
    }
    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.CheckFaile_JKSinfo);
    }
}