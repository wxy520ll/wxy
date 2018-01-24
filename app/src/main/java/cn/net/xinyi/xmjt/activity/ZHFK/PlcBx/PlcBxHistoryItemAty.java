package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.content.Context;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
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
import cn.net.xinyi.xmjt.model.PlcBxWorkLog;

/**
 * Created by hao.zhou on 2016/4/21.
 */
public class PlcBxHistoryItemAty extends BaseActivity2 {

    private List<PlcBxWorkLog> operInfos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dutyhistory_item);
        getData();
    }


    private void getData() {
        showLoadding();
        JSONObject jo = new JSONObject();
        jo.put("ID", getIntent().getStringExtra("ID"));
        String json = jo.toString();
        ApiResource.getGtData(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        stopLoading();
                        String result = new String(bytes);
                        JSONObject jo1 = (JSONObject)JSON.parseArray(result).get(0);
                        for (int k = 0; k < jo1.size(); k++) {
                            operInfos = JSON.parseObject(jo1.getString("DATALIST"), new TypeReference<List<PlcBxWorkLog>>() {
                            });
                        }
                        if (operInfos.size()>0){
                            intiAdapt();
                        }else {
                            onFailure(i, headers, bytes, null);
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
            }
        });
    }

    private void intiAdapt() {
        ListView lv_list=(ListView)findViewById(R.id.lv_list);
        PlcBxHitoryItemAdp   mAdapter=new PlcBxHitoryItemAdp(lv_list,operInfos,R.layout.aty_dutyhistory_item_adp,PlcBxHistoryItemAty.this);
        lv_list.setAdapter(mAdapter);
    }


    class PlcBxHitoryItemAdp extends BaseListAdp<PlcBxWorkLog> {

        public PlcBxHitoryItemAdp(AbsListView view, Collection<PlcBxWorkLog> datas, int itemLayoutId, Context context) {
            super(view, datas, itemLayoutId, context);
        }
        //  1开始值守
        // 2结束值守
        // 3离岗报备
        // 4擅自离岗
        // 5继续值守
        @Override
        public void convert(AdapterHolder helper, PlcBxWorkLog item, boolean isScrolling) {
            helper.setText(R.id.tv_year, item.getSCSJ());
            if (item.getLX().equals("1")) {//开始值守
                helper.setText(R.id.tv_lx, getResources().getString(R.string.start_working_plc_bx));
                helper.setText(R.id.tv_xldmc,"");
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (item.getLX().equals("2")) {//结束值守
                helper.setText(R.id.tv_lx, getResources().getString(R.string.ending_work_plc_bx));
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (item.getLX().equals("3")) {//离岗报备
                helper.setText(R.id.tv_lx, getResources().getString(R.string.bb_work_plc_bx));
                helper.setText(R.id.tv_xldmc,item.getMS());
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.bbutton_danger));
            } else if (item.getLX().equals("4")) {//擅自离岗
                helper.setText(R.id.tv_lx,getResources().getString(R.string.sz_work_plc_bx));
                helper.setText(R.id.tv_xldmc,"");
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.bbutton_success));
            } else if (item.getLX().equals("5")) {//继续值守
                helper.setText(R.id.tv_lx,getResources().getString(R.string.contin_work_plc_bx));
                helper.setText(R.id.tv_xldmc,"");
                helper.getConvertView().findViewById(R.id.tv_lx)
                        .setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }
    }



    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_bx_history_xq);
    }
}