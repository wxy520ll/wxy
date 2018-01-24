package cn.net.xinyi.xmjt.v527.presentation.gzrz.presenter;

import com.alibaba.fastjson.JSONObject;
import com.xinyi_tech.comm.base.BasePresenter;

import cn.net.xinyi.xmjt.v527.data.remote.retrofit.repository.GzrzRepository;

import static cn.net.xinyi.xmjt.config.AppContext.instance;

/**
 * Created by jiajun.wang on 2017/12/30.
 */

public class GzrzPresenter extends BasePresenter {

    private GzrzRepository gzrzRepository=new GzrzRepository();
    public final static int SUCCESS = 100;

    public void requestDictionary() {
        execute(gzrzRepository.getDictionary(), SUCCESS);
    }

    public void getGzrzRecord(int requestCode, int pageIndex, int pageSize,String kssj,String type,String sb,String sp,String seacher){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("PAGENUMBER",pageIndex);
        jsonObject.put("PAGESIZE",pageSize);
        jsonObject.put("TEL_NUMBER",instance.getLoginInfo().getCellphone());
        jsonObject.put("KSSJ",kssj);
        jsonObject.put("RECORD_TYPE",type);
        jsonObject.put("REPORTED",sb);
        jsonObject.put("APPROVAL_STATUS",sp);
        jsonObject.put("RECORD_BODY",seacher);
        String s=jsonObject.toJSONString();
        execute(gzrzRepository.getGzrzPageData(jsonObject), requestCode);
    }
}
