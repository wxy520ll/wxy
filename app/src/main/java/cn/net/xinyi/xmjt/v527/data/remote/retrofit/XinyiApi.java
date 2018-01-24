package cn.net.xinyi.xmjt.v527.data.remote.retrofit;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.net.xinyi.xmjt.BuildConfig;
import cn.net.xinyi.xmjt.v527.base.model.ResponseData;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.CsxcEntity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.GzcxEntity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.ZajcEntity;
import cn.net.xinyi.xmjt.v527.presentation.gzrz.model.GzrzTypeModel;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.mode.RycjOtherModel;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjQueryCondition;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.ZajcModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zhiren.zhang on 2017/9/21.
 */

public interface XinyiApi {

    String comm = "/xsmws-web/api/v1.0/";
    //证件识别接口
    String URL_ROOT = "http://" + BuildConfig.HOST + ":" + BuildConfig.PORT + comm + "%s";

    @POST(comm + "yajie")
    Observable<String> getInfoByCommand(@Body JSONObject jsonObject);

    @POST("/sessions/phoneValid")
    Observable<String> loginByScan(@Body JSONObject jsonObject);


    @POST(comm + "user/getTxllAndSubDeptByQuery")
        //{"ORGCODE":"440307000000"}//440307000000
    Observable<ResponseData<String>> getTxl(@Body JSONObject jsonObject);

    @POST(comm + "shouye/tongji/distanceAndRypcAndPlate")
    Observable<List<GzcxEntity>> distanceAndRypcAndPlate(@Body JSONObject jsonObject);

    //获取警情数据
    @POST(comm + "jq/getJqlistByQuery")
    /**
     * {"KSSJ":"2017-12-28 00:00:00","JSSJ":"2017-12-29 00:00:00",PAGENUMBER:1,PAGESIZE:10}
     */
    Observable<ResponseData<JSONObject>> getJqlistByQuery(@Body JSONObject jsonObject);


    //附件上传
    @Multipart
    @POST(comm + "{path}/upload")
    Observable<Boolean> uploadFile(@Path("path") String url, @Part MultipartBody.Part file);


    //附件上传
    @Multipart
    @POST(comm + "{path}/upload")
    // Observable<String> uploadFile1(@Path("path") String url, @Part MultipartBody.Part file);

    Observable<String> uploadFile1(@Path("path") String url, @Part MultipartBody.Part file, @Part("number") RequestBody requestBody);

   /* //附件上传
    @Multipart
    @POST(comm + "user/upload")
    Observable<Boolean> uploadFile2(@Part MultipartBody.Part file);*/

    //更新照片
    @POST(comm + "user/update")
    Observable<Boolean> updateImage(@Body JSONObject jsonObject);

    //更新用户信息
    @POST(comm + "users/update")
    Observable<String> updateUseInfo(@Body JSONObject jsonObject);

    @POST(comm + "log/add")
    Observable<String> logAdd(@Body JSONObject jsonObject);

    @POST(comm + "log/update")
    Observable<String> logUpdate(@Body JSONObject jsonObject);

    @GET(comm + "sysDic/work_record_type")
    Observable<List<GzrzTypeModel>> requestDictionary();

    //获取任务列表
    @POST(comm + "task/getTasklistByQuery")
    /**
     * {TASK_STATUS:3,PAGENUMBER:1,PAGESIZE:10}
     */
    Observable<ResponseData<JSONObject>> getTasklistByQuery(@Body JSONObject jsonObject);

    //获取场所明细
    @POST(comm + "place/getPlaceByCodeAndId")
    /**
     * {"CODE":"RCJC_JXCS","ID":"C024403070084"}
     */
    Observable<CsxcEntity> getPlaceByCodeAndId(@Body JSONObject jsonObject);


    //根据行业类型查询检查项
    @POST(comm + "task/getJcxmByHylx")
    /**
     * {"ID":"C024403070084"}
     */
    Observable<List<ZajcEntity>> getJcxmByHylx(@Body JSONObject jsonObject);

    //停止任务
    @POST(comm + "task/stop")
    /**
     * {"ID":"C024403070084"}
     */
    Observable<Boolean> stopTask(@Body JSONObject jsonObject);

    //根据行业类型查询检查项
    @POST(comm + "checkResult/getCheckResultlistByQuery")
    /**
     * {"ID":"C024403070084"}
     */
    Observable<ResponseData<JSONObject>> getJcjl(@Body JSONObject jsonObject);

    //根据行业类型查询检查项
    @POST(comm + "checkResult/add")
    /**
     * {"ID":"C024403070084"}
     */
    Observable<String> addZajc(@Body ZajcModel zajcModel);

    @POST(comm + "log/getLoglistByQuery")
    Observable<ResponseData<JSONObject>> getPageData(@Body JSONObject jsonObject);

    @POST(comm + "checkResult/getCheckResultById")
    Observable<JSONObject> getCheckResultById(@Body JSONObject jsonObject);


    //添加三类人员
    @POST(comm + "personidentity/add")
    Observable<String> addPersonidentity(@Body RycjOtherModel rycjOtherModel);

    @POST(comm + "zajcxx/getZajcxxlistByQueryNew")
    Observable<JSONObject> getZajcxxlistByQueryNew(@Body ZacjQueryCondition zacjQueryCondition);

    @POST(comm+"zajcxx/update")
    Observable<Boolean>updateAddress(@Body JSONObject jsonObject);

    @POST(comm+"zajcxxJc/getZajcxxJcListByQuery")
    Observable<JSONObject>queryZajc(@Body JSONObject jsonObject);

    @POST(comm+"zajcxxJc/add")
    Observable<JSONObject>addZajc(@Body JSONObject jsonObject);

    @POST(comm+"/attachment/addFiles")
    Observable<Boolean>addJson(@Body JSONObject jsonObject);
}
