package cn.net.xinyi.xmjt.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.Locale;

/**
 * Created by mazhongwang on 15/4/3.
 */
public class ApiSyncHttpClient extends ApiHttpClient {
    public static SyncHttpClient client;

    public ApiSyncHttpClient() {
    }

    public static void setHttpClient(SyncHttpClient c) {
        client = c;
        client.addHeader("Accept-Language", Locale.getDefault().toString());
        client.addHeader("Host", HOST);
        client.addHeader("Connection", "Keep-Alive");
        //setUserAgent(ApiClientHelper.getUserAgent(AppContext.instance()));
        //setUserAgent("OSChina.NET/1.0.0.4_29/Android/4.4.4/Nexus 4/1cd6bd26-fe78-4fbd-8bcf-1dd4d121ef1d");
    }

    public static SyncHttpClient getHttpClient() {
        return client;
    }

    public static void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }

    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    public static void setCookie(String cookie) {
        client.addHeader("Cookie", cookie);
    }

    public static void delete(final String partUrl, final AsyncHttpResponseHandler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.delete(getAbsoluteApiUrl(partUrl), handler);
                log(new StringBuilder("DELETE ").append(partUrl).toString());
            }
        });
    }

    public static void get(final String partUrl, final AsyncHttpResponseHandler handler) {
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                client.get(getAbsoluteApiUrl(partUrl), handler);
//                log(new StringBuilder("GET ").append(partUrl).toString());
//            }
//        });
        client.get(getAbsoluteApiUrl(partUrl), handler);
        log(new StringBuilder("GET ").append(partUrl).toString());
    }

    public static void get(final String partUrl, final RequestParams params,
                           final AsyncHttpResponseHandler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.get(getAbsoluteApiUrl(partUrl), params, handler);
                log(new StringBuilder("GET ").append(partUrl).append("&")
                        .append(params).toString());
            }
        });
    }

    public static void getDirect(final String url, final AsyncHttpResponseHandler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new AsyncHttpClient().get(url, handler);
                log(new StringBuilder("GET ").append(url).toString());
            }
        });

    }

    public static void post(final String partUrl, final AsyncHttpResponseHandler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.post(getAbsoluteApiUrl(partUrl), handler);
                log(new StringBuilder("POST ").append(partUrl).toString());
            }
        });

    }

    public static void post(final String partUrl, final RequestParams params,
                            final AsyncHttpResponseHandler handler) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                client.post(getAbsoluteApiUrl(partUrl), params, handler);
//                log(new StringBuilder("POST ").append(partUrl).append("&")
//                        .append(params).toString());
//            }
//        }).start();

        client.post(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("POST ").append(partUrl).append("&")
                .append(params).toString());
    }

    public static void put(final String partUrl, final AsyncHttpResponseHandler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.put(getAbsoluteApiUrl(partUrl), handler);
                log(new StringBuilder("PUT ").append(partUrl).toString());
            }
        });

    }

    public static void put(final String partUrl, final RequestParams params,
                           final AsyncHttpResponseHandler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.put(getAbsoluteApiUrl(partUrl), params, handler);
                log(new StringBuilder("PUT ").append(partUrl).append("&")
                        .append(params).toString());
            }
        });
    }

}