package cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.v527.base.model.ResponseData;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.CsxcEntity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JcjlEntity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.ZajcEntity;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.CsxcModel;
import cn.net.xinyi.xmjt.v527.presentation.task.csxc.model.ZajcModel;
import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Fracesuit on 2017/12/28.
 */

public class RwRepository {

    public Observable<List<ZajcEntity>> getJcxmByHylx(String code) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("CODE", code);

        return AppContext.apiService.getJcxmByHylx(jsonObject)
              /*  .flatMap(new Func1<ResponseData<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ResponseData<String> csxcEntityResponseData) {
                        if ("0".equals(csxcEntityResponseData.getStatus())) {
                            final String csxcEntitie = csxcEntityResponseData.getResult();
                            return Observable.just(csxcEntitie);
                        } else {
                            return Observable.error(new Throwable("获取数据异常"));
                        }
                    }
                })*/;
    }

    public Observable<JSONObject> getCheckResultById(String id) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", id);

        return AppContext.apiService.getCheckResultById(jsonObject);
    }

    public Observable<List<JcjlEntity>> getJcjl(String sysId, int pageIndex, int pageSize) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("SYSTEMID", sysId);
        jsonObject.put("PAGENUMBER", pageIndex);
        jsonObject.put("PAGESIZE", pageSize);

        return AppContext.apiService.getJcjl(jsonObject)
                .flatMap(new Func1<ResponseData<JSONObject>, Observable<List<JcjlEntity>>>() {
                    @Override
                    public Observable<List<JcjlEntity>> call(ResponseData<JSONObject> csxcEntityResponseData) {
                        if ("0".equals(csxcEntityResponseData.getStatus())) {
                            final String list = csxcEntityResponseData.getResult().getString("list");
                            return Observable.just(JSON.parseArray(list, JcjlEntity.class));
                        } else {
                            return Observable.error(new Throwable("获取数据异常"));
                        }
                    }
                });
    }

    public Observable<String> addZajc(final ZajcModel zajcModel) {
        final UploadRepository uploadRepository = new UploadRepository();
        return Observable.just(zajcModel.getJczp(), zajcModel.getQmzp())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return uploadRepository.uploadFileForString("checkResult", s);
                    }
                }).toList()
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        final ZajcModel clone = zajcModel.clone();
                        clone.setJczp("/check/" + strings.get(0));
                        clone.setQmzp("/check/" + strings.get(1));
                        return AppContext.apiService.addZajc(clone);
                    }
                });
    }

    public Observable<Boolean> stopTaskById(String id) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", id);

        return AppContext.apiService.stopTask(jsonObject);
    }


    public Observable<CsxcEntity> getTaskDetail(String id, String taskCode) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("CODE", taskCode);
        jsonObject.put("ID", id);

        return AppContext.apiService.getPlaceByCodeAndId(jsonObject);
    }

    public Observable<List<CsxcModel>> getTasklistByQuery(final String stateCode, int pageIndex, int pageSize) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("TASK_STATUS", stateCode);
        jsonObject.put("PAGENUMBER", pageIndex);
        jsonObject.put("PAGESIZE", pageSize);
        return AppContext.apiService.getTasklistByQuery(jsonObject)
                .flatMap(new Func1<ResponseData<JSONObject>, Observable<List<CsxcEntity>>>() {
                    @Override
                    public Observable<List<CsxcEntity>> call(ResponseData<JSONObject> csxcEntityResponseData) {
                        if ("0".equals(csxcEntityResponseData.getStatus())) {
                            final String list = csxcEntityResponseData.getResult().getString("list");
                            return Observable.just(JSON.parseArray(list, CsxcEntity.class));
                        } else {
                            return Observable.error(new Throwable("获取数据异常"));
                        }
                    }
                })
                .flatMap(new Func1<List<CsxcEntity>, Observable<CsxcEntity>>() {
                    @Override
                    public Observable<CsxcEntity> call(List<CsxcEntity> csxcEntities) {
                        return Observable.from(csxcEntities);
                    }
                })
                .map(new Func1<CsxcEntity, CsxcModel>() {
                    @Override
                    public CsxcModel call(CsxcEntity csxcEntitie) {
                        final CsxcModel csxcModel = new CsxcModel();
                        csxcModel.setId(csxcEntitie.getID());
                        csxcModel.setCsId(csxcEntitie.getF_ID());
                        csxcModel.setCscode(csxcEntitie.getCS_KEY());

                        csxcModel.setTaskName(csxcEntitie.getTASK_NAME());
                        csxcModel.setPfr(csxcEntitie.getNAME());
                        csxcModel.setPfrDw(csxcEntitie.getORGNAME());
                        csxcModel.setTaskJj(csxcEntitie.getTASK_DESC());
                        csxcModel.setLx(csxcEntitie.getLX());
                        csxcModel.setFqsj(csxcEntitie.getSEND_TIME());
                        csxcModel.setXqsj(csxcEntitie.getLIMIT_TIME());
                        csxcModel.setState(stateCode);
                        return csxcModel;
                    }
                }).toList();
    }
}
