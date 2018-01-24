package cn.net.xinyi.xmjt.config;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.utils.BaiduMapUtil;

/**
 * 带定位的基类activity
 */
public abstract class BaseWithLocActivity extends BaseActivity2 {

    LocationClient mLocationClient;
    BDLocation mLocation;
    int mSpan = 1000;
    boolean isFirstLoc=true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //地图流程 1，开始地图，2，开启定位，3，定位成功，4，开启定位图层
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new BaseLocationListener());
        initLocationOptions(); //初始化定位参数
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            if (needAutoLoc()) {
                if (isFirstLoc)
                    mLocationClient.start();
            }
        }
    }

    @Override
    public void onStop() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        super.onStop();
    }

    /**
     * 初始化定位服务
     * 使用是国家坐标系
     */
    public void initLocationOptions() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); //百度坐标系
        option.setIsNeedAddress(true); //需要地址
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //高精度定位
        option.setOpenGps(true);
        if (needKeepLoc()) {
            option.setScanSpan(mSpan);  //持续定位
        } else {
            option.setScanSpan(0);  //0为定位一次
        }
        mLocationClient.setLocOption(option);
    }


    /**
     * 百度定位回调
     */
    public class BaseLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            stopLoading();
            if (!needKeepLoc())
                mLocationClient.stop();
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                mLocation = location;
                onReceiveLoc(BaiduMapUtil.bdLocation2LocationInfo(location), true, null);
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                mLocation = location;
                onReceiveLoc(BaiduMapUtil.bdLocation2LocationInfo(location), true, null);
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                mLocation = location;
                onReceiveLoc(BaiduMapUtil.bdLocation2LocationInfo(location), true, null);
            } else if (location.getLocType() == BDLocation.TypeServerError) { //服务端网络定位失败
                onReceiveLoc(BaiduMapUtil.bdLocation2LocationInfo(location), false, new Throwable("服务端网络定位失败"));
                mLocation = null;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) { //网络不同导致定位失败，请检查网络是否通畅
                onReceiveLoc(BaiduMapUtil.bdLocation2LocationInfo(location), false, new Throwable("请检查网络是否通畅"));
                mLocation = null;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) { //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                mLocation = null;
                onReceiveLoc(BaiduMapUtil.bdLocation2LocationInfo(location), false, new Throwable("请确保手机未处于飞行模式，或者重启手机"));

            }
            isFirstLoc=false;
        }
    }

    /**
     * 获取定位详情
     *
     * @return
     */
    public BDLocation getLocation() {
        return mLocation;
    }

    public abstract void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg);

    /**
     * 是否要保持定位
     *
     * @return
     */
    public boolean needKeepLoc() {
        return false;
    }

    /**
     * 定位间隔
     * 单位 ms
     * 当needkeeploc为ture时有效
     * 默认1000ms
     */
    public void setLocSpan(int time) {
        mSpan = time;
    }

    /**
     * 获取定位
     */
    public void loc() {
        showLoadding("定位中...",BaseWithLocActivity.this);
        mLocationClient.start();
    }

    /**
     * 是否需要自动定位
     *
     * @return
     */
    public boolean needAutoLoc() {
        return true;
    }



}
