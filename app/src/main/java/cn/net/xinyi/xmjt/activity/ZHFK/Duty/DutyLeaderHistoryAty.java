package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Calendar;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;

/**
 * Created by hao.zhou on 2016/5/31.
 */
public class DutyLeaderHistoryAty extends BaseListAty implements View.OnClickListener {

    private JSONArray rankArray;
    private View addView;
    private TextView tv_choose_date;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    private void setData() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            }
        });
        requestData();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getDutyLeaderHistory(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                rankArray= JSON.parseArray(result);
                if (rankArray.size()>0){
                    setHasData();
                    DutyLeaderHistoryAdp adp = new DutyLeaderHistoryAdp(DutyLeaderHistoryAty.this,rankArray);
                    mListView.setAdapter(adp);
                } else {
                    setNoData();
                    toast("没有数据");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                setNoData();
                stopLoading();
                toast("获取数据失败");
            }
        });
    }


    /*
     * @return
     */
    private String getRequest() {
        JSONObject  requestJson = new JSONObject();
        if (getIntent().getFlags()==1){
            requestJson.put("PCSBM",getIntent().getStringExtra("PCSBM"));
        }else {
            requestJson.put("SJHM",userInfo.getUsername());
        }
        requestJson.put("SCSJ",tv_choose_date.getText().toString());
        return requestJson.toJSONString();
    }

    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        addView= LayoutInflater.from(this).inflate(R.layout.aty_xlxz_tj,parent);
        parent.setVisibility(View.VISIBLE);
        tv_choose_date=(TextView) addView.findViewById(R.id.tv_choose_date);
        addView.findViewById(R.id.top_title).setVisibility(View.GONE);
        tv_choose_date.setText(BaseDataUtils.getNowYearAndMonthAndDay());
        tv_choose_date.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_choose_date:
                new DatePickerDialog(
                        DutyLeaderHistoryAty.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_start.set(year, monthOfYear, dayOfMonth);
                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_start);
                        tv_choose_date.setText(forma);
                        if (rankArray!=null){
                            rankArray.clear();
                        }
                        requestData();
                    }
                },c_end.get(Calendar.YEAR), c_end .get(Calendar.MONTH), c_end
                        .get(Calendar.DAY_OF_MONTH)).show();

                break;
        }
    }
    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.duty_leader_history);
    }


}