package cn.net.xinyi.xmjt.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Locale;

public class  ApiAsyncHttpClient  extends ApiHttpClient{

	public static AsyncHttpClient client;


	public ApiAsyncHttpClient() {
	}


	public static void setHttpClient(AsyncHttpClient c) {
		client = c;
		client.addHeader("Accept-Language", Locale.getDefault().toString());
		client.addHeader("Host", HOST);
		client.addHeader("Connection", "Keep-Alive");
		client.setTimeout(50000);
		//setUserAgent(ApiClientHelper.getUserAgent(AppContext.instance()));
		//setUserAgent("OSChina.NET/1.0.0.4_29/Android/4.4.4/Nexus 4/1cd6bd26-fe78-4fbd-8bcf-1dd4d121ef1d");
	}

	public static AsyncHttpClient getHttpClient() {
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

	public static void delete(String partUrl, AsyncHttpResponseHandler handler) {
		client.delete(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("DELETE ").append(partUrl).toString());
	}

	public static void get(String partUrl, AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("GET ").append(partUrl).toString());
	}

	public static void get(String partUrl, RequestParams params,
						   AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("GET ").append(partUrl).append("&")
				.append(params).toString());
	}

	public static void getDirect(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		new AsyncHttpClient().get(url,params, handler);
		log(new StringBuilder("GET ").append(url).toString());
	}

	public static void getDirect(String url, AsyncHttpResponseHandler handler) {
		new AsyncHttpClient().get(url, handler);
		log(new StringBuilder("GET ").append(url).toString());
	}

	public static void post(String partUrl, AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("POST ").append(partUrl).toString());
	}

	public static void post(String partUrl, RequestParams params,
							AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("POST ").append(partUrl).append("&")
				.append(params).toString());
	}

	public static void put(String partUrl, AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("PUT ").append(partUrl).toString());
	}

	public static void put(String partUrl, RequestParams params,
						   AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("PUT ").append(partUrl).append("&")
				.append(params).toString());
	}

	public static void postDirect(String absoluteApi, RequestParams params,
								  AsyncHttpResponseHandler handler) {
		client.post(absoluteApi, params, handler);
		log(new StringBuilder("Post ").append(absoluteApi).append("&")
				.append(params).toString());
	}

	public static void postPDF(String pdfUri, AsyncHttpResponseHandler handler) {
		client.post(pdfUri, handler);
		log(new StringBuilder("POST ").append(pdfUri).toString());
	}

}
