package cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinyi_tech.comm.picker.picture.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.utils.StringUtils;
import cn.net.xinyi.xmjt.v527.config.Constants;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjModel;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjQueryCondition;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjXcModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Fracesuit on 2018/1/19.
 */

public class ZacsCjRepository {
    public Observable<List<ZacjModel>> getZajcxxlistByQueryNew(ZacjQueryCondition zacjQueryCondition) {
        return AppContext.apiService.getZajcxxlistByQueryNew(zacjQueryCondition)
                .map(new Func1<JSONObject, List<ZacjModel>>() {
                    @Override
                    public List<ZacjModel> call(JSONObject s) {
                        try {
                            final String o = JSONObject.parseObject(s.getString("result"), JSONObject.class).getString("list");
                            return JSONObject.parseArray(o, ZacjModel.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new ArrayList<ZacjModel>();
                        }
                    }
                });
    }


    public Observable<Boolean>updateAddress(ZacjModel zacjModel){
         JSONObject jsonObject=new JSONObject();
        jsonObject.put("ID",zacjModel.getID());
        jsonObject.put("NAME",zacjModel.getNAME());
        jsonObject.put("JD",zacjModel.getJD());
        jsonObject.put("WD",zacjModel.getWD());
        return AppContext.apiService.updateAddress(jsonObject);
    }
    public Observable<List<ZacjXcModel>>queryZacsList(JSONObject jsonObject){
        return  AppContext.apiService.queryZajc(jsonObject)
                    .flatMap(new Func1<JSONObject, Observable<List<ZacjXcModel>>>() {
                        @Override
                        public Observable<List<ZacjXcModel>> call(JSONObject jsonObject) {
                            if (jsonObject.getString("status").equals("0")){
                                String s=jsonObject.getJSONObject("result").getString("list");
                                List<ZacjXcModel> objects= JSONArray.parseArray(s,ZacjXcModel.class);
                                return Observable.just(objects);
                            }else {
                                return Observable.error(new Throwable("数据获取失败"));
                            }
                        }
                    });
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

    public Observable<List<String>> uploadFile(HashMap<List<LocalMedia>,Integer> map, String qm){
        List <String>paths=new ArrayList<>();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry <List<LocalMedia>,Integer> entry = (Map.Entry) iter.next();
            int value=entry.getValue();
            List<LocalMedia> medias=entry.getKey();
            for (LocalMedia l:medias
                 ) {
                paths.add(l.getCompressPath()+"////"+value);
            }
        }
        if(!StringUtils.isEmpty(qm)){
            paths.add(qm+"////5");
        }
        return  Observable.from(paths)
                  .filter(new Func1<String, Boolean>() {
                      @Override
                      public Boolean call(String s) {
                          if (StringUtils.isEmpty(s)){
                              return false;
                          }else {
                              return true;
                          }
                      }
                  })
                  .flatMap(new Func1<String, Observable<String>>() {
                      @Override
                      public Observable<String> call(String s) {
                          return logAdd(s.split(Constants.TAG)[0], s.split(Constants.TAG)[1]);
                      }
                  }).toList();
    }


    public Observable<JSONObject>uploadZajc(JSONObject jsonObject){

        return AppContext.apiService.addZajc(jsonObject);
    }

    public Observable<Boolean>addJson(JSONObject jsonObject){
        return AppContext.apiService.addJson(jsonObject);
    }
}
