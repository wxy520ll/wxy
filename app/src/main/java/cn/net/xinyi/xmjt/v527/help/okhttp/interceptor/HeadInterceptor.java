package cn.net.xinyi.xmjt.v527.help.okhttp.interceptor;


import com.blankj.utilcode.util.Utils;

import java.io.IOException;

import cn.net.xinyi.xmjt.api.ApiHttpClient;
import cn.net.xinyi.xmjt.config.AppContext;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhiren.zhang on 2017/10/16.
 */

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        final String cookie = ApiHttpClient.getCookie((AppContext) Utils.getApp());
        if (cookie != null) {
            requestBuilder.addHeader("Cookie", cookie);
        }
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
