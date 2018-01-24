package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;

/**
 * Created by hao.zhou on 2016/4/19.
 */
public class PlcBxHistoryAty extends BaseActivity2 {
    @BindView(id = R.id.lv_list)
    private ListView lv_list;
    @BindView(id = R.id.ll_empty_data)
    private LinearLayout ll_nodata;
    private JSONArray rankArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_duty_history);
        AnnotateManager.initBindView(PlcBxHistoryAty.this);
        getData();
    }


    private void getData() {
        showLoadding();
        JSONObject jo = new JSONObject();
        if (getIntent().getFlags()==1001){
            jo.put("GTID", getIntent().getStringExtra("GTID"));
        }else {
            jo.put("TEL_NUMBER", userInfo.getUsername());
        }
        String json = jo.toString();
        ApiResource.getGtzsList(json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    if (i == 200 && bytes != null) {
                        stopLoading();
                        String result = new String(bytes);
                        rankArray= JSON.parseArray(result);
                        if (rankArray!=null&&rankArray.size() > 0) {
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
        PlcBxHistoryAdp adp = new PlcBxHistoryAdp(this,rankArray);
        lv_list.setAdapter(adp);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlcBxHistoryAty.this, PlcBxHistoryItemAty.class);
                intent.putExtra("ID", ((JSONObject)rankArray.get(position)).getString("ID"));
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
        return getString(R.string.plc_bx_history);
    }
}