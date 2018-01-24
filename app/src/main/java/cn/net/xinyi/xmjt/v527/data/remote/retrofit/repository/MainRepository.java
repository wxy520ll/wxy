package cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.model.DevicesModel;
import cn.net.xinyi.xmjt.model.UnitModel;
import cn.net.xinyi.xmjt.model.UserInfo;
import cn.net.xinyi.xmjt.model.View.TaskListModel;
import cn.net.xinyi.xmjt.utils.SharedPreferencesUtil;
import cn.net.xinyi.xmjt.v527.base.model.ResponseData;
import cn.net.xinyi.xmjt.v527.config.Constants;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.GzcxEntity;
import cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity.JqdtEntity;
import cn.net.xinyi.xmjt.v527.presentation.home.model.GzcxModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlDeptModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlGroup;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlPersonModel;
import cn.net.xinyi.xmjt.v527.presentation.txl.model.TxlTypeModel;
import cn.net.xinyi.xmjt.v527.util.TimeUtils2;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static cn.net.xinyi.xmjt.utils.BaiduTraceFacade.userInfo;

/**
 * Created by Fracesuit on 2017/11/8.
 */

public class MainRepository {


    public Observable<String> loginByScan(String scanCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("USERNAME", userInfo.getUsername());
        jsonObject.put("LOGIN_TYPE", "PHONE");
        jsonObject.put("UUID", scanCode);
        jsonObject.put("DEVICEID", ((AppContext) Utils.getApp()).getDeviceId());

        return AppContext.apiService.loginByScan(jsonObject);
    }

    public Observable<String> getInfoByCommand() {
        return Observable.just("getTaskList", "getDeviceList", "getUnitList")
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String command) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("command", command);
                        jsonObject.put("apikey", Constants.JWT_APP_KEY);
                        return AppContext.apiService.getInfoByCommand(jsonObject).doOnNext(new Action1<String>() {
                            @Override
                            public void call(String content) {
                                if (!StringUtils.isEmpty(content)) {
                                    final JSONObject response = JSON.parseObject(content, JSONObject.class);
                                    final String error = response.getString("error");
                                    if ("0".equals(error)) {
                                        SharedPreferencesUtil.putString(Utils.getApp(), command, content);
                                    } else {
                                        content = SharedPreferencesUtil.getString(Utils.getApp(), command, "");
                                    }

                                } else {
                                    content = SharedPreferencesUtil.getString(Utils.getApp(), command, "");
                                }

                                if (!StringUtils.isEmpty(content)) {
                                    if ("getDeviceList".equals(command)) {
                                        final DevicesModel d = JSON.parseObject(content, DevicesModel.class);
                                        Map<String, DevicesModel.DataBean> map = AppContext.getDevicesMap();
                                        for (DevicesModel.DataBean db : d.getData()) {
                                            map.put("" + db.getId(), db);
                                        }
                                    } else if ("getTaskList".equals(command)) {
                                        final TaskListModel taskListModel = JSON.parseObject(content, TaskListModel.class);
                                        Map<String, TaskListModel.DataBean> map = AppContext.getTaskMap();
                                        for (TaskListModel.DataBean db : taskListModel.getData()) {
                                            map.put("" + db.getId(), db);
                                        }

                                    } else if ("getUnitList".equals(command)) {


                                        final UnitModel unitModel = JSON.parseObject(content, UnitModel.class);
                                        Map<String, UnitModel.DataBean> map = AppContext.getUnitModelMap();
                                        for (UnitModel.DataBean db : unitModel.getData()) {
                                            map.put("" + db.getId(), db);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
    }

    public Observable<List<Object>> initHomeData() {
        String time = TimeUtils2.getTime(0);
        final Observable<List<JqdtEntity>> jqlistByQuery = getJqlistByQuery(null, 1, 10);
        final Observable<GzcxEntity> gzcx = getGzcx(time);
        return Observable.merge(jqlistByQuery, gzcx).toList();

    }

    public Observable<List<JqdtEntity>> getJqlistByQuery(String time, int pageIndex, int pageSize) {
        JSONObject jsonObject = new JSONObject();
        if (time != null) {
            final String[] split = time.split(",");
            jsonObject.put("KSSJ", split[0]);
            jsonObject.put("JSSJ", split[1]);
        }
        jsonObject.put("DEPARTMENTCODE", AppContext.instance().getLoginInfo().getOrgancode());
        jsonObject.put("PAGENUMBER", pageIndex);
        jsonObject.put("PAGESIZE", pageSize);
        return AppContext.apiService.getJqlistByQuery(jsonObject)
                .flatMap(new Func1<ResponseData<JSONObject>, Observable<List<JqdtEntity>>>() {
                    @Override
                    public Observable<List<JqdtEntity>> call(ResponseData<JSONObject> jsonObjectResponseData) {
                        if ("0".equals(jsonObjectResponseData.getStatus())) {
                            final String list = jsonObjectResponseData.getResult().getString("list");
                            return Observable.just(JSON.parseArray(list, JqdtEntity.class));
                        } else {
                            return Observable.error(new Throwable("获取警情动态数据异常"));
                        }
                    }
                });

    }

    public Observable<List<GzcxModel>> getGzcxDetailList(String time, final String type) {
        final UserInfo loginInfo = AppContext.instance.getLoginInfo();
        JSONObject jsonObject = new JSONObject();
        final String[] split = time.split(",");
        jsonObject.put("KSSJ", split[0]);
        jsonObject.put("JSSJ", split[1]);
        jsonObject.put("ORGNAME", loginInfo.getPcs());
        jsonObject.put("SORT", "0");
        jsonObject.put("ORGCODE", loginInfo.getOrgancode());
        jsonObject.put("SORT_COLUMN", type);
        jsonObject.put(type, "1");
        return AppContext.apiService.distanceAndRypcAndPlate(jsonObject).flatMap(new Func1<List<GzcxEntity>, Observable<GzcxEntity>>() {
            @Override
            public Observable<GzcxEntity> call(List<GzcxEntity> gzcxEntities) {
                return Observable.from(gzcxEntities);
            }
        }).map(new Func1<GzcxEntity, GzcxModel>() {
            @Override
            public GzcxModel call(GzcxEntity gzcxEntity) {
                //RYPCRANK,PLATERANK,XLJLRANK
                final String name = gzcxEntity.getNAME();
                final String mobile = gzcxEntity.getUSERNAME();
                int pm = 1;
                String typeName = null;
                String size = "1";
                if ("RYPCRANK".equals(type)) {
                    pm = gzcxEntity.getRYPCRANK();
                    size = gzcxEntity.getRYPCZS() + "";
                    typeName = "人员采集";
                } else if ("PLATERANK".equals(type)) {
                    pm = gzcxEntity.getPLATERANK();
                    size = gzcxEntity.getPLATEZS() + "";
                    typeName = "车牌采集";
                } else if ("XLJLRANK".equals(type)) {
                    pm = gzcxEntity.getXLJLRANK();
                    size = gzcxEntity.getXLJL() + "";
                    typeName = "巡逻公里数";
                }

                return new GzcxModel(pm + "", name, mobile, typeName, size + "");
            }
        }).toList();
    }


    //获取通讯录
    public Observable<List<MultiItemEntity>> getTxlById(String orgcode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ORGCODE", orgcode);
        //jsonObject.put("")
        return AppContext.cacheProviders.getTxl(AppContext.apiService.getTxl(jsonObject), new DynamicKey(orgcode), new EvictDynamicKey(false))
                .flatMap(new Func1<Reply<ResponseData<String>>, Observable<ResponseData<String>>>() {
                    @Override
                    public Observable<ResponseData<String>> call(Reply<ResponseData<String>> responseDataReply) {
                        return Observable.just(responseDataReply.getData());
                    }
                })
               // AppContext.apiService.getTxl(jsonObject)
                .flatMap(new Func1<ResponseData<String>, Observable<List<MultiItemEntity>>>() {
                    @Override
                    public Observable<List<MultiItemEntity>> call(ResponseData<String> stringResponseData) {
                        final List<MultiItemEntity> multiItemEntities = new ArrayList<>();
                        if ("0".equals(stringResponseData.getStatus())) {
                            final JSONObject result = JSON.parseObject(stringResponseData.getResult(), JSONObject.class);
                            final List<TxlPersonModel> personModels = JSON.parseArray(result.getString("USERLIST"), TxlPersonModel.class);
                            Collections.sort(personModels);
                            if (personModels != null && personModels.size() > 0) {

                                final HashMap<String, List<TxlPersonModel>> tempMap = new HashMap<>();

                                for (TxlPersonModel m : personModels) {
                                    final String accounttype = m.getACCOUNTTYPE();
                                    List<TxlPersonModel> txlPersonModels = tempMap.get(accounttype);
                                    if (txlPersonModels == null) {
                                        txlPersonModels = new ArrayList<>();
                                        tempMap.put(accounttype, txlPersonModels);
                                    }
                                    txlPersonModels.add(m);
                                }
                                final List<TxlTypeModel> txlTypeModels = new ArrayList<>();
                                for (String key : tempMap.keySet()) {
                                    final TxlTypeModel txlTypeModel = new TxlTypeModel();
                                    if ("民警".equals(key)) {
                                        txlTypeModel.setPx(1);
                                    } else if ("聘员".equals(key) || "辅警".equals(key)) {
                                        txlTypeModel.setPx(2);
                                    } else {
                                        txlTypeModel.setPx(3);
                                    }

                                    txlTypeModel.setACCOUNTTYPE(key);
                                    final List<TxlPersonModel> txlPersonModels = tempMap.get(key);
                                    Collections.sort(txlPersonModels);
                                    txlTypeModel.getPerson().addAll(txlPersonModels);
                                    txlTypeModels.add(txlTypeModel);
                                }

                                Collections.sort(txlTypeModels);
                                multiItemEntities.addAll(txlTypeModels);
                            }


                            final TxlGroup txlGroup = new TxlGroup("子部门");
                            multiItemEntities.add(txlGroup);

                            final List<TxlDeptModel> deptModels = JSON.parseArray(result.getString("SUBSDEPT"), TxlDeptModel.class);
                            if (deptModels != null && deptModels.size() > 0) {
                                multiItemEntities.addAll(deptModels);
                            }

                            return Observable.just(multiItemEntities);
                        } else {
                            return Observable.error(new Throwable("获取通讯录数据失败"));
                        }
                    }
                });
    }

    //获取通讯录
    public Observable<List<MultiItemEntity>> getTxlById(String orgcode, int t) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ORGCODE", orgcode);
        jsonObject.put("SEARCH", "民警");
        //jsonObject.put("")
        return AppContext.cacheProviders.getTxl(AppContext.apiService.getTxl(jsonObject), new DynamicKey(orgcode), new EvictDynamicKey(false))
                .flatMap(new Func1<Reply<ResponseData<String>>, Observable<ResponseData<String>>>() {
                    @Override
                    public Observable<ResponseData<String>> call(Reply<ResponseData<String>> responseDataReply) {
                        return Observable.just(responseDataReply.getData());
                    }
                })
                // AppContext.apiService.getTxl(jsonObject)
                .flatMap(new Func1<ResponseData<String>, Observable<List<MultiItemEntity>>>() {
                    @Override
                    public Observable<List<MultiItemEntity>> call(ResponseData<String> stringResponseData) {
                        final List<MultiItemEntity> multiItemEntities = new ArrayList<>();
                        if ("0".equals(stringResponseData.getStatus())) {
                            final JSONObject result = JSON.parseObject(stringResponseData.getResult(), JSONObject.class);
                            final List<TxlPersonModel> personModels = JSON.parseArray(result.getString("USERLIST"), TxlPersonModel.class);
                            if (personModels != null && personModels.size() > 0) {
                                multiItemEntities.addAll(personModels);
                            }

                            final TxlGroup txlGroup = new TxlGroup("子部门");
                            multiItemEntities.add(txlGroup);

                            final List<TxlDeptModel> deptModels = JSON.parseArray(result.getString("SUBSDEPT"), TxlDeptModel.class);
                            if (deptModels != null && deptModels.size() > 0) {
                                multiItemEntities.addAll(deptModels);
                            }
                            return Observable.just(multiItemEntities);
                        } else {
                            return Observable.error(new Throwable("获取通讯录数据失败"));
                        }
                    }
                });
    }


    public Observable<GzcxEntity> getGzcx(String time) {
        final UserInfo loginInfo = AppContext.instance.getLoginInfo();
        JSONObject jsonObject = new JSONObject();
        final String[] split = time.split(",");
        jsonObject.put("KSSJ", split[0]);
        jsonObject.put("JSSJ", split[1]);
        jsonObject.put("USERNAME", AppContext.instance.getLoginInfo().getUsername());
        jsonObject.put("ORGNAME", loginInfo.getPcs());
        jsonObject.put("ORGCODE", loginInfo.getOrgancode());
        return AppContext.apiService.distanceAndRypcAndPlate(jsonObject)
                .map(new Func1<List<GzcxEntity>, GzcxEntity>() {
                    @Override
                    public GzcxEntity call(List<GzcxEntity> gzcxEntities) {
                        if (gzcxEntities == null || gzcxEntities.size() == 0) {
                            return new GzcxEntity();
                        }
                        return gzcxEntities.get(0);
                    }
                });
    }
}


