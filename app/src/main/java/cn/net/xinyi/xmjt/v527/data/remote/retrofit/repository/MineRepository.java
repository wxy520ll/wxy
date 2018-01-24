package cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;

import cn.net.xinyi.xmjt.config.AppContext;
import rx.Observable;
import rx.functions.Func1;

import static cn.net.xinyi.xmjt.config.AppContext.apiService;


/**
 * Created by Fracesuit on 2017/12/28.
 */

public class MineRepository {
    public Observable<String> updateUseInfo(final JSONObject jsonObject) {
        final String txzp = jsonObject.getString("TXZP");
        Observable<JSONObject> observable;
        if (!StringUtils.isEmpty(txzp)) {
            observable = new UploadRepository().uploadFile("user", txzp)
                    .flatMap(new Func1<String, Observable<JSONObject>>() {
                        @Override
                        public Observable<JSONObject> call(String s) {
                            if (s != null) {
                                jsonObject.put("TXZP", s);
                                return Observable.just(jsonObject);
                            } else {
                                return Observable.error(new Throwable("图片上传失败"));
                            }
                        }
                    }).flatMap(new Func1<JSONObject, Observable<JSONObject>>() {
                        @Override
                        public Observable<JSONObject> call(final JSONObject jsonObject) {
                            return AppContext.apiService.updateImage(jsonObject).flatMap(new Func1<Boolean, Observable<JSONObject>>() {
                                @Override
                                public Observable<JSONObject> call(Boolean s) {
                                    if (s) {
                                        return Observable.just(jsonObject);
                                    } else {
                                        return Observable.error(new Throwable("图片更新失败"));
                                    }
                                }
                            });

                        }
                    });
        } else {
            observable = Observable.just(jsonObject);
        }
        return observable.flatMap(new Func1<JSONObject, Observable<String>>() {
            @Override
            public Observable<String> call(JSONObject jsonObject) {
                return apiService.updateUseInfo(jsonObject).map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        if ("1".equals(s)) {
                            return "1";
                        } else {
                            return null;
                        }
                    }
                });
            }
        });

    }
}
