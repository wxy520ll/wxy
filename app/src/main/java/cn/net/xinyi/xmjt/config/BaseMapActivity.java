package cn.net.xinyi.xmjt.config;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.model.LocaionInfo;


public abstract class BaseMapActivity extends BaseWithLocActivity {


    public TextureMapView mMapView;

    public BaiduMap mMap;

    public GeoCoder mGeoCoder;



    public LinearLayout mFooterView;

    public LocaionInfo mMyLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //添加底部布局
        mFooterView=(LinearLayout)findViewById(R.id.map_footer);
        setupFooterView(mFooterView);
        mMapView= (TextureMapView) findViewById(R.id.map_baidumap);
        mMap = mMapView.getMap();
        mMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); //基础地
        CoordinateConverter converter = new CoordinateConverter();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(22.61667, 114.06667)).zoom(19.0f); //定位深圳
        mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        /**
         * 手动定位
         */
        findViewById(R.id.map_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstLoc=true; //定位完成，需要移动到屏幕中央
                loc();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    /**
     * 清除地图覆盖物
     */
    public void clearMarker(){
        mMap.clear();
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if (mGeoCoder!=null){
            mGeoCoder.destroy();
            mGeoCoder =null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }




    /**
     * 接受到定位
     * @param location
     * @param isSuccess
     * @param errMsg
     */
    @Override
    public void onReceiveLoc(LocaionInfo location, boolean isSuccess, Throwable errMsg) {
        if (isSuccess && null !=mMap){
            mMyLocation = location;
            mMap.setMyLocationEnabled(true);
            MyLocationData locationData = new MyLocationData.Builder().accuracy(location.radius).latitude(location.lat).longitude(location.longt).direction(location.getDirection()).build();
            mMap.setMyLocationData(locationData);
            if (isFirstLoc){
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(new LatLng(locationData.latitude,locationData.longitude)).zoom(19.0f);
                mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                onFirstLoc();
                isFirstLoc=false;
            }
        }
    }

    protected  void onFirstLoc(){

    }

    /**
     * 设置footview
     * @param parent
     */
    public void setupFooterView(LinearLayout parent){

    }

    /**
     * 设置地图中心
     * @param latLng
     */
    public void setCenter(LatLng latLng){
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(21.0f);
        mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
    /**
     * 设置地图中心
     * @param latLng
     */
    public void setCenter(LatLng latLng, float f){
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(f);
        mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    public void setMarker(LatLng latLng, int drawableId){
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(drawableId);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mMap.addOverlay(option);
    }


    /**
     * 设置带bundle的Marker
     * @param latLng
     * @param drawableId
     * @param bundle
     */
    public void setMarker(LatLng latLng, int drawableId, Bundle bundle){
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(drawableId);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
        //在地图上添加Marker，并显示
        Overlay o=mMap.addOverlay(option);
        o.setExtraInfo(bundle);

    }


    /**
     * 设置简易的marker标注
     * @param points
     * @param drawableId
     */
    public void setMarkers(List<LatLng> points, int drawableId){
        for (LatLng ll:points){
            LatLng latLng = new LatLng(ll.latitude,ll.longitude);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(drawableId);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mMap.addOverlay(option);
        }
    }

    /**
     * 批量设置带bundle的marker
     * bundle.size必须等于points.size
     * @param points
     * @param drawableId
     * @param bundles
     */
    public void setMarkers(List<LatLng> points , int drawableId, List<Bundle> bundles){
        if (points==null||bundles==null)
            return;
        if (points.size()!=bundles.size())
            return;
        for (int i=0;i<points.size();i++){
            LatLng ll = points.get(i);
            LatLng latLng = new LatLng(ll.latitude,ll.longitude);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(drawableId);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmap);

            //在地图上添加Marker，并显示
            Overlay o = mMap.addOverlay(option);
            o.setExtraInfo(bundles.get(i));
        }
    }

    /**
     * 设置marker点击事件
     * @param listener
     */
    public void setMarkerClickListener(BaiduMap.OnMarkerClickListener listener){
        mMap.setOnMarkerClickListener(listener);
    }



    /**
     * 获取主容器
     * @return
     */
//    LinearLayout getCOntentView(){
//        return (FrameLayout) findViewById(R.id.base_map_content_view);
//    }

    /**
     * 获取主容器
     * @return
     */
    public RelativeLayout getContentView(){
        return (RelativeLayout) findViewById(R.id.base_map_content_view_relavtive);
    }
}
