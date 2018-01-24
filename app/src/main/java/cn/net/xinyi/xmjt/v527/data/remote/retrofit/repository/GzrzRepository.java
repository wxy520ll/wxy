package cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.List;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.v527.base.model.ResponseData;
import cn.net.xinyi.xmjt.v527.config.Constants;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzPageModel;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzTypeModel;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by jiajun.wang on 2017/12/30.
 */

public class GzrzRepository {

    public Observable<List<String>> uploadFile(JSONObject jsonObject) {
        String path1 = jsonObject.getString("ATT_ID");
        String path2 = jsonObject.getString("FILE_ID");
        String path3=jsonObject.getString("FILE_ID2");
        String path4=jsonObject.getString("FILE_ID3");
        return Observable.just(path1 + Constants.TAG + "1", path2 + Constants.TAG + "2",
                path3 + Constants.TAG + "3",path4+ Constants.TAG + "4")
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return new File(s.split(Constants.TAG)[0]).exists() ? true : false;
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return logAdd(s.split(Constants.TAG)[0], s.split(Constants.TAG)[1]);
                    }
                }).toList();
    }

    public Observable<String> logAdd(String path, String number) {
        File file = new File(path);             // 需要上传的文件
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploadedFile", file.getName(), requestFile);
        RequestBody params = RequestBody.create(MediaType.parse("text/plain"), number);
        return AppContext.apiService.uploadFile1("log", body, params).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                if ("fail".equalsIgnoreCase(s)) {
                    return Observable.error(new Throwable("图片上传失败"));
                } else {
                    return Observable.just(s);
                }
            }
        });
    }

    public Observable<String> uoloadJson(JSONObject jsonObject) {
        return AppContext.apiService.logAdd(jsonObject);
    }
    public Observable<String> updateJson(JSONObject jsonObject) {
        return AppContext.apiService.logUpdate(jsonObject);
    }

    public Observable<List<GzrzTypeModel>> getDictionary() {
        Observable<List<GzrzTypeModel>> listObservable = AppContext.apiService.requestDictionary();
        return AppContext.cacheProviders.getDict(listObservable, new DynamicKey("gzrz"), new EvictDynamicKey(false))
                .flatMap(new Func1<Reply<List<GzrzTypeModel>>, Observable<List<GzrzTypeModel>>>() {
                    @Override
                    public Observable<List<GzrzTypeModel>> call(Reply<List<GzrzTypeModel>> listReply) {
                        return Observable.just(listReply.getData());

                    }
                });
    }


    public Observable<List<GzrzPageModel>> getGzrzPageData(JSONObject jsonObject) {
        return AppContext.apiService.getPageData(jsonObject)
                .flatMap(new Func1<ResponseData<JSONObject>, Observable<List<GzrzPageModel>>>() {
                    @Override
                    public Observable<List<GzrzPageModel>> call(ResponseData<JSONObject> jsonObjectResponseData) {
                        String s = jsonObjectResponseData.getResult().getString("list");
                        List<GzrzPageModel> objects = JSON.parseArray(s, GzrzPageModel.class);
                        return Observable.just(objects);
                    }
                });
    }
}
