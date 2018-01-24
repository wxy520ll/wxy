package cn.net.xinyi.xmjt.activity.Main.Setting;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Calendar;
import java.util.List;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.SalaryModle;
import cn.net.xinyi.xmjt.model.SalaryNewModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BaseDataUtils;
import cn.net.xinyi.xmjt.utils.BindView;
import cn.net.xinyi.xmjt.utils.MonPickerDialog;
import cn.net.xinyi.xmjt.utils.UI;

public class SalaryMainAty extends BaseActivity2 implements View.OnClickListener{
    //查询日期
    @BindView(id = R.id.tv_cxrq,click = true)
    private TextView tv_cxrq;
    //身份证号码
    @BindView(id = R.id.et_sfzhm)
    private EditText et_sfzhm;
    //查询
    @BindView(id = R.id.btn_query,click = true)
    private Button btn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main_salary);
        AnnotateManager.initBindView(this);//注解式绑定控件
        setData();

    }

    private void setData() {
        tv_cxrq.setText(BaseDataUtils.getNowYearAndMonth());
        if (null!=userInfo.getSfzh()&&!userInfo.getSfzh().isEmpty()){//已经验证通过的身份证号码不允许修改
            et_sfzhm.setText(userInfo.getSfzh());
            et_sfzhm.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cxrq:
                new MonPickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c_start.set(year, monthOfYear, dayOfMonth);
                        tv_cxrq.setText(DateFormat.format("yyyyMM", c_start));
                    }
                }, c_start.get(Calendar.YEAR), c_start.get(Calendar.MONTH), c_start.get(Calendar.DATE)).show();
                break;

            case R.id.btn_query:
                String msg="";
                if (et_sfzhm.getText().toString().isEmpty()||et_sfzhm.getText().toString().length()<15){
                    msg="请输入正确的身份证号码";
                }
                if (!msg.isEmpty()){
                    UI.toast(SalaryMainAty.this,msg);
                }else {
                    if (Integer.parseInt(tv_cxrq.getText().toString())>201704){
                        newRequestData();
                    }else {
                        requestData();
                    }
                }
                break;

        }
    }

    private void newRequestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("MONTH",tv_cxrq.getText().toString());
        requestJson.put("SFZH",et_sfzhm.getText().toString());
        ApiResource.getSalaryNewTotal(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                stopLoading();
                if (i==200){
                    List<SalaryNewModle> salaryNewList = JSON.parseArray(result,SalaryNewModle.class);
                    if (salaryNewList!=null&&salaryNewList.size()>0){
                        SalaryNewModle salarys=salaryNewList.get(0);
                        if (salarys.getREALNAME().equals(userInfo.getName())){
                            Intent intent=new Intent(SalaryMainAty.this,SalaryNewQueryAty.class);
                            intent.putExtra("salary",salarys);
                            startActivity(intent);
                        }else {
                            UI.toast(SalaryMainAty.this,"查询人员信息匹配错误！");
                        }
                    } else {
                        onFailure(i,headers,bytes,null);
                    }
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                if (i==400){
                    UI.toast(SalaryMainAty.this,new String(bytes));
                }else {
                    UI.toast(SalaryMainAty.this,"获取数据失败");
                }
            }
        });
    }


    private void requestData() {
        showLoadding();
        JSONObject requestJson = new JSONObject();
        requestJson.put("MONTH",tv_cxrq.getText().toString());
        requestJson.put("SFZH",et_sfzhm.getText().toString());
        ApiResource.getSalaryTotal(requestJson.toJSONString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                stopLoading();
                if (i==200){
                    List<SalaryModle> salaryList = JSON.parseArray(result,SalaryModle.class);
                    if (salaryList!=null&&salaryList.size()>0){
                        SalaryModle salary=salaryList.get(0);
                        if (salary.getNAME().equals(userInfo.getName())){
                            Intent intent=new Intent(SalaryMainAty.this,SalaryQueryAty.class);
                            intent.putExtra("salary",salary);
                            startActivity(intent);
                        }else {
                            UI.toast(SalaryMainAty.this,"查询人员信息匹配错误！");
                        }
                    } else {
                        onFailure(i,headers,bytes,null);
                        // UI.toast(SalaryMainAty.this,"当前没有该人员的薪资信息！");
                    }
                }else {
                    onFailure(i,headers,bytes,null);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                if (i==400){
                    UI.toast(SalaryMainAty.this,new String(bytes));
                }else {
                    UI.toast(SalaryMainAty.this,"获取数据失败");
                }
            }
        });
    }





    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return getString(R.string.salary_query);
    }

}
