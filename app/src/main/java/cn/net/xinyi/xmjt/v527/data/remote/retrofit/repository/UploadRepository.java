package cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xinyi_tech.comm.constant.CommConstant;

import java.io.File;
import java.util.Date;

import cn.net.xinyi.xmjt.config.AppContext;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Fracesuit on 2017/12/28.
 */

public class UploadRepository {
    public Observable<String> uploadFile(String urlPath, final String path) {
        final File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploadedFile", file.getName(), requestFile);
        return AppContext.apiService.uploadFile(urlPath, body)
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean b) {
                        if (b) {
                            return TimeUtils.date2String(new Date(), CommConstant.DeteFromat.DEFAULT_FORMAT_DAY) + "/" + FileUtils.getFileName(path);
                        } else {
                            return null;
                        }
                    }
                });
    }

    public Observable<String> uploadFileForString(String urlPath, final String path) {
        File file = new File(path);             // 需要上传的文件
        final String fileNameNoExtension = FileUtils.getFileNameNoExtension(file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploadedFile", file.getName(), requestFile);
        RequestBody params = RequestBody.create(MediaType.parse("text/plain"), fileNameNoExtension);
        return AppContext.apiService.uploadFile1(urlPath, body, params).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                if ("fail".equalsIgnoreCase(s)) {
                    return Observable.error(new Throwable("图片上传失败"));
                } else {
                    return Observable.just(JSONObject.parseObject(s).getString(fileNameNoExtension));
                }
            }
        });
    }
}
