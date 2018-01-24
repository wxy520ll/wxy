package cn.net.xinyi.xmjt.activity.ZHFK.Duty;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;

/**
 * Created by hao.zhou on 2016/4/19.
 */
public class DutyPoliceHistoryAty extends BaseActivity2 {
    @BindView(id = R.id.lv_list)
    private ListView lv_list;
    private PolicAdapter mAdapter;
    private List<DutyOperationModle> optionInfos;
    @BindView(id = R.id.ll_empty_data)
    private LinearLayout ll_nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_duty_history);
        AnnotateManager.initBindView(DutyPoliceHistoryAty.this);
        getData();
    }

    private void getData() {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("TEL_NUMBER", userInfo.getUsername());
        String json = jo.toString();
        ApiResource.getPoliceHitory(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes);
                    if (i == 200 && result .length()>3) {
                        stopLoading();
                        optionInfos = JSON.parseArray(result, DutyOperationModle.class);
                        initAdapt();
                    }else {
                        onFailure(i, headers, bytes, null);
                    }
                } catch (Exception e) {
                    onFailure(i, headers, bytes, null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                initAdapt();
            }
        });
    }

    private void initAdapt() {
        if (null!=optionInfos&&optionInfos.size() > 0) {
            mAdapter = new PolicAdapter(lv_list,optionInfos,R.layout.aty_duty_history_item,DutyPoliceHistoryAty.this);
            lv_list.setAdapter(mAdapter);
            lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } else {
            lv_list.setVisibility(View.GONE);
            ll_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_police_history);
    }

    class PolicAdapter extends BaseListAdp<DutyOperationModle>{

        public PolicAdapter(AbsListView view, Collection<DutyOperationModle> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
        }

        @Override
        public void convert(AdapterHolder helper, DutyOperationModle item, boolean isScrolling) {
            helper.setText(R.id.tv_name,item.getSHOWNAME());
            helper.setText(R.id.tv_dh,item.getTEL_NUMBER());
            helper.setText(R.id.tv_dk,item.getDISTANCE());
            if (null==item.getENDDTIME()||item.getENDDTIME().isEmpty()){
                helper.setText(R.id.tv_year,item.getCREATETIME());
            }else {
                helper.setText(R.id.tv_year,item.getCREATETIME()+"至"+item.getENDDTIME());
            }
            helper.setText(R.id.tv_ksxl,getResources().getString(R.string.duty_police));
            helper.setText(R.id.tv_dz,item.getADDRESS());
        }
    }
}