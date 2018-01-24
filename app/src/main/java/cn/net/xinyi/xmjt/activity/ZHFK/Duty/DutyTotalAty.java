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

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by hao.zhou on 2016/5/31.
 */
public class DutyTotalAty extends BaseListAty {

    private JSONArray rankArray;
    private TextView tv_beatlevel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    private void setData() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent(DutyTotalAty.this,DutyTotalDetailsAty.class);
                intent.putExtra("PCSBM",((JSONObject)rankArray.get(position)).getString("PCSBM"));
                intent.putExtra("BEATLEVEL",tv_beatlevel.getText().toString());
                startActivity(intent);
            }
        });
        requestData();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyPcsTotal(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                rankArray= JSON.parseArray(result);
                if (rankArray!=null){
                    DutyTotalAdp adp = new DutyTotalAdp(DutyTotalAty.this,rankArray);
                    mListView.setAdapter(adp);
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
        JSONObject  requestJson = new JSONObject();
        requestJson.put("SJC",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (!"全部".equals(tv_beatlevel.getText().toString().trim())){
            requestJson.put("BEAT_LEVEL", BaseDataUtils.getLevelNum(tv_beatlevel.getText().toString()));
        }
        return requestJson.toJSONString();
    }


    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        View addView= LayoutInflater.from(this).inflate(R.layout.aty_duty_total,parent);
        parent.setVisibility(View.VISIBLE);
        tv_beatlevel=(TextView)addView.findViewById(R.id.tv_beatlevel);
        tv_beatlevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDataUtils.showAlertDialog(DutyTotalAty.this,getResources().getStringArray(R.array.duty_level),tv_beatlevel);
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