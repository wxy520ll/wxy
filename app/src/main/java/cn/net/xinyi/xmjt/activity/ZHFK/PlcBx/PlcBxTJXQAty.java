package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.activity.ZHFK.Duty.DutyFlightsAty;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;

/**
 * Created by hao.zhou on 2016/5/31.
 */
public class PlcBxTJXQAty extends BaseListAty {

    private View addView;
    private TextView tv_start_date;
    private JSONArray rankArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    private void setData() {
        requestData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(PlcBxTJXQAty.this,PlcBxWatchAty.class);
                intent.putExtra("data",((JSONObject)rankArray.get(position)).getString("GTID"));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getPCSGTTJXQ(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                rankArray= JSON.parseArray(result);
                if (rankArray.size() > 0){
                    PlcBxTJXQAdp adp = new PlcBxTJXQAdp(PlcBxTJXQAty.this,rankArray);
                    mListView.setAdapter(adp);
                    adp.setOnPeopleClickListener(new PlcBxTJXQAdp.OnPeopleClickListener() {
                        @Override
                        public void onFlightRults(View view, JSONObject jsonObject) {
                            Intent intent = new Intent(PlcBxTJXQAty.this,DutyFlightsAty.class);
                            intent.putExtra("data",jsonObject.getString("WORKTIME"));
                            intent.setFlags(1);
                            startActivity(intent);
                        }

                        @Override
                        public void onPlcBxHistory(View view, JSONObject jsonObject) {
                            Intent intent = new Intent(PlcBxTJXQAty.this,PlcBxHistoryAty.class);
                            intent.putExtra("GTID",jsonObject.getString("GTID"));
                            intent.setFlags(1001);
                            startActivity(intent);
                        }
                    });
                } else {
                    toast("没有数据");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
            }
        });
    }

    /**
     * 获取派出所下巡逻统计数据
     *   科技通信科、人口管理科作为管理单位，可以查看全部数据
     *
     * @return
     */
    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("SJC",getIntent().getStringExtra("SJC"));
        requestJson.put("PCSMC",getIntent().getStringExtra("PCSMC"));
        return requestJson.toJSONString();
    }


    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        addView= LayoutInflater.from(this).inflate(R.layout.aty_plcbx_tj_xq,parent);
        parent.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.plc_sttn_tj);
    }


}