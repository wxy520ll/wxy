package cn.net.xinyi.xmjt.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
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

import java.math.BigDecimal;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.MapDataModle;

public class GetLocation {
	/** *定位失败默认的位置  龙岗中心*/
	public static double DEFAULT_LATITUDE = 22.726214;
	public static double DEFAULT_LONGITUDE = 114.254215;
	//地图级别
	private static int zoomLevel = 21;
	/** *百度地图定位的 基本参数信息*/
	private static String tempcoor = "bd09ll";
	private static int span = 5000;
	private static LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
	private Context context;
	private LocationClient locationClient;
	private Handler handler;
	private int networkType;

	private Marker mCurrentMarker;
	private BigDecimal map_latitude;
	private BigDecimal map_longitude;
	private GeoCoder geoCoder;
	private String address;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);


	public GetLocation(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}

	public GetLocation(Context context) {
		super();
		this.context = context;
	}


	/***百度地图初始化获得位置信息*/
	public void startLocation() {
		locationClient = new LocationClient(context);
		locationClient.registerLocationListener(new LocationListener());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		locationClient.setLocOption(option);
		locationClient.start();
		locationClient.requestLocation();
	}


	private class LocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			Message msg = handler.obtainMessage();
			msg.what = -100;
			msg.obj = bdLocation;
			handler.sendMessage(msg);
			locationClient.unRegisterLocationListener(this);
			locationClient.stop();
		}
	}


	/**
	 * 地图定位
	 * 用于地图模式下  在百度上标注当前的位置  如果没有获取到坐标则默认龙岗中心
	 * @param location
	 * @param mBaiduMap
	 */
	public void mapLocation(BDLocation location, BaiduMap mBaiduMap) {
		LatLng ll = null;
		if (location!=null) {
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			ll = new LatLng(location.getLatitude(),location.getLongitude());
		} else {
			ll = new LatLng(DEFAULT_LATITUDE,DEFAULT_LONGITUDE);
		}
		MapStatus mMapStatus = new MapStatus.Builder().target(ll)
				.zoom(zoomLevel).build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.animateMapStatus(u);
	}




	/**
	 * 百度地图运用  手动定位获得位置信息
	 * @param mBaiduMap
	 */
	public void initMapView(final BaiduMap mBaiduMap, final Handler handler) {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		final MapDataModle mapData=new MapDataModle();
		//点击定位图标，获取地址
		mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				networkType = AppContext.instance().getNetworkType();
				mBaiduMap.clear();
				OverlayOptions oo = new MarkerOptions().position(latLng).icon(bd).title(latLng.latitude + "," + latLng.longitude).zIndex(9).draggable(true);
				mCurrentMarker = (Marker) (mBaiduMap.addOverlay(oo));
				View viewCatch = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.pop_layout, null);
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

				mButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mapData.setLoctype("999");
						mapData.setAddress(address);
						mapData.setLongitude("" + map_longitude);
						mapData.setLatitude("" + map_latitude);

						Message msg = new Message();
						msg.what = 1001;
						Bundle b = new Bundle();
						b.putSerializable("mapData", mapData);
						msg.setData(b);
						handler.sendMessage(msg);

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
}
