package cn.net.xinyi.xmjt.activity.Plate;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wintone.plateid.PlateCfgParameter;
import com.wintone.plateid.PlateRecognitionParameter;
import com.wintone.plateid.RecogService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.AppException;
import cn.net.xinyi.xmjt.config.BaseActivity;
import cn.net.xinyi.xmjt.model.PlateInfoModle;
import cn.net.xinyi.xmjt.utils.BaseUtil;
import cn.net.xinyi.xmjt.utils.DB.DBOperation;
import cn.net.xinyi.xmjt.utils.PositionUtil;
import cn.net.xinyi.xmjt.utils.PositionUtil.CellIDInfo;


public class PlateRecogResultActivity extends BaseActivity implements OnClickListener{
    private static final String TAG = "PlateRecogResultActivity";
    //车牌 证件识别 devcode
    public static String Devcode="5REX5ZYZ5BIC5LA";//old  2018.1.3更换  5REX5ZYZ5BIC5LA
    private PopupWindow mpopupWindow;
    private LinearLayout layout_plateInfo;
    private ImageView iv_plate_image;
    private TextView tv_plate_info_gps;
    private TextView tv_plate_info_time;
    private TextView tv_plate_info_callid;
    private TextView tv_plate_info_address;
    private TextView tv_plate_info_color;
    private TextView tv_plate_info_accuracy;
    private EditText tv_plate_info_number;
    //	private EditText tv_plate_info_number;
    private Button btn_cancel;
    private Button btn_save;
    private Button btn_zoom;
    private Button btn_map;
    private long MIN_FILE_SIZE = 1024 * 50;

    // 文通车牌识别
    public RecogService.MyBinder recogBinder;
    private int iInitPlateIDSDK = -1;
    private int imageformat = 1;
    private int bVertFlip = 0;
    private int bDwordAligned = 1;
    private String[] fieldvalue = new String[14];
    private int nRet = -1;
    private String recogPicPath;
    private int width = 420;
    private int height = 232;

    int[] fieldname = {R.string.plate_number, R.string.plate_color,
            R.string.plate_color_code, R.string.plate_type_code,
            R.string.plate_reliability, R.string.plate_brightness_reviews,
            R.string.plate_move_orientation, R.string.plate_leftupper_pointX,
            R.string.plate_leftupper_pointY, R.string.plate_rightdown_pointX,
            R.string.plate_rightdown_pointY, R.string.plate_elapsed_time,
            R.string.plate_light, R.string.plate_car_color};

    private ProgressDialog mProgressDialog = null;
    private String plateThumb;
    // 缩略图宽度
    private static final int PLATE_THUMB_WIDTH = 160;
    // 缩略图高度
    private static final int PLATE_THUMB_HEIGHT = 120;

    private boolean flag = false;
    private boolean bUpload = false;
    private PlateInfoModle capturePlateInfo;

    // 地图标注及定位
    Marker mCurrentMarker;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private int zoomLevel = 18;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    private int span = 1000;
    private LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public BDLocation curLocation;
    public double DEFAULT_LATITUDE = 22.726214;
    public double DEFAULT_LONGITUDE = 114.254215;
    public final float DEFAULT_ACCURACY = 20.0f;
    public final int MIN_PLATE_ACCURACY = 75;

    // 定位图层
    private boolean isFirstLoc = true;// 是否首次定位
    BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    private InfoWindow mInfoWindow;
    private BigDecimal map_latitude, map_longitude;
    private String address = "";
    private GeoCoder geoCoder;
    private int networkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.plate_result_land);
        initResource();

        recogPicPath = getIntent().getExtras().getString(
                PlateListActivity.KEY_PLATE_IMAGE);

        tv_plate_info_time.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",
                Calendar.getInstance(Locale.CHINA)));

        //车牌识别
        if (!recogPicPath.isEmpty()) {
            iv_plate_image.setImageBitmap(decodeFile(recogPicPath));
            new Thread() {
                public void run() {
                    Intent recogIntent = new Intent(
                            getApplicationContext(), RecogService.class);
                    bindService(recogIntent, recogConn,
                            Service.BIND_AUTO_CREATE);
                }

            }.start();
        }
        // 获取定位信息
        getPositionAddress();
    }

    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        if(AppContext.lastLocationLongitude!="0.00000") {
            DEFAULT_LATITUDE= Double.valueOf(AppContext.lastLocationLatitude);
            DEFAULT_LONGITUDE= Double.valueOf(AppContext.lastLocationLongitude);
        }

        // 设置默认坐标
        LatLng defaultLL = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        MapStatus mMapStatus = new MapStatus.Builder().target(defaultLL)
                .zoom(zoomLevel).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(u);

        //点击定位图标，获取地址
        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                networkType = ((AppContext) getApplication()).getNetworkType();
                mBaiduMap.clear();
                OverlayOptions oo = new MarkerOptions().position(latLng).icon(bd).title(latLng.latitude + "," + latLng.longitude).zIndex(9).draggable(true);
                mCurrentMarker = (Marker) (mBaiduMap.addOverlay(oo));

                View viewCatch = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_layout, null);

                final TextView popText = ((TextView) viewCatch.findViewById(R.id.location_tips));

                //坐标截取，保存小数点后面6位
                map_latitude = new BigDecimal(mCurrentMarker.getPosition().latitude);
                map_latitude = map_latitude.setScale(6, BigDecimal.ROUND_HALF_UP);

                map_longitude = new BigDecimal(mCurrentMarker.getPosition().longitude);
                map_longitude = map_longitude.setScale(6, BigDecimal.ROUND_HALF_UP);
                //获取地址保存按钮
                final Button mButton = ((Button) viewCatch.findViewById(R.id.location_save));

                //判断是否有网
                if (networkType == 0) {
                    popText.setText(map_latitude + "\n" + map_longitude);
                    mButton.setEnabled(true);

                } else {
                    geoCoder = GeoCoder.newInstance();
                    LatLng ptCenter = new LatLng((Float.valueOf("" + map_latitude)),
                            (Float.valueOf("" + map_longitude)));
                    // 设置反地理编码位置坐标
                    ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                    op.location(ptCenter);
                    mButton.setEnabled(false);

                    // 理编码请求(经纬度->地址信息)
                    geoCoder.reverseGeoCode(op);
                    geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                        @Override
                        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                            //获取点击的坐标地址
                            address = reverseGeoCodeResult.getAddress();
                            popText.setText(address);
                            //地址获取成功后可以点击保存
                            mButton.setEnabled(true);
                        }

                        @Override
                        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        }
                    });

                }


                mButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置经纬度
                        capturePlateInfo.setLatitude("" + map_latitude);
                        capturePlateInfo.setLongitude("" + map_longitude);

                        // 定位方式为999：手动定位
                        capturePlateInfo.setLocType("999");
                        capturePlateInfo.setAddress(address);


                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = 999;
                        handler.sendMessage(msg);
                        showMapView(false);
                    }
                });
                LatLng ll = mCurrentMarker.getPosition();
                mInfoWindow = new InfoWindow(viewCatch, ll, -60);
                mBaiduMap.showInfoWindow(mInfoWindow);
            }

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }
        });
    }


    @SuppressLint("InflateParams")
    private void initResource() {
        initMapView();
        networkType = ((AppContext) getApplication()).getNetworkType();
        mLocationClient = ((AppContext) getApplication()).mLocationClient;
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        layout_plateInfo = (LinearLayout) findViewById(R.id.layout_plate_detail);
        iv_plate_image = (ImageView) findViewById(R.id.plate_image);
        tv_plate_info_gps = (TextView) findViewById(R.id.plate_info_gps);
        tv_plate_info_time = (TextView) findViewById(R.id.plate_info_time);
        tv_plate_info_callid = (TextView) findViewById(R.id.plate_info_callid);
        tv_plate_info_address = (TextView) findViewById(R.id.plate_info_address);
        tv_plate_info_number = (EditText) findViewById(R.id.plate_info_number);
        //tv_plate_info_number = (EditText) findViewById(R.id.plate_info_number);
        tv_plate_info_color = (TextView) findViewById(R.id.plate_info_color);
        tv_plate_info_accuracy = (TextView) findViewById(R.id.plate_info_accuracy);

        iv_plate_image.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.btn_plate_cancel);
        btn_save = (Button) findViewById(R.id.btn_plate_save);
        btn_map = (Button) findViewById(R.id.btn_map_show);
        btn_zoom = (Button) findViewById(R.id.btn_zoom);

        iv_plate_image.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_map.setOnClickListener(this);
        btn_zoom.setOnClickListener(this);

        capturePlateInfo = new PlateInfoModle();

    }

    private void showPopMenu() {
        View view = View.inflate(getApplicationContext(),
                R.layout.plate_full_image, null);
        ImageView fullImage = (ImageView) view
                .findViewById(R.id.full_plate_image);

        if (!recogPicPath.isEmpty()) {
            File captureImageFile = new File(recogPicPath);
            if (captureImageFile.exists()) {
                fullImage.setImageURI(Uri.fromFile(captureImageFile));
            }
        }

        fullImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mpopupWindow.dismiss();
            }
        });

        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow(this);
            mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
            mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mpopupWindow.setFocusable(true);
            mpopupWindow.setOutsideTouchable(true);
        }

        mpopupWindow.setContentView(view);
        mpopupWindow.showAtLocation(btn_zoom, Gravity.TOP, 0, 0);
        mpopupWindow.update();

        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1000);
        view.setAnimation(aa);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消
            case R.id.btn_plate_cancel:
                cancelSave();
                break;

            // 保存
            case R.id.btn_plate_save:
                if (tv_plate_info_number.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("车牌号码不允许为空", PlateRecogResultActivity.this);
                    break;
                }

                if (tv_plate_info_gps.getText().toString().trim().isEmpty()) {
                    BaseUtil.showDialog("定位坐标不允许为空", PlateRecogResultActivity.this);
                    break;
                }
//                if ( tv_plate_info_address.getText().toString().trim().isEmpty()) {
//                    BaseUtil.showDialog("采集地点不能为空，请检查网络是否开启", PlateRecogResultActivity.this);
//                    break;
//                }


//                File mFile = new File(recogPicPath);
//                if(!mFile.exists() || mFile.length()<MIN_FILE_SIZE){
//                    BaseUtil.showDialog("检测到当前车牌图片质量太低，影响后端车牌识别效果，请重新抓拍", PlateRecogResultActivity.this);
//                    break;
//                }

                // 设置最后一次定位的数据；
                AppContext.lastLocationTimeZone = (new Date()).getTime();
                AppContext.lastLocationLatitude = String.valueOf(capturePlateInfo.getLatitude());
                AppContext.lastLocationLongitude = String.valueOf(capturePlateInfo.getLongitude());
                AppContext.lastLocationAddress = capturePlateInfo.getAddress();
                AppContext.lastLocationType = capturePlateInfo.getLocType();

                bUpload = saveToLocalDB(PlateInfoModle.ISUPDATE_LOCAL);
                if (bUpload) {
                    showPlateList();
                } else {
                    BaseUtil.showDialog("保存失败", PlateRecogResultActivity.this);
                }
                break;
            case R.id.btn_map_show:
                mapLocation();
                flag = true;
                showMapView(flag);
                break;
            case R.id.btn_zoom:
                showPopMenu();
                break;
            case R.id.plate_image:
                showPopMenu();
                break;
            default:
                break;
        }
    }

    /**
     * 取消保存删除确认
     */
    private void cancelSave() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认放弃这条记录吗？")
                .setPositiveButton(R.string.sure,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 点击“确认”后的操作
                                File file = new File(recogPicPath);
                                if (file.exists()) {
                                    file.delete();
                                }

                                showPlateList();
                            }
                        })
                .setNegativeButton(R.string.cancle,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 获取当前GPS地址信息后显示
                    if (networkType == 0) {
                        tv_plate_info_address.setHint("当前没有开启网络连接，无法获得采集地点");
                    }else {
                        tv_plate_info_address.setText(capturePlateInfo.getAddress());
                    }
                    tv_plate_info_gps.setText(capturePlateInfo.getLatitude() + ","
                            + capturePlateInfo.getLongitude());
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                    break;
                case 2:
                    // 获取当前GPS反地理信息失败后显示
                    // 如果没有手动定位则采用上一次定位结果
                    if (!capturePlateInfo.getLatitude().equals("0.00000")) {
                        Toast.makeText(PlateRecogResultActivity.this, "定位失败，沿用上一次定位结果",
                                Toast.LENGTH_SHORT).show();
                        tv_plate_info_address
                                .setText(capturePlateInfo.getAddress());

                        tv_plate_info_gps.setText(capturePlateInfo.getLatitude() + ","
                                + capturePlateInfo.getLongitude());

                    } else {
//					Toast.makeText(PlateRecogResultActivity.this, "定位失败，请手动定位",
//							Toast.LENGTH_SHORT).show();
                        tv_plate_info_gps.setHint("定位失败，请手动定位");
                        tv_plate_info_gps.setText("");
                    }

                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                    break;
                case 3:// 上传进度条显示
                    mProgressDialog = new ProgressDialog(PlateRecogResultActivity.this);
                    mProgressDialog.setMessage("正在保存数据,请稍候...");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.show();
                    break;
                case 4:// 保存之后
                    mProgressDialog.cancel();
                    if (bUpload) {
                        showPlateList();
                    } else {
                        BaseUtil.showDialog("保存失败", PlateRecogResultActivity.this);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 捕获后退按钮
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 在2级菜单用
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mMapView.isShown()) {
                showMapView(false);
            } else {
                cancelSave();
            }
        }
        // super.dispatchKeyEvent(event);
        return false;
    }

    /**
     * 显示车牌列表
     */
    private void showPlateList() {
        this.finish();
    }

    // *********************地图及定位**********************

    /**
     * 根据当前GPS定位坐标获取地理信息
     */
    private void getPositionAddress() {

        initLocation();
        long currentTimeZone = (new Date()).getTime();

        // 判定是否沿用上一次定位
        if (currentTimeZone - AppContext.LOCATION_SPAN <= AppContext.lastLocationTimeZone) {
            capturePlateInfo.setLatitude(AppContext.lastLocationLatitude);
            capturePlateInfo.setLongitude(AppContext.lastLocationLongitude);
            capturePlateInfo.setAddress(AppContext.lastLocationAddress);
            capturePlateInfo.setLocType(AppContext.lastLocationType);

            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);

        } else {
            mLocationClient.start();
        }

        // 获取基站信息
        PositionUtil pu = new PositionUtil(PlateRecogResultActivity.this);

        if (pu.getCallId() != null ) {
//        if (cellIDs != null && cellIDs.size() > 0) {
            ArrayList<CellIDInfo> cellIDs = pu.getCallId();
            String cellid = String.valueOf(cellIDs.get(0).cellId);
            String cellLocationCode = String
                    .valueOf(cellIDs.get(0).locationAreaCode);
            capturePlateInfo.setCellID(cellid == null ? "" : cellid);
            capturePlateInfo.setCellLocationCode(cellLocationCode == null ? ""
                    : cellLocationCode);
            tv_plate_info_callid.setText(capturePlateInfo.getCellID() + ","
                    + capturePlateInfo.getCellLocationCode());
        } else {
            tv_plate_info_callid.setText("0,0");
        }
    }


    /**
     * 显示或隐藏地图
     */
    private void showMapView(boolean mFlag) {
        if (mFlag) {
            this.mMapView.setVisibility(View.VISIBLE);
            layout_plateInfo.setVisibility(View.GONE);
        } else {
            this.mMapView.setVisibility(View.GONE);
            layout_plateInfo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);
        option.setCoorType(tempcoor);
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }



    /**
     * 实现定位回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        int i = 0;
        @Override
        public void onReceiveLocation(BDLocation location) {
            int locType = location.getLocType();
            i++;
            if (location != null && locType != BDLocation.TypeNetWorkException) {
                if (isFirstLoc) {
                    isFirstLoc = false;
                    capturePlateInfo.setTime(location.getTime());
                    capturePlateInfo.setLocType(String.valueOf(location.getLocType()));
                    //坐标截取，保存小数点后面6位
                    BigDecimal map_latitude_1 = new BigDecimal(location.getLatitude());
                    map_latitude_1 = map_latitude_1.setScale(6,BigDecimal.ROUND_HALF_UP);

                    BigDecimal  map_longitude_1 = new BigDecimal(location .getLongitude());
                    map_longitude_1 = map_longitude_1.setScale(6, BigDecimal.ROUND_HALF_UP);

                    capturePlateInfo.setLatitude(String.valueOf(map_latitude_1));
                    capturePlateInfo.setLongitude(String.valueOf(map_longitude_1));
                    capturePlateInfo.setAddress(location.getAddrStr());


                    curLocation = location;

                    // 设置最后一次定位的数据；
                    AppContext.lastLocationTimeZone = (new Date())
                            .getTime();
                    AppContext.lastLocationLatitude = capturePlateInfo.getLatitude();
                    AppContext.lastLocationLongitude = capturePlateInfo.getLongitude();

                    AppContext.lastLocationAddress = capturePlateInfo.getAddress();
                    AppContext.lastLocationType = capturePlateInfo.getLocType();

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }

            // 定位10次后还不成功停止定位；
            if (i > 5 && isFirstLoc) {
                isFirstLoc = false;
                //没有手动定位,则自动沿用上一次定位结果
                if (capturePlateInfo.getLatitude().equals("0.00000") || capturePlateInfo.getLatitude().isEmpty()) {

                    capturePlateInfo.setLatitude(AppContext.lastLocationLatitude);
                    capturePlateInfo.setLongitude(AppContext.lastLocationLongitude);
                    capturePlateInfo.setAddress(AppContext.lastLocationAddress);
                    capturePlateInfo.setLocType(AppContext.lastLocationType);

                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }
    }

    /**
     * 地图定位
     */
    private void mapLocation() {
        LatLng ll = null;
        if (curLocation != null) {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(curLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(curLocation.getLatitude())
                    .longitude(curLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            ll = new LatLng(curLocation.getLatitude(),
                    curLocation.getLongitude());
        } else {
            ll = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        }

        MapStatus mMapStatus = new MapStatus.Builder().target(ll)
                .zoom(zoomLevel).build();

        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(u);
    }

    // *********************车牌比对**********************

    /**
     * 显示车牌识别结果
     *
     * @param fieldvalue
     */
    private void getResult(String[] fieldvalue) {
        String reCaptureMsg = "识别失败，请重新抓拍";

        if (nRet == -1008 || nRet == 20) {
            //reCaptureMsg = "待上传到服务器识别";
            tv_plate_info_number.setHint(reCaptureMsg);
            Toast.makeText(PlateRecogResultActivity.this, "识别算法未授权，请检测你的TF卡",
                    Toast.LENGTH_SHORT).show();
        } else if (nRet != 0 || fieldvalue == null || fieldvalue[0] == null) {
            tv_plate_info_number.setHint(reCaptureMsg);
        } else {
            int accuracy = 0;
            try {
                accuracy = Integer.parseInt(getFirstFieldBySplit(fieldvalue[4])
                        .trim());
            } catch (Exception e) {

            }

            if (accuracy < MIN_PLATE_ACCURACY) {
                tv_plate_info_number.setHint(reCaptureMsg);

            } else {

                String plate_number = getFirstFieldBySplit(fieldvalue[0]);
                String plate_color = getFirstFieldBySplit(fieldvalue[1]);
                String plate_accuracy = getFirstFieldBySplit(fieldvalue[4]);

                capturePlateInfo.setAccuracy(plate_accuracy);
                tv_plate_info_accuracy.setText(plate_accuracy);
                tv_plate_info_number.setText(plate_number);
                tv_plate_info_color.setText(plate_color);

                //车牌本地比对
                //int result = new BlackPlateDBHelper(this).checkBlatePlate(plate_number);
                int result = 0;
                capturePlateInfo.setInBlacklist(result == 0 ? "0" : "1");
//                if (result > 0) {
//                    playAlertRingtone();
//                    BaseUtil.showWarnDialog("发现疑似盗抢车辆，请确认该车牌是否识别正确，避免误报！", PlateRecogResultActivity.this);
//                } else {
//                    BaseUtil.showDialog("已完成比对，车牌比对库中无此车牌", PlateRecogResultActivity.this);
//                }

            }
        }
        nRet = -1;
        fieldvalue = null;
    }

    private void playAlertRingtone() {
        //Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    private String getFirstFieldBySplit(String str) {
        if (str.isEmpty())
            return "";
        String[] fields = str.split(";");
        return fields[0];
    }

    /**
     * 车牌识别服务
     */
    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogConn = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // System.out.println("ResultActivity onServiceConnected");
            recogBinder = (RecogService.MyBinder) service;
            iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();
            if (iInitPlateIDSDK != 0) {
                nRet = iInitPlateIDSDK;
                String[] str = {"" + iInitPlateIDSDK};
                getResult(str);
            } else {
                // recogBinder.setRecogArgu(recogPicPath, imageformat,
                // bGetVersion, bVertFlip, bDwordAligned);
                PlateCfgParameter cfgparameter = new PlateCfgParameter();
                cfgparameter.armpolice = 5;// 单层武警车牌是否开启:4是；5不是
                cfgparameter.armpolice2 = 17;// 双层武警车牌是否开启:16是；17不是
                cfgparameter.embassy = 13;// 使馆车牌是否开启:12是；13不是
                cfgparameter.individual = 0;// 是否开启个性化车牌:0是；1不是
                cfgparameter.nContrast = 5;// 清晰度指数(取值范围0-9,最模糊时设为1;最清晰时设为9)
                cfgparameter.nOCR_Th = 0;
                cfgparameter.nPlateLocate_Th = 7;// 识别阈值(取值范围0-9,5:默认阈值0:最宽松的阈值9:最严格的阈值)
                cfgparameter.onlylocation = 15;// 只定位车牌是否开启:14是；15不是
                cfgparameter.tworowyellow = 3;// 双层黄色车牌是否开启:2是；3不是
                cfgparameter.tworowarmy = 7;// 双层军队车牌是否开启:6是；7不是
                cfgparameter.szProvince = "粤";// 省份顺序
                cfgparameter.onlytworowyellow = 11;// 只识别双层黄牌是否开启:10是；11不是
                cfgparameter.tractor = 9;// 农用车车牌是否开启:8是；9不是
                cfgparameter.bIsNight = 0;// 是否夜间模式：1是；0不是
                recogBinder.setRecogArgu(cfgparameter,imageformat, bVertFlip, bDwordAligned);

                // fieldvalue = recogBinder.doRecog(recogPicPath, width,
                // height);
                PlateRecognitionParameter prp = new PlateRecognitionParameter();
                prp.height = height;// 图像高度
                prp.width = width;// 图像宽度
                prp.pic = recogPicPath;// 图像文件
                // prp.dataFile = dataFile;
                //  prp.devCode = "5REX5ZYZ5BIC5LA";
                prp.devCode = Devcode;
                // prp.versionfile =
                // Environment.getExternalStorageDirectory().toString()+"/AndroidWT/wtversion.lsc";;

                try {
                    fieldvalue = recogBinder.doRecogDetail(prp);
                    nRet = recogBinder.getnRet();
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }

                if (nRet != 0) {
                    String[] str = {"" + nRet};
                    getResult(str);
                } else {
                    getResult(fieldvalue);
                }
            }
            if (recogBinder != null) {
                unbindService(recogConn);
            }
        }
    };

    // *********************数据库操作**********************
    private DBOperation dbo;

    // 保存到本地sqlite数据库
    private boolean saveToLocalDB(int isupdate) {

        //重命名图片文件
//        String oldFile = recogPicPath;
//        String lpn = tv_plate_info_number.getText().toString().trim();
//        if(!lpn.isEmpty()) {
//            String newFile = oldFile.replace("LPN", lpn);
//            boolean b = BaseUtil.renameFile(oldFile, newFile);
//            if (b) {
//                recogPicPath = newFile;
//            }
//        }

        // 生成缩略图
        setThumbnail();
        capturePlateInfo.setUserName(AppContext.instance.getLoginInfo().getUsername());
        capturePlateInfo.setOrgName(AppContext.instance.getLoginInfo().getPcs());

        capturePlateInfo.setDeviceid(((AppContext) getApplication())
                .getDeviceId());
        capturePlateInfo.setTime(tv_plate_info_time.getText().toString().trim());
        capturePlateInfo.setPlateImage(recogPicPath);
        capturePlateInfo.setPlateThumb(plateThumb);
        capturePlateInfo.setAddress(tv_plate_info_address.getText().toString().trim());
        capturePlateInfo.setPlateNumber(tv_plate_info_number.getText().toString().trim());
        capturePlateInfo.setPlateColor(tv_plate_info_color.getText().toString()
                .trim());
        capturePlateInfo.setIsupdate(String.valueOf(PlateInfoModle.ISUPDATE_LOCAL));

        long rows = 0;

        // 本地保存
        dbo = new DBOperation(PlateRecogResultActivity.this);
        try {
            rows = dbo.insertPlateToSqlite(capturePlateInfo);
        } catch (AppException e) {
            e.printStackTrace();
        }
        dbo.clossDb();
        return rows != 0;
    }
    //解码图片然后对图片进行缩放以减少内存消耗
    private Bitmap decodeFile(String path){
        try {
            //解码图片大小
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(path),null,o);
            //我们想要的新的图片大小
            final int REQUIRED_SIZE=240;
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;
            //用inSampleSize解码
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(path), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    /**
     * 生成缩略图
     */
    private void setThumbnail() {
        Bitmap bitmap =decodeFile(recogPicPath);
        plateThumb = recogPicPath.replace(".jpg", "_thumb.jpg");

        // 缩略图
        Bitmap bitmapThumb = ThumbnailUtils.extractThumbnail(bitmap,
                PLATE_THUMB_WIDTH, PLATE_THUMB_HEIGHT);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(plateThumb);
            bitmapThumb.compress(Bitmap.CompressFormat.PNG, 50, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
            bitmapThumb.recycle();
            bitmap=null;
            bitmapThumb=null;
            System.gc();
        }
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        // mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
