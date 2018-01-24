package cn.net.xinyi.xmjt.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mazhongwang on 15/6/29.
 */
public class RestUtils {

    public static String postJson(String urlStr,String json){
        // 获取请求的返回值
        String strResultData = "";
        // 网络请求
        InputStream in = null;
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(urlStr);
            // 设置属性
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(50000);
            httpConn.setReadTimeout(50000);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setChunkedStreamingMode(0);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            httpConn.setRequestProperty("Accept", "application/json;charset=utf-8");

            OutputStream outStream = httpConn.getOutputStream();
            outStream.write("{\"cjsjjs\":\"2015-06-30\"}".getBytes());
            //outStream.write(json.getBytes());
            in = httpConn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"), 1024 * 10);
            char[] bufferChar = new char[1024 * 10];
            int nlenmsg = 0;
            while ((nlenmsg = br.read(bufferChar, 0, 1024 * 10)) != -1) {
                strResultData += new String(bufferChar, 0, nlenmsg);
                bufferChar = new char[1024 * 10];
            }
        }catch (Exception e){
            strResultData = "-1";
        }
        return strResultData;
    }
}
