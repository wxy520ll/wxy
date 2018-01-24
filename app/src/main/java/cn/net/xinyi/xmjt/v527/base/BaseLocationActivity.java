package cn.net.xinyi.xmjt.v527.base;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.permission.DefaultRequestPermissionsListener;
import com.xinyi_tech.comm.permission.PermissionsHelp;
import com.xinyi_tech.comm.util.ToastyUtil;

import cn.net.xinyi.xmjt.v527.util.BaiduMapUtil;
import cn.net.xinyi.xmjt.v527.util.LocationUtils;


/**
 * Created by Fracesuit on 2017/8/31.
 */

public abstract class BaseLocationActivity<T extends BasePresenter> extends BaseJwtActivity<T> implements BDLocationListener {
    public static final int LOCATION_START = 1;
    public static final int LOCATION_SUCCESS = 2;
    public static final int LOCATION_ERROR = 3;
    public LocationClient mLocationClient;

    protected boolean isAutoLocation = true;
    protected boolean isKeepLocation = false;
    protected boolean isSupportLocation = true;
    protected int span = 1000;//定位间隔

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        if (isSupportLocation) {
            initLocation();
        }
    }

    protected void loc() {
        if (LocationUtils.isLocationEnabled()) {
            //定位权限
            PermissionsHelp.with(this).requestPermissions(new DefaultRequestPermissionsListener() {
                @Override
                public void grant() {
                    isFirstLocation = true;
                    locationState(LOCATION_START, null);
                    if (mLocationClient != null) {
                        if (mLocationClient.isStarted()) {
                            mLocationClient.requestLocation();
                        } else {
                            mLocationClient.start();
                        }
                    }
                }

            }, PermissionsHelp.ACCESS_FINE_LOCATION, PermissionsHelp.ACCESS_COARSE_LOCATION);
        } else {
            new MaterialDialog.Builder(this)
                    .title("位置服务")
                    .content("点击确定开启位置服务,否则无法定位")
                    .positiveText("确认")
                    .negativeText("取消")
                    .autoDismiss(true)
                    .canceledOnTouchOutside(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            LocationUtils.openGpsSettings();
                        }
                    })
                    .show();
        }
    }

    boolean isFirstLocation = true;

    protected void locationState(int state, BDLocation locaionInfo) {
        if (LOCATION_SUCCESS == state) {
            locationSuccess(locaionInfo);
        }
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); //百度坐标系,这个有点不太明白
        option.setIsNeedAddress(true); //需要地址
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //高精度定位
        option.setOpenGps(true);
        option.setScanSpan(isKeepLocation ? span : 0);  //0为定位一次,1000 为1s定位一次
        mLocationClient.setLocOption(option);
        if (isAutoLocation) {
            loc();
        }
    }

    protected abstract BaiduMap getMap();

    //定位成功
    protected void locationSuccess(BDLocation locaionInfo) {
        final BaiduMap map = getMap();
        if (map != null && locaionInfo != null) {
            map.setMyLocationEnabled(true);//开启定位图层
            MyLocationData locationData = new MyLocationData.Builder()
                    //.accuracy(locaionInfo.getRadius())
                    .accuracy(0)//设置为0，去除光环
                    .latitude(locaionInfo.getLatitude())
                    .longitude(locaionInfo.getLongitude())
                    .direction(locaionInfo.getDirection()).build();
            map.setMyLocationData(locationData);

            //将定位位置放到中央
            BaiduMapUtil.setCenter(
                    map,
                    new LatLng(locaionInfo.getLatitude(), locaionInfo.getLongitude()));
        }

    }

    @Override
    protected void onStop() {
        if (isSupportLocation && mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        super.onStop();
    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (isFirstLocation || isKeepLocation) {
            isFirstLocation = false;
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
                    bdLocation.getLocType() == BDLocation.TypeNetWorkLocation ||
                    bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// GPS定位结果
                locationState(LOCATION_SUCCESS, bdLocation);
            } else {
                if (bdLocation.getLocType() == BDLocation.TypeServerError) { //服务端网络定位失败
                    ToastyUtil.errorShort("定位失败，服务端网络定位失败");
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) { //网络不同导致定位失败，请检查网络是否通畅
                    ToastyUtil.errorShort("定位失败，请检查网络是否通畅");
                } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) { //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    ToastyUtil.errorShort("定位失败，请确保手机未处于飞行模式，或者重启手机");
                } else {
                    ToastyUtil.errorShort("定位失败,请重新定位,失败类型:" + bdLocation.getLocType());
                }
                locationState(LOCATION_ERROR, bdLocation);
            }

        }
    }
}
