package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Context;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Collection;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AdapterHolder;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.model.DutyOperationModle;

/**
 * Created by hao.zhou on 2016/4/21.
 */
public class DutyOperHistoryItemAty extends BaseActivity2 {
    List<DutyOperationModle> operInfos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dutyhistory_item);
        getData();
    }



    private void getData() {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("ID", getIntent().getStringExtra("XLDID"));
        String json = jo.toString();
        ApiResource.getDutyHistory(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        stopLoading();
                        String result = new String(bytes);
                        JSONArray js = JSON.parseArray(result);
                        for (int j = 0; j < js.size(); j++) {
                            JSONObject jo1 = (JSONObject) js.get(0);
                            for (int k = 0; k < jo1.size(); k++) {
                                operInfos = JSON.parseObject(jo1.getString("DATALIST"), new TypeReference<List<DutyOperationModle>>() {
                                });
                            }
                        }
                        if (operInfos.size()>0){
                            intiAdapt();
                        }

                    } else {
                        onFailure(i, headers, bytes, null);
                    }

                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败！");
            }
        });
    }

    private void intiAdapt() {
        ListView lv_list=(ListView)findViewById(R.id.lv_list);
        DutyHitoryItemAdp   mAdapter=new DutyHitoryItemAdp(lv_list,operInfos,R.layout.aty_dutyhistory_item_adp,DutyOperHistoryItemAty.this);
        lv_list.setAdapter(mAdapter);
    }




    class DutyHitoryItemAdp extends BaseListAdp<DutyOperationModle>{

        public DutyHitoryItemAdp(AbsListView view, Collection<DutyOperationModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
        }

        @Override
        public void convert(AdapterHolder helper, DutyOperationModle item, boolean isScrolling) {
            helper.setText(R.id.tv_dz, item.getADDRESS());
            if (null==item.getENDDTIME()||item.getENDDTIME().isEmpty()){
                helper.setText(R.id.tv_year,item.getCREATETIME());
            }else {
                helper.setText(R.id.tv_year,item.getCREATETIME()+"至"+item.getENDDTIME());
            }
            if (item.getDUTY_OPR_TYPE().equals(DutyStartAty.TYPE_BEATS_START)) {//巡逻
                helper.setText(R.id.tv_lx, getResources().getString(R.string.duty_beats));
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.blue));
                helper.setText(R.id.tv_xldmc, " ");
            } else if (item.getDUTY_OPR_TYPE().equals(DutyStartAty.TYPE_BOXS)) {//打卡
                helper.setText(R.id.tv_lx, getResources().getString(R.string.duty_point));
                helper.setText(R.id.tv_xldmc, item.getDUTY_SIGNBOX_NAME());
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.bg_dark));
            } else if (item.getDUTY_OPR_TYPE().equals(DutyStartAty.TYPE_REPORTS_START)) {//报备
                helper.setText(R.id.tv_lx, getResources().getString(R.string.xl_bb_ks));
                helper.setText(R.id.tv_xldmc,item.getDESCRIPTION());
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.bbutton_danger));
            } else if (item.getDUTY_OPR_TYPE().equals(DutyPoliceAty.TYPE_POLICE_START)) {//处警
                helper.setText(R.id.tv_lx,getResources().getString(R.string.duty_police));
                helper.setText(R.id.tv_xldmc,"");
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.bbutton_success));
            }
        }
    }



    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_history_item);
    }
}