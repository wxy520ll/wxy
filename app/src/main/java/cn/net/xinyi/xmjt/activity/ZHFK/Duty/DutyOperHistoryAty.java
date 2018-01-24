package cn.net.xinyi.xmjt.activity.ZHFK.Duty;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.DutyOperationModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;

/**
 * Created by hao.zhou on 2016/4/19.
 */
public class DutyOperHistoryAty extends BaseActivity2 {
    @BindView(id = R.id.lv_list)
    private ListView lv_list;
    private DutyOperHistoryAdp mAdapter;
    private List<DutyOperationModle> optionInfos;
    @BindView(id = R.id.ll_empty_data)
    private LinearLayout ll_nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_duty_history);
        AnnotateManager.initBindView(DutyOperHistoryAty.this);
        getData();
    }


    private void getData() {
        showLoadding();
        JSONObject jo = new JSONObject();
        if (getIntent().getFlags()==1001){
            jo.put("DUTY_BEATS_ID", getIntent().getStringExtra("XLDID"));
        }else {
            jo.put("TEL_NUMBER", userInfo.getUsername());
        }
        String json = jo.toString();
        ApiResource.getXlList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        stopLoading();
                        String result = new String(bytes);
                        optionInfos = JSON.parseObject(result, new TypeReference<List<DutyOperationModle>>() {
                        });
                        if (optionInfos.size() > 0) {
                            initAdapt();//初始化列表；
                        } else {
                            emptyData();
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
                emptyData();
            }
        });

    }

    private void initAdapt() {
        mAdapter = new DutyOperHistoryAdp(lv_list,optionInfos,R.layout.aty_duty_history_item,DutyOperHistoryAty.this);
        lv_list.setAdapter(mAdapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DutyOperHistoryAty.this, DutyOperHistoryItemAty.class);
                intent.putExtra("XLDID", optionInfos.get(position).getID());
                startActivity(intent);
            }
        });
    }

    private void emptyData() {
        lv_list.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_history);
    }
}