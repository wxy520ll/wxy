package cn.net.xinyi.xmjt.api;

import android.content.Context;
import android.util.Log;

import cn.net.xinyi.xmjt.config.AppContext;

/**
 * Created by mazhongwang on 15/4/3.
 */
public class ApiHttpClient {


    //测试环境：183.62.140.8:8080
    public final static String HOST = "183.62.140.8:8080";
    public final static String IMAGE_HOST = "http://183.62.140.8:8080";
    public static String DEV_API_URL = "http://183.62.140.8:8080/xsmws-web/api/v1.0%s";

    // 正式环境：219.134.134.156:8088
    /*public final static String HOST = "http://219.134.134.156:8088";
    public final static String IMAGE_HOST = "http://219.134.134.156:8089";
    public static String DEV_API_URL = "http://219.134.134.156:8088/xsmws-web/api/v1.0%s";*/

    public static String API_URL;
    public static String ASSET_URL = "http://media-cache.pinterest.com/%s";
    public static String ATTRIB_ASSET_URL = "http://passets-ec.pinterest.com/%s";
    public static String DEFAULT_API_URL;
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    //58.61.148.26:60(旧正式环境)
    static {
        API_URL = DEV_API_URL;
    }

    private static String appCookie;

    public static void cleanCookie() {
        appCookie = "";
    }

    public static String getCookie(AppContext appContext) {
        if (appCookie == null || appCookie == "") {
            appCookie = appContext.getProperty("cookie");
        }
        return appCookie;
    }

    public static void resetApiUrl() {
        setApiUrl(DEFAULT_API_URL);
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    public static void log(String log) {
        Log.d("BaseApi", log);
    }

    public static String getAbsoluteApiUrl(String partUrl) {
        String url = String.format(API_URL, partUrl);
        Log.d("BASE_CLIENT", "request:" + url);
        return url;
    }

    public static String getApiUrl() {
        return API_URL;
    }

    public static String getAssetUrl(String url) {
        if (url.indexOf("/") == 0)
            url = url.substring(1);
        if (!url.contains("http")) {
            url = String.format(ASSET_URL, url);
        }
        return url;
    }

    public static String getAttributionAssetUrl(String url) {
        if (url.indexOf("/") == 0)
            url = url.substring(1);
        return String.format(ATTRIB_ASSET_URL, url);
    }

    public static void clearUserCookies(Context context) {
        cleanCookie();
    }

}
