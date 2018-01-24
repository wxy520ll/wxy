package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.text.DecimalFormat;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseMapActivity;
import cn.net.xinyi.xmjt.model.LocaionInfo;


public class PickupMapActivity extends BaseMapActivity {

    public static final int MSG_MANUAL_LOC_SUCCESS = 1001;
    public static final int MAP_PICK_UP = 100;

    public GeoCoder mGeocoder;
    public TextView mAddress;
    public ImageView pickView;
    public ReverseGeoCodeResult mReverseGeoCodeResult;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGeocoder = GeoCoder.newInstance();
        mMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                mReverseGeoCodeResult=null;
                mAddress.setHint("获取地址中...");
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (mapStatus.zoom>10){
                    mAddress.setVisibility(View.VISIBLE);
                    getPickupCenterPoint(mapStatus.target);
                } else {
                    mAddress.setVisibility(View.GONE);
                }
            }
        });
        initPickView();
    }



    void initPickView() {
        pickView = new ImageView(this);
        pickView.setImageResource(R.drawable.map_pickup_nail);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        pickView.setId(R.id.tv_lx);
        pickView.setLayoutParams(params);
        getContentView().addView(pickView);

        mAddress = (TextView) LayoutInflater.from(this).inflate(R.layout.item_map_pickup_address,null);
        RelativeLayout.LayoutParams paramstV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramstV.addRule(RelativeLayout.ABOVE, pickView.getId());
        paramstV.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mAddress.setLayoutParams(paramstV);
        getContentView().addView(mAddress);

        mAddress.setBackgroundColor(Color.WHITE);
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddress.setHint("正在获取位置...");
                if (mReverseGeoCodeResult==null||mReverseGeoCodeResult.getLocation()==null){
                    toast("正在获取位置...");
                } else {
                    DecimalFormat format = new DecimalFormat("##0.00000000");//保留6位小数
                    LocaionInfo loc = new LocaionInfo(Double.parseDouble(format.format(mReverseGeoCodeResult.getLocation().latitude)),Double.parseDouble(format.format(mReverseGeoCodeResult.getLocation().longitude)),mReverseGeoCodeResult.getAddress(),999);
                    // LocaionInfo loc = new LocaionInfo(mReverseGeoCodeResult.getLocation().latitude,mReverseGeoCodeResult.getLocation().longitude,mReverseGeoCodeResult.getAddress(),999);
                    if (onAddressPickEvent(loc)){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", loc);
                        intent.putExtra("data", bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

    }


    /**
     * 获取位置
     * @param point
     */
    void getPickupCenterPoint(LatLng point) {

        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
        op.location(point);
        mGeocoder.reverseGeoCode(op);
        mGeocoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                mAddress.setText(reverseGeoCodeResult.getAddress());
                mReverseGeoCodeResult =reverseGeoCodeResult;

            }
        });

    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }
    @Override
    public String getAtyTitle() {
        return getString(R.string.map_sddw);
    }

    /**
     * false会拦截，不会继续执行
     * true 会执行完毕会继续执行addresspick操作
     * @return
     * @param loc
     */
    public boolean onAddressPickEvent(LocaionInfo loc){
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
