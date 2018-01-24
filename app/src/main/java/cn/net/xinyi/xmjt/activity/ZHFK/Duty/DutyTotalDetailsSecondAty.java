package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by hao.zhou on 2016/5/31.
 */
public class DutyTotalDetailsSecondAty extends BaseListAty {

    private JSONArray rankArray;
    private TextView tv_beatlevel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestData();
        initListView();
    }

    private void initListView() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (null==((JSONObject)rankArray.get(position)).getString("XLDID")){
                    Intent intent = new Intent(DutyTotalDetailsSecondAty.this,DutyBoxAddAty.class);
                    intent.putExtra("extra",((JSONObject)rankArray.get(position)).toJSONString());
                    intent.putExtra("isPeople",true);
                    intent.setFlags(1);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(DutyTotalDetailsSecondAty.this,DutyOperHistoryAty.class);
                    intent.putExtra("XLDID",((JSONObject)rankArray.get(position)).getString("XLDID"));
                    intent.setFlags(1001);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyPCSDetailsXQNew(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                rankArray= JSON.parseArray(result);
                if (rankArray.size() > 0){
                    setHasData();
                    DutyTotalDetailsSecondAdp adp = new DutyTotalDetailsSecondAdp(DutyTotalDetailsSecondAty.this,rankArray);
                    adp.setOnPeopleClickListener(new DutyTotalDetailsSecondAdp.OnPeopleClickListener() {
                        @Override
                        public void onClick(View view,JSONObject jsonObject) {
                            if (null ==  jsonObject.getString("NAME")) {
                                toast("当前无人巡逻！");
                            }else {
                                Intent intent = new Intent(DutyTotalDetailsSecondAty.this,DutyBoxAddAty.class);
                                intent.putExtra("data",jsonObject.getString("XLDID"));
                                intent.putExtra("extra",jsonObject.toJSONString());
                                intent.putExtra("isPeople",true);
                                intent.setFlags(1);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onPointClick(View view, JSONObject jsonObject) {
                            if (null == jsonObject.getString("XLDID")){//处警状态
                                Intent intent = new Intent(DutyTotalDetailsSecondAty.this,DutyBoxAddAty.class);
                                intent.putExtra("extra",jsonObject.toJSONString());
                                intent.putExtra("isPeople",true);
                                intent.setFlags(1);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(DutyTotalDetailsSecondAty.this,DutyBoxAddAty.class);
                                intent.putExtra("data",jsonObject.getString("XLDID"));
                                intent.putExtra("extra",jsonObject.toJSONString());
                                intent.setFlags(1);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFlightRults(View view, JSONObject jsonObject) {
                            if (null ==  jsonObject.getString("XLDID")){//处警状态
                                Intent intent = new Intent(DutyTotalDetailsSecondAty.this,DutyBoxAddAty.class);
                                intent.putExtra("extra",jsonObject.toJSONString());
                                intent.putExtra("isPeople",true);
                                intent.setFlags(1);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(DutyTotalDetailsSecondAty.this,DutyFlightsAty.class);
                                intent.putExtra("data",jsonObject.getString("FRID"));
                                intent.setFlags(1);
                                startActivity(intent);
                            }
                        }
                    });
                    mListView.setAdapter(adp);
                } else {
                    stopLoading();
                    setNoData();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                setNoData();
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
        requestJson.put("SJC",getIntent().getStringExtra("TIME"));
        requestJson.put("PCSBM",getIntent().getStringExtra("PCSBM"));
        if (!"全部".equals(tv_beatlevel.getText().toString().trim())){
            requestJson.put("BEAT_LEVEL", BaseDataUtils.getLevelNum(tv_beatlevel.getText().toString()));
        }
        return requestJson.toJSONString();
    }


    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        View addView= LayoutInflater.from(this).inflate(R.layout.aty_duty_total_details_second,parent);
        parent.setVisibility(View.VISIBLE);
        tv_beatlevel=(TextView)addView.findViewById(R.id.tv_beatlevel);
        tv_beatlevel.setText(getIntent().getStringExtra("BEATLEVEL"));
        tv_beatlevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String [] levels=new String[]{"全部","一级","二级","三级","视频"};
                BaseDataUtils.showAlertDialog(DutyTotalDetailsSecondAty.this,getResources().getStringArray(R.array.duty_level),tv_beatlevel);
            }
        });
        tv_beatlevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                rankArray=null;
                requestData();
            }
        });
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.xl_tj);
    }


}