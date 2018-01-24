package cn.net.xinyi.xmjt.activity.Collection.Camera;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
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

import java.io.File;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.Collection.Camera.Adapter.ShowsJKSAdapter;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.JKSInfoModle;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DB.CollectDBUtils;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.GetLocation;

/**
 * Created by hao.zhou on 2015/9/18.
 */
public class ManageJKSInfoActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_upload_JKS;
    private List<JKSInfoModle> jKSInfo;
    private ShowsJKSAdapter mAdapter;
    private TextView empty_data;
    private Button menu_sure;
    private int networkType;
    int uploadCount = 0;
    int noImageCount = 0;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapDataModle mapdata;
    private RelativeLayout rl_top;
    private LinearLayout makerLayout;
    public static ManageJKSInfoActivity intence;
    private MarkerOptions oo;
    private InfoWindow mInfoWindow;
    private View viewCatch;
    private MenuItem action_list,action_map;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_camera_room_info);
        intence=this;
        getActionBar().setIcon(R.drawable.actionbar_back_icon_normal);
        getActionBar().setTitle(getResources().getString(R.string.JKSinfo_Manage));
        getActionBar().setHomeButtonEnabled(true);
        /** *百度地图请求当前位置信息  */
        new GetLocation(this, handler).startLocation();
        initView();//控件初始化
    }


    private void initView() {
        //地图
        mapdata = new MapDataModle();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        lv_upload_JKS = (ListView) findViewById(R.id.lv_upload_camera_room);
        empty_data = (TextView) findViewById(R.id.empty_data);
        menu_sure = (Button) findViewById(R.id.menu_sure);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);

        initAdapter();
        setListener();//监听
        initData();//加载数据
    }

    private void initAdapter() {//地图标识
        makerLayout = (LinearLayout) LayoutInflater.from(ManageJKSInfoActivity.this).
                inflate(R.layout.map_marker_layout, null);
    }

    private void setListener() {
        menu_sure.setOnClickListener(this);
    }

    public void showJksInfo(JKSInfoModle jks) { //查看监控室
        Intent intent = new Intent(ManageJKSInfoActivity.this, WatchJKSInfoActivity.class);
        intent.putExtra(GeneralUtils.JKSInfo, jks);
        startActivity(intent);
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
                /** *点击列表  切换为地图模式
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

    //设置地图模式下地图数据
    private void setMapOverlay() {
        //监控室图标
        BitmapDescriptor bdJks = BitmapDescriptorFactory.fromView(makerLayout);
        //添加监控室标注
        for (int i = 0; i < jKSInfo.size(); i++) {
            LatLng ll = new LatLng(Double.valueOf(jKSInfo.get(i).getWD()), Double.valueOf(jKSInfo.get(i).getJD()));
            oo = new MarkerOptions().position(ll).icon(bdJks).zIndex(jKSInfo.size()).title("JKS");
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("JKS", jKSInfo.get(i));
            oo.extraInfo(mBundle);
            mBaiduMap.addOverlay(oo);

        }

        //添加标注点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                viewCatch = LayoutInflater.from(getApplicationContext()).inflate(R.layout.caiji_pop_layout, null);
                final TextView popText = ((TextView) viewCatch.findViewById(R.id.location_tips));
                //获取地址保存按钮
                final Button mButton = ((Button) viewCatch.findViewById(R.id.location_save));

                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(viewCatch, ll, 20);
                mBaiduMap.showInfoWindow(mInfoWindow);

                if (marker.getTitle().equals("JKS")) {
                    Bundle mBundle = marker.getExtraInfo();
                    final JKSInfoModle jks = (JKSInfoModle) mBundle.get("JKS");
                    popText.setText("监控室名称："+jks.getJKSMC());
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

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mInfoWindow!=null){
                    mBaiduMap.hideInfoWindow();
                }
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) { return false; }
        });

    }


    private void initData() {//加载本地获得数据
        jKSInfo = CollectDBUtils.getJKSLocalData(ManageJKSInfoActivity.this);
        //当没有数据，布局的显示
        dataNull();
        mAdapter = new ShowsJKSAdapter(lv_upload_JKS,jKSInfo,R.layout.upload_camera_room_adapter,this);
        lv_upload_JKS.setAdapter(mAdapter);

        //点击item到查看信息页面
        lv_upload_JKS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ManageJKSInfoActivity.this, WatchJKSInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(GeneralUtils.JKSInfo, jKSInfo.get(i));
                intent.putExtras(mBundle);
                startActivityForResult(intent, 100);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_sure://上传
                //检测本地是否有未监控室采集信息
                jKSInfo = CollectDBUtils.getJKSLocalData(ManageJKSInfoActivity.this);
                if (jKSInfo == null || jKSInfo.size() == 0) {
                    BaseUtil.showDialog("没有需要上传的监控室采集信息！", ManageJKSInfoActivity.this);
                    break;
                }

                //检测是否连接wifi网络
                networkType = ((AppContext) getApplication()).getNetworkType();
                if (networkType == 0) {
                    BaseUtil.showDialog("当前无可用的网络连接，无法上传", ManageJKSInfoActivity.this);
                    break;
                }

//                if (networkType != 1) {
//                    BaseUtil.showDialog("只允许在 WIFI 网络下上传", ManageJKSInfoActivity.this);
//                    break;
//                }
                //没有连接wifi提示，是否继续上传
                if (networkType != 1) {
                    diaoShow(getResources().getString(R.string.upload_no_wifi_tips));
                    break;
                }
                //显示上传的对话框
                diaoShow(getResources().getString(R.string.upload_jks_tips));
                break;
        }
    }


    private void diaoShow(String message) {
        AlertDialog dialog = new AlertDialog.Builder(ManageJKSInfoActivity.this).
                setTitle("提示").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                        //检查APP是否最新版本及网络是否连通
                        new Thread() {
                            @Override
                            public void run() {
                                ApiResource.getVersionByAppid(AppContext.APP_ID, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        Message msg = new Message();
                                        try {
                                            String result = new String(bytes);
                                            if (result != null && result.trim() != "") {
                                                //获取服务器端版本号
                                                JSONObject jo_v = JSONObject.parseObject(result);
                                                int newVersionCode = Integer.parseInt(jo_v.getString(GeneralUtils.BUILDNUMBER));
                                                //获取当前版本号
                                                PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                                int curVersionCode = info.versionCode;
                                                if (newVersionCode > curVersionCode) {
                                                    //有新版本
                                                    msg.arg1 = 0;
                                                } else {
                                                    msg.arg1 = 1;
                                                }
                                                msg.what = 4;
                                                handler.sendMessage(msg);
                                            } else {
                                                onFailure(i, headers, bytes, null);
                                            }
                                        } catch (Exception e1) {
                                            onFailure(i, headers, bytes, null);
                                        }
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        Message msg = new Message();
                                        msg.what = 5;
                                        if (i == 0) {
                                            //检测超时或当前网络不能连接到互联网
                                            msg.arg1 = 0;
                                        } else {
                                            msg.arg1 = 1;
                                        }
                                        handler.sendMessage(msg);
                                    }
                                });
                            }
                        }.start();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }


    private ProgressDialog mProgressDialog = null;
    private BDLocation location;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0://上传前检测
                    mProgressDialog = new ProgressDialog(ManageJKSInfoActivity.this);
                    mProgressDialog.setTitle("上传前检测");
                    mProgressDialog.setMessage("检测网络情况及APP是否最新版本");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 1:// 上传进度条显示
                    mProgressDialog.cancel();
                    int count = msg.arg1;
                    String msgText = "本机共有 " + count + "条监控室采集信息需要上传";
                    mProgressDialog = new ProgressDialog(ManageJKSInfoActivity.this);
                    mProgressDialog.setProgress(count);
                    mProgressDialog.setMax(count);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setTitle("信息上传中");
                    mProgressDialog.setMessage(msgText);
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    break;

                case 2:// 上传之后
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("本次上传了" + msg.arg1 + "条监控室采集信息", ManageJKSInfoActivity.this);
                    initView();
                    break;

                case 3:// 上传失败
                    mProgressDialog.cancel();
                    BaseUtil.showDialog("上传失败，可能当前上传的人数较多，请稍候重试！", ManageJKSInfoActivity.this);
                    break;

                case 4://上传检测完成
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到当前APP 版本过低，请回到民警通主菜单点击【系统设置】-【APP 更新】，按提示升级APP 后重新上传！", ManageJKSInfoActivity.this);
                    } else {
                        //开始上传监控室信息
                        startUploadThread();
                    }
                    break;

                case 5://上传检测失败
                    mProgressDialog.cancel();
                    if (msg.arg1 == 0) {
                        BaseUtil.showDialog("系统检测到你当前的WIFI网络无法连接互联网或不稳定，无法上传，请检查网络正常后重新上传！", ManageJKSInfoActivity.this);
                    } else {
                        BaseUtil.showDialog("检测失败！", ManageJKSInfoActivity.this);
                    }
                    break;

                case -100:
                    location = (BDLocation) msg.obj;
                    mapdata.setLatitude(""+location.getLatitude());
                    mapdata.setLongitude("" + location.getLongitude());
                    break;

            }
        }
    };

    private void startUploadThread() {
        // 保存与上传
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = jKSInfo.size();
                handler.sendMessage(msg);
                // 将数据及视频上传到服务器
                uploadCameraRoomImage(jKSInfo);
                int result = uploadCount;

                if (result > 0) {
                    msg = new Message();
                    msg.what = 2;
                    msg.arg1 = result;
                    handler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //同步上传监控室图片到服务端
    void uploadCameraRoomImage(List<JKSInfoModle> list) {
        uploadCount = 0;
        noImageCount = 0;

        for (int i = 0; i < list.size(); i++) {
            final JKSInfoModle mCarInfo = list.get(i);
            File captureImage = null;
            if (mCarInfo.getZP1() != null) {
                String path= mCarInfo.getZP1();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
                captureImage = new File(path);
            }
            if (mCarInfo.getZP2() != null) {
                String path= mCarInfo.getZP2();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
                captureImage = new File(path);
            }
            if (mCarInfo.getZP3() != null) {
                String path= mCarInfo.getZP3();
                filePath.add(path);
                fileName.add(BaseUtil.getFileNameNoEx(path));
                captureImage = new File(path);
            }

            if (!captureImage.exists()) {
                noImageCount++;
                continue;
            }

            //上传监控室图片
            ApiResource.uploadCameraRoomImage(filePath, fileName, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(final int i, final Header[] headers, final byte[] bytes) {
                    String result = new String(bytes);
                    if (i == 200 && result != null && result.startsWith("true")) {
                        fileName.clear();
                        filePath.clear();
                        uploadCameraRoomInfo(mCarInfo);
                    } else {
                        onFailure(i, headers, bytes, null);
                    }
                }

                @Override
                public void onFailure(final int i, final Header[] headers, final byte[] bytes, final Throwable throwable) {
                    if (bytes != null) {
                        String result = new String(bytes);
                    }
                }
            });
        }
    }


    //同步上传监控采集数据到服务端
    public void uploadCameraRoomInfo(final JKSInfoModle info) {

        //json处理
        JSONObject jo = JSON.parseObject(JSON.toJSONString(info));
        if (jo != null) {
            jo.remove("id");
            jo.remove("zP1");
            jo.remove("zP2");
            jo.remove("zP3");
            jo.remove("policeId");
            jo.remove("cellID");
            jo.remove("cellLocationCode");
            jo.remove("isupdate");
            jo.remove("networkCode");
            if (info.getZP1() != null) {
                jo.put("zP1", BaseUtil.getFileName(info.getZP1()));
            }
            if (info.getZP2() != null) {
                jo.put("zP2", BaseUtil.getFileName(info.getZP2()));
            }
            if (info.getZP3() != null) {
                jo.put("zP3", BaseUtil.getFileName(info.getZP3()));
            }
        }

        String json = jo.toJSONString();
        //保存上传日志
        // AppConfig.saveUploadLog(json);

        ApiResource.addCameraRoomInfo(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                if (!result.isEmpty() && result.startsWith("true")) {
                    //已上传记数+1；
                    uploadCount++;
                    //删除本地数据库中的记录
                    boolean flag = delPlateInfo(info.getId());

                    if (flag) {
                        if (info.getZP1() != null) {
                            //删除本地图片1
                            File plateImage = new File(info.getZP1());
                            if (plateImage.exists()) {
                                plateImage.delete();
                            }
                        }

                        if (info.getZP2() != null) {
                            //删除本地图片2
                            File plateImage = new File(info.getZP2());
                            if (plateImage.exists()) {
                                plateImage.delete();
                            }
                        }

                        if (info.getZP3() != null) {
                            //删除本地图片3
                            File plateImage = new File(info.getZP3());
                            if (plateImage.exists()) {
                                plateImage.delete();
                            }
                        }
                    }
                    mProgressDialog.incrementProgressBy(1);

                } else {
                    onFailure(i, headers, bytes, null);
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


    /**
     * 删除本地数据库中已上传监控室信息
     *
     * @param id
     * @return
     */
    private boolean delPlateInfo(int id) {
        boolean mFlag = false;
        DBOperation dbo = new DBOperation(this);
        mFlag = dbo.delCameraRoomInfo(id);
        dbo.clossDb();
        return mFlag;
    }


    //返回刷新数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1) {
            initView();
        }
    }


    //当没有数据，布局的显示
    private void dataNull() {
        if (jKSInfo == null || jKSInfo.size() == 0) {
            empty_data.setVisibility(View.VISIBLE);
            lv_upload_JKS.setVisibility(View.GONE);
            menu_sure.setVisibility(View.GONE);
            return;
        }
    }


    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.dispatchKeyEvent(event);
    }
}