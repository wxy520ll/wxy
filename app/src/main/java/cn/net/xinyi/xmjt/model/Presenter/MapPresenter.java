package cn.net.xinyi.xmjt.model.Presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;

import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.model.View.IMapDataView;
import cn.net.xinyi.xmjt.utils.GetLocation;

/**
 * Created by hao.zhou on 2016/1/29.
 */
public class MapPresenter {
    private IMapDataView mapView;
    private MapDataModle mapData;

    public MapPresenter(IMapDataView View) {
        this.mapView = View;
        mapData=new MapDataModle();
    }


    /***
     * 获取百度地图位置信息
     * */
    public void setMapData(Activity aty) {
        /** *百度地图请求当前位置信息  */
        new GetLocation(aty, mHandle).startLocation();
    }


    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -100:
                    BDLocation location = (BDLocation) msg.obj;
                    mapView.setLoction(location);
//                    /**获取地址信息**/
//                    mapView.setbWz(location.getAddrStr());
//                    /**百度纬度**/
//                    mapView.setbWd(location.getLatitude());
//                    /**百度经度**/
//                    mapView.setbJd(location.getLongitude());
                    break;
                default:
                    break;
            }
        }
    };
}
