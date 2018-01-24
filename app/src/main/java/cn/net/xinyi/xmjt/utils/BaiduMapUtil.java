package cn.net.xinyi.xmjt.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.TraceLocation;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.model.BaiduFence;
import cn.net.xinyi.xmjt.model.LocaionInfo;
import cn.net.xinyi.xmjt.model.MapDataModle;
import cn.net.xinyi.xmjt.model.XinyiLatLng;

/**
 * Created by studyjun on 2016/3/31.
 */
public class BaiduMapUtil {
    private static final String SK = "hMlahhhqqs4175BSDGOupUtSeeYjNBZ7";
//    public  static final String AK="dMB4WHGGhfGj1jPbVSSXgVdWxz9KuilX";
//    public  static final String GEOTABLE_ID="139079"; //LBS表格

    public static final String AK = "AtKcomnjaDayqzOEV7kOpgPwa76u2q7U";//web对应的ak
    public static final String GEOTABLE_ID = "139263"; //LBS表格
    public static final int SERVICE_ID = 114440;

    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_REPEAT = 3002;//插入重复的主健
    public static final int RESULT_CODE_NO_POI = 3003;//不存在该点


    public static LocaionInfo bdLocation2LocationInfo(BDLocation location) {
        LocaionInfo li = new LocaionInfo(location.getLatitude(), location.getLongitude(), location.getAddrStr(), location.getLocType());
        return li;
    }

    public static LatLng GCJ2Baidu09ll(LatLng gcj2Latlngt) {
        // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(gcj2Latlngt);
        return converter.convert();

    }


    public static LatLng GPS209ll(LatLng gcj2Latlngt) {
        // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(gcj2Latlngt);
        return converter.convert();
    }

    public static LatLng xinLatlngt2LatLngt(XinyiLatLng latLng) {
        return new LatLng(latLng.latitude, latLng.longitude);
    }

    public static Serializable mapDataModle2LocationInfo(MapDataModle obj) {
        LocaionInfo li = new LocaionInfo(Double.parseDouble(obj.getLatitude()), Double.parseDouble(obj.getLongitude()), obj.getAddress());
        return li;
    }


    /**
     * 获取百度sn，
     * map必须使用linkhashMap
     *
     * @param paramsMap
     * @param apiName
     * @return
     */
    public static String getSN(Map<String, String> paramsMap, String apiName) {
        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String paramsStr = null;
        try {
            paramsStr = toQueryString(paramsMap);
            // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
            String wholeStr = new String(apiName + paramsStr + SK);

            // 对上面wholeStr再作utf8编码
            String tempStr = URLEncoder.encode(wholeStr, "UTF-8");

            return MD5(tempStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

    // 对Map内所有value作utf8编码，拼接返回结果
    public static String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String) pair.getValue(),
                    "UTF-8") + "&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }


    /**
     * 判断调用lbs操作时候成功
     *
     * @param result
     * @return
     */
    public static boolean isOptionSuceess(String result) {
        try {
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("status") == 0) {
                return true;
            }
        } catch (JSONException e) {
            android.util.Log.d("BaiduUtil", e.getMessage());
        }
        return false;
    }


    /**
     * 0 success
     * 3002 唯一索引字段存在重复
     * -1 json error
     *
     * @param result
     * @return
     */
    public static int getResultCode(String result) {
        try {
            JSONObject jsonObject = JSON.parseObject(result);
            return jsonObject.getInteger("status");
        } catch (JSONException e) {
            android.util.Log.d("BaiduUtil", e.getMessage());
        }
        return -1;

    }

    public static LocaionInfo BDLocation2LocationInfo(BDLocation bdLocation) {
        if (bdLocation == null)
            return null;
        return new LocaionInfo(bdLocation.getLatitude(), bdLocation.getLongitude(), bdLocation.getAddrStr(), bdLocation.getRadius(), bdLocation.getDirection());
    }

    public static LocaionInfo traceLocation2LocationInfo(TraceLocation traceLocation) {
        if (traceLocation == null)
            return null;
        return new LocaionInfo(traceLocation.getLatitude(), traceLocation.getLongitude(), traceLocation.getBuilding(), (float) traceLocation.getRadius(), traceLocation.getDirection());
    }

    /**
     * 获取json类的地理围栏
     *
     * @param result
     * @return
     */
    public static List<BaiduFence> getFnces(String result) {
        try {
            JSONObject jsonObject = JSON.parseObject(result);
            return JSON.parseArray(jsonObject.getString("fences"), BaiduFence.class);
        } catch (JSONException e) {
            android.util.Log.d("BaiduUtil", e.getMessage());
        }
        return null;
    }
}
