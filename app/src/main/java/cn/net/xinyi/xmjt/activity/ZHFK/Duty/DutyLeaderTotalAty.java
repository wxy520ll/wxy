package cn.net.xinyi.xmjt.activity.ZHFK.Duty;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
public class DutyLeaderTotalAty extends BaseListAty implements View.OnClickListener{

    private View addView;
    private JSONArray rankArray;
    private TextView tv_choose_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    private void setData() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int position, long l) {
                new AlertDialog.Builder(DutyLeaderTotalAty.this)
                        .setTitle(getString(R.string.tips))
                        .setMessage("拨打电话联系"+((JSONObject)rankArray.get(position)).getString("XZXM")
                                +"("+((JSONObject)rankArray.get(position)).getString("XZHM")+")")
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();// 创建一个意图
                                intent.setAction(Intent.ACTION_CALL);// 指定其动作为拨打电话 添加打电话的动作
                                intent.setData(Uri.parse("tel:" + ((JSONObject)rankArray.get(position)).getString("XZHM")));// 指定要拨出的号码
                                startActivity(intent);// 执行动作
                            }
                        })
                        .setCancelable(false).show();
            }
        });
        requestData();
    }

    @Override
    protected void requestData() {
        showLoadding();
        ApiResource.getXZTJ(getRequest(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                stopLoading();
                String result = new String(bytes);
                rankArray= JSON.parseArray(result);
                if (rankArray.size()>0 ){
                    setHasData();
                    DutyLeaderTotalAdp adp = new DutyLeaderTotalAdp(DutyLeaderTotalAty.this,rankArray);
                    mListView.setAdapter(adp);
                    adp.setOnPeopleClickListener(new DutyLeaderTotalAdp.OnPeopleClickListener() {
                        @Override
                        public void onHistoryClick(View view, JSONObject jsonObject) {
                            Intent intent=new Intent(DutyLeaderTotalAty.this,DutyLeaderHistoryAty.class);
                            intent.putExtra("PCSBM",jsonObject.getString("ZDBM"));
                            intent.setFlags(1);
                            startActivity(intent);
                        }
                    });
                }else {
                    setNoData();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                setNoData();
                toast(getString(R.string.get_data_faile));
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
        if (!BaseDataUtils.notLeader() || BaseDataUtils.isAdminPcs() ){
            requestJson.put("PCSBM","");
        }else {
            requestJson.put("PCSBM",userInfo.getOrgancode());
        }
        requestJson.put("SJ",tv_choose_date.getText().toString());
        return requestJson.toJSONString();
    }


    @Override
    public void setupTopView(LinearLayout parent) {
        super.setupTopView(parent);
        addView= LayoutInflater.from(this).inflate(R.layout.aty_xlxz_tj,parent);
        tv_choose_date=(TextView) addView.findViewById(R.id.tv_choose_date);
        tv_choose_date.setOnClickListener(this);
        tv_choose_date.setText(BaseDataUtils.getNowYearAndMonthAndDay());
        parent.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }


    @Override
    public String getAtyTitle() {
        return getString(R.string.xl_xztj);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_choose_date:
                new DatePickerDialog(
                        DutyLeaderTotalAty.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        c_start.set(year, monthOfYear, dayOfMonth);
                        CharSequence forma= DateFormat.format("yyyy-MM-dd", c_start);
                        tv_choose_date.setText(forma);
                        requestData();
                    }
                },c_end.get(Calendar.YEAR), c_end .get(Calendar.MONTH), c_end
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;


        }
    }
}