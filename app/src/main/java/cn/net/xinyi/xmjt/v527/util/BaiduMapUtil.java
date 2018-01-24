package cn.net.xinyi.xmjt.v527.util;

import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.List;

/**
 * Created by studyjun on 2016/3/31.
 */
public class BaiduMapUtil {

    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static final double pi = 3.14159265358979324;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

    /**
     * gg_lat 纬度
     * gg_lon 经度
     * GCJ-02转换BD-09
     * Google地图经纬度转百度地图经纬度
     */
    public static LatLng google_bd_encrypt(Double gg_lat, Double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLng(bd_lat, bd_lon);
    }

    public static void setCenter(BaiduMap map, LatLng latLng, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(zoom);
        map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public static void setCenter(BaiduMap map, LatLng latLng) {
        setCenter(map, latLng, 18);
    }


    /**
     * wgLat 纬度
     * wgLon 经度
     * BD-09转换GCJ-02
     * 百度转google
     */
    public static LatLng bd_google_encrypt(Double bd_lat, Double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLng(gg_lat, gg_lon);
    }

    /**
     * 设置简易的marker标注
     */
    public static void setMarkersByView(BaiduMap mMap, List<LatLng> points, List<View> views) {
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < points.size(); i++) {
            LatLng latLng = points.get(i);
            //构建Marker图标
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(views.get(i));
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .icon(bitmapDescriptor);
            //在地图上添加Marker，并显示
            Overlay overlay = mMap.addOverlay(option);
            overlay.setZIndex(i);
            if (overlay instanceof Marker) {
                builder.include(((Marker) overlay).getPosition());
            }
        }
    }


    public static void setMarkerByResid(BaiduMap mMap, LatLng point, Integer resids, int zIndex) {
        //构建Marker图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(resids);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .draggable(true)
                .icon(bitmapDescriptor);
        //在地图上添加Marker，并显示
        mMap.addOverlay(option).setZIndex(zIndex);
    }


    private static void setMarkersByResid(BaiduMap mMap, List<LatLng> points, List<Integer> resids) {
        for (int i = 0; i < points.size(); i++) {
            LatLng latLng = points.get(i);
            setMarkerByResid(mMap, latLng, resids.get(i), i);
        }
    }

}
