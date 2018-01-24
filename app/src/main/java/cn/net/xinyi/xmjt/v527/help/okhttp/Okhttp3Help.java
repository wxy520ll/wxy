package cn.net.xinyi.xmjt.v527.help.okhttp;


import cn.net.xinyi.xmjt.v527.help.okhttp.interceptor.HeadInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by zhiren.zhang on 2017/10/16.
 */

public class Okhttp3Help {

    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HeadInterceptor());
        return builder;
    }
}
