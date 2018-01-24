package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseMapActivity;
import cn.net.xinyi.xmjt.model.PoliceBoxModle;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

public class PlcBxMapActivity extends BaseMapActivity {

    public final static int NORMAL = 1;
    public final static int NORMAL2 = 5;
    public final static int ABNORMAL = 2;

    private Map<Integer, List<PoliceBoxModle>> maps = new HashMap<Integer, List<PoliceBoxModle>>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoadding();
        getUserPlcBxList();

        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        setMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final PoliceBoxModle pb= (PoliceBoxModle) marker.getExtraInfo().getSerializable("data");
                //创建InfoWindow展示的view
                Button button = new Button(getApplicationContext());
                button.setTextColor(getResources().getColor(R.color.bthumbnail_font));
                if (pb.getZSRY()!=null){
                    button.setText(pb.getUNIFIEDNO()+"\n"+pb.getADDRESS()+"\n"+pb.getZSRY());
                }else {
                    button.setText(pb.getUNIFIEDNO()+"\n"+pb.getADDRESS());
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlcBxMapActivity.this).setMessage("拨打"+pb.getLINKMAN()+"-"+pb.getPHONENO()).setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                Uri data = Uri.parse("tel:" + pb.getPHONENO());
                                intent.setData(data);
                                startActivity(intent);
                            }
                        });

                    }
                });
                button.setBackgroundResource(R.drawable.map_pickup);
                //定义用于显示该InfoWindow的坐标点
                LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                //显示InfoWindow
                mMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });

    }

    /**
     * 设置岗亭标注
     * @param markers
     * @param drawableId
     */
    public void setPlcBxMarker(List<PoliceBoxModle> markers, int drawableId) {
        if (markers==null)
            return;
        for (PoliceBoxModle pb : markers) {
            if (pb.getLAT()<=0||pb.getLNGT()<=0)
                return;

            LatLng ll = new LatLng(pb.getLAT(), pb.getLNGT());

//            LatLng latLng = new LatLng(ll.latitude, ll.longitude);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(drawableId);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(ll)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Overlay overlay = mMap.addOverlay(option);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", pb);
            overlay.setExtraInfo(bundle);
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_bx_map);
    }

    private void getUserPlcBxList() {
        ApiResource.getGtList(getPlcSttnList(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                try {
                    List<PoliceBoxModle> list = JSON.parseArray(result, PoliceBoxModle.class);
                    if (list != null) {
                        for (PoliceBoxModle p : list) {

                            if (p.getLAT()<=0||p.getLNGT()<=0){
                                continue; //如果没有经纬度信息则过滤
                            }

                            // if (p.getZSZT() == NORMAL || p.getZSZT() == NORMAL2) {//值守状态的岗亭信息
                            if (Integer.valueOf(p.getZSZT()) > 0 ) {//值守状态的岗亭信息
                                if (maps.get(NORMAL) == null)
                                    maps.put(NORMAL, new ArrayList<PoliceBoxModle>());
                                maps.get(NORMAL).add(p);
                            } else {
                                if (maps.get(ABNORMAL) == null) //不在值守状态的岗亭
                                    maps.put(ABNORMAL, new ArrayList<PoliceBoxModle>());
                                maps.get(ABNORMAL).add(p);
                            }
                        }
                        setPlcBxMarker(maps.get(NORMAL), R.drawable.plc_bx_online);
                        setPlcBxMarker(maps.get(ABNORMAL), R.drawable.plc_bx_offline);
                    }
                } catch (JSONException e) {
                    toast("获取数据失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");

            }
        });
    }


    /**
     * 获取派出所下岗亭列表
     *
     * @return
     */
    private String getPlcSttnList() {
        JSONObject requestJson = new JSONObject();
        if (BaseDataUtils.isAdminCommit()){
            requestJson.put("","");
        }else {
            requestJson.put("POLICESTATION", userInfo.getPcs());
        }
        return requestJson.toJSONString();
    }

    @Override
    public boolean needKeepLoc() {
        return true;
    }
}

