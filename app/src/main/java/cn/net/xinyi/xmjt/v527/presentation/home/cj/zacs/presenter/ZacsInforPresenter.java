package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.presenter;

import com.alibaba.fastjson.JSONObject;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.picker.picture.entity.LocalMedia;

import java.util.HashMap;
import java.util.List;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.ZacsCjRepository;
import cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode.ZacjModel;

/**
 * Created by jiajun.wang on 2018/1/23.
 */

public class ZacsInforPresenter extends BasePresenter {
    public final static int UPDATESUCCESS = 100;
    public final static int QUERYDATA = 101;
    public final static int UPLOADFILE = 102;
    public final static int ADDDATE = 103;
    public final static int ADDJSON = 104;
    ZacsCjRepository zacsCjRepository = new ZacsCjRepository();

    public void updateAddress(ZacjModel zacjModel) {
        execute(zacsCjRepository.updateAddress(zacjModel), UPDATESUCCESS);
    }


    public void queryZacsList(JSONObject jsonObject) {
        execute(zacsCjRepository.queryZacsList(jsonObject), QUERYDATA);
    }

    public void uploadImages(HashMap<List<LocalMedia>,Integer>map, String qm){
        execute(zacsCjRepository.uploadFile(map,qm),UPLOADFILE);
    }

    public void addZajc(JSONObject jsonObject){
        execute(zacsCjRepository.uploadZajc(jsonObject),ADDDATE);
    }

    public void addJson(JSONObject jsonObject){
        execute(zacsCjRepository.addJson(jsonObject),ADDJSON);
    }
}
