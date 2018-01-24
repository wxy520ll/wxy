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
import com.bigkoo.pickerview.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Calendar;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.DateUtil;

/**
 * Created by hao.zhou on 2016/5/31.
 */
public class PlcBxTJAty extends BaseListAty implements View.OnClickListener{

    private View addView;
    private TextView tv_start_date;
    private JSONArray rankArray;
    private String sjc;
    private TimePickerView pvTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTimePickerView();
    }


    //初始化时间data控件
    private void initTimePickerView() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        pvTime.setTitle("请选择时间");
        //控制时间范围
        final Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR)+20);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String choosDate= DateUtil.date2String(date,"yyyy-MM-dd HH:mm");
                tv_start_date.setText(choosDate);
                rankArray=null;
                requestData();
            }
        });
        setData();
    }


    private void setData() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent(PlcBxTJAty.this,PlcBxTJXQAty.class);
                intent.putExtra("PCSMC",((JSONObject)rankArray.get(position)).getString("PCSMC"));
                intent.putExtra("SJC",sjc);
                startActivity(intent);
            }
        });
        requestData();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getPCSGTTJ(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                rankArray= JSON.parseArray(result);
                if (rankArray!=null){
                    PlcBxTJAdp adp = new PlcBxTJAdp(PlcBxTJAty.this,rankArray);
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
        requestJson.put("SJC",tv_start_date.getText().toString()+":00");
        return requestJson.toJSONString();
    }


    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        addView= LayoutInflater.from(this).inflate(R.layout.aty_plcbx_tj,parent);
        parent.setVisibility(View.VISIBLE);
        tv_start_date=(TextView)addView.findViewById(R.id.tv_start_date);//开始时间
        tv_start_date.setText(DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm"));//默认当前时间
        tv_start_date.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_start_date:
                pvTime.show();
                break;
        }
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