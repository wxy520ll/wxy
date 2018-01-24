package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

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
import com.bigkoo.pickerview.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Calendar;
import java.util.Date;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.DateUtil;

/**
 * Created by hao.zhou on 2016/5/31.
 * 巡逻里程统计
 */
public class DutyTotalSecondAty extends BaseListAty  implements View.OnClickListener{

    private JSONArray rankArray;
    private TextView tv_beatlevel;
    private TextView tv_start_date;
    private TextView tv_end_date;
    private TimePickerView pvTime;
    private int i_flag;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
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
                String choosDate=DateUtil.date2String(date,"yyyy-MM-dd HH:mm:ss");
                if (i_flag==1){
                    tv_start_date.setText(choosDate);
                }else {
                    tv_end_date.setText(choosDate);
                }
                rankArray=null;
                requestData();
            }
        });
    }


    private void setData() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent=new Intent(DutyTotalSecondAty.this,DutyTotalDetailsSecondAty.class);
//                intent.putExtra("PCSBM",((JSONObject)rankArray.get(position)).getString("PCSBM"));
//                intent.putExtra("BEATLEVEL",tv_beatlevel.getText().toString());
//                intent.putExtra("TIME",tv_start_date.getText().toString());
//                startActivity(intent);
            }
        });
        requestData();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyPcsDistance(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                rankArray= JSON.parseArray(result);
                if (rankArray!=null){
                    DutyTotalSecondAdp adp = new DutyTotalSecondAdp(DutyTotalSecondAty.this,rankArray);
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
        requestJson.put("KSSJ",tv_start_date.getText().toString());
        requestJson.put("JSSJ",tv_end_date.getText().toString());
//        if (!"全部".equals(tv_beatlevel.getText().toString().trim())){
//            requestJson.put("BEAT_LEVEL",BaseDataUtils.getLevelNum(tv_beatlevel.getText().toString()));
//        }
        return requestJson.toJSONString();
    }


    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        View addView= LayoutInflater.from(this).inflate(R.layout.aty_duty_total_second,parent);
        parent.setVisibility(View.VISIBLE);
        tv_beatlevel=(TextView)addView.findViewById(R.id.tv_beatlevel);//选择巡段级别
        tv_start_date=(TextView)addView.findViewById(R.id.tv_start_date);//选择时间
        tv_end_date=(TextView)addView.findViewById(R.id.tv_end_date);//选择时间
        tv_start_date.setText(DateUtil.date2String(new Date(),"yyyy-MM-dd ")+"00:00:00");//默认当天0点
        tv_end_date.setText(DateUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));//默认当前时间
        tv_beatlevel.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_beatlevel:
                BaseDataUtils.showAlertDialog(DutyTotalSecondAty.this,getResources().getStringArray(R.array.duty_level),tv_beatlevel);
                break;

            case R.id.tv_start_date:
                pvTime.show();
                i_flag=1;
                break;

            case R.id.tv_end_date:
                i_flag=2;
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
        return getString(R.string.xl_tj);
    }


}