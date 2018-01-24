package cn.net.xinyi.xmjt.v527.presentation.gzrz.presenter;

import com.alibaba.fastjson.JSONObject;
import com.xinyi_tech.comm.base.BasePresenter;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.GzrzRepository;

/**
 * Created by jiajun.wang on 2017/12/29.
 */

public class AddGzrzPresenter extends BasePresenter {

    public static final int FILESUCCESS=100;
    public static final int JSONSUCCESS=101;
    public static final int DICTIONARY=102;
    private GzrzRepository gzrzRepository=new GzrzRepository();

    /**
     * 上传工作日志
     */
    public void uploadFile(JSONObject jsonObject){
        execute(gzrzRepository.uploadFile(jsonObject),FILESUCCESS);
    }

    public void updateJson(JSONObject jsonObject){
        execute(gzrzRepository.updateJson(jsonObject),JSONSUCCESS);
    }

    public void uploadJson(JSONObject jsonObject){
        execute(gzrzRepository.uoloadJson(jsonObject),JSONSUCCESS);
    }

    public void getDictionary(){
        execute(gzrzRepository.getDictionary(),DICTIONARY);
    }

}
