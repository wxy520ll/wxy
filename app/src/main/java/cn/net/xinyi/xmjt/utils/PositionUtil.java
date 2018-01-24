package cn.net.xinyi.xmjt.utils;

import android.content.Context;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/** 
 * 坐标定位工具包
 * @author nodeny
 * @version 1.0
 * @created 2012-11-21
 */
public class PositionUtil {
	public static final String BAIDU_MAP_WEB_KEY = "B97e50b4c64267db56505626fd58dfbe";
	
	
	public PositionUtil(Context context) {
		this.context = context;
	}
	
	Context context;
	public class CellIDInfo {

		public int cellId;
		public String mobileCountryCode;
		public String mobileNetworkCode;
		public int locationAreaCode;
		public String radioType;

	}

	/**
	 * 获取基站信息
	 * @return
	 */
	public ArrayList<CellIDInfo> getCallId() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = tm.getNetworkType();
		String OperatorName = tm.getNetworkOperatorName();
		ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
			try {
				//OperatorName.equals("中国电信")
				CdmaCellLocation location = (CdmaCellLocation) tm.getCellLocation();
				int cellIDs = location.getBaseStationId();
				int networkID = location.getNetworkId();
				StringBuilder nsb = new StringBuilder();
				nsb.append(location.getSystemId());
				CellIDInfo info = new CellIDInfo();
				info.cellId = cellIDs;
				info.locationAreaCode = networkID; // ok
				info.mobileNetworkCode = nsb.toString();
				info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
				info.radioType = "cdma";
				CellID.add(info);
			}catch (Exception e){
				//OperatorName.equals("中国移动") || OperatorName.equals("中国联通")
				GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
				int cellIDs = location.getCid();
				int lac = location.getLac();
				CellIDInfo info = new CellIDInfo();
				info.cellId = cellIDs;
				info.locationAreaCode = lac;
				info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);
				info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
				info.radioType = "gsm";
				CellID.add(info);
			}
		return CellID;
	}



	
	/**
	 * 根据坐标获取地理位置(反地理信息编码）
	 * 
	 * @throws Exception
	 */
	public String getLocation(Location itude) throws Exception {
		String location = "";
		String resultString = "";
		String key = BAIDU_MAP_WEB_KEY;
		String pois = "0";
		String output = "json";
		String coordtype = "wgs84ll";

		/** 这里采用get方法，直接将参数加到URL上 */
		String urlString = String.format(
				"http://api.map.baidu.com/geocoder/v2/?ak=%s&coordtype=%s&location=%s,%s&output=json&pois=%s",
				key,coordtype,itude.getLatitude(), itude.getLongitude(),pois);
		Log.i("URL", urlString);

		/** 新建HttpClient */
		HttpClient client = new DefaultHttpClient();
		/** 采用GET方法 */
		HttpGet get = new HttpGet(urlString);
		try {
			/** 发起GET请求并获得返回数据 */
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			
			/** 解析JSON数据，获得物理地址 */
			if (resultString != null && resultString.length() > 0) {
				JSONObject root = new JSONObject(resultString);
				if(root!=null && root.getString("status").equals("0")){
					String resultJson = root.getString("result");
					if(resultJson !=null && resultJson.length()>0){
						location = new JSONObject(resultJson).getString("formatted_address");
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("获取物理位置出现错误:" + e.getMessage());
		} finally {
			get.abort();
			client = null;
		}

		return location;
	}

	//	获取EditText的坐标   点击空白部分隐藏
	public static boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			//获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			return !(event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom);
		}
		return false;
	}
	
}
