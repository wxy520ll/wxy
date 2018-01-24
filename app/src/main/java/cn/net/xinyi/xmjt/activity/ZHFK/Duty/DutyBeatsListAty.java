package cn.net.xinyi.xmjt.activity.ZHFK.Duty;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.AppContext;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by hao.zhou on 2016/6/29.
 */
public class DutyBeatsListAty extends BaseListAty implements View.OnClickListener {
    //title 查询按钮、以及查询条件
    private TextView tv_level;//选择巡逻段级别
    private TextView tv_type;//选择巡逻方式
    private Button btn_query;//查询
    public static DutyBeatsListAty intence;
    private JSONArray rankArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotateManager.initBindView(this);//注解式绑定控件
        intence=this;
        initBaseData();
    }

    private void initBaseData() {
        tv_level.setText("二级");
        tv_type.setText("GPS车辆");
        requestData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DutyBeatsListAty.this,DutyStartAty.class);
                intent.putExtra("data",((JSONObject)rankArray.get(i)).getString("BID"));
                intent.putExtra("BEAT_TYPE",((JSONObject)rankArray.get(i)).getString("BEAT_TYPE"));
                startActivity(intent);
            }
        });
    }


    private String getRequest() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("DEPT_ID", AppContext.instance().getLoginInfo().getOrgancode());
        requestJson.put("BEAT_LEVEL", BaseDataUtils.getLevelNum(tv_level.getText().toString()));
        requestJson.put("BEAT_TYPE", BaseDataUtils.getTypeNum(tv_type.getText().toString()));
        return requestJson.toJSONString();
    }
    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyBeatsList(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                if (null!=rankArray){
                    rankArray.clear();
                }
                rankArray= JSON.parseArray(result);
                if (rankArray!=null&&rankArray.size()>0){
                    mNodata.setVisibility(View.GONE);
                    DutyBeatsAddAdp adp = new DutyBeatsAddAdp(DutyBeatsListAty.this,rankArray);
                    mListView.setAdapter(adp);
                } else {
                    mNodata.setVisibility(View.VISIBLE);
                }
                stopLoading();
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");
                mNodata.setVisibility(View.VISIBLE);
            }
        });
    }

    //查询部分布局
    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        View addView= LayoutInflater.from(this).inflate(R.layout.aty_dutybeats_top,parent);
        parent.setVisibility(View.VISIBLE);
        tv_level=(TextView)addView.findViewById(R.id.tv_level);//巡逻段级别
        tv_level.setOnClickListener(this);
        tv_type=(TextView)addView.findViewById(R.id.tv_type);//巡逻方式
        tv_type.setOnClickListener(this);
        btn_query=(Button)addView.findViewById(R.id.btn_query);//查询
        btn_query.setOnClickListener(this);
    }



    //点击事件监听
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_query://点击查询
                if (((AppContext) getApplication()).getNetworkType() == 0) {
                    toast(getString(R.string.network_not_connected));
                } else{
                    requestData();
                }
                break;
            case R.id.tv_level://巡逻级别
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_level),tv_level);
                break;
            case R.id.tv_type://巡逻类型
                BaseDataUtils.showAlertDialog(this, getResources().getStringArray(R.array.duty_type),tv_type);
                break;
        }
    }
    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_beats);
    }


}