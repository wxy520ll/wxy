package cn.net.xinyi.xmjt.activity.Main.Setting;


import android.os.Bundle;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.model.SalaryModle;
import cn.net.xinyi.xmjt.utils.AnnotateManager;
import cn.net.xinyi.xmjt.utils.BindView;

public class SalaryQueryAty extends BaseActivity2 {

    private SalaryModle salary;
    //
    @BindView(id = R.id.tv_month)
    private TextView tv_month;
    //
    @BindView(id = R.id.tv_name)
    private TextView tv_name;
    //
    @BindView(id = R.id.tv_dep)
    private TextView tv_dep;
    //
    @BindView(id = R.id.tv_post_name)
    private TextView tv_post_name;
    //
    @BindView(id = R.id.tv_job_name)
    private TextView tv_job_name;
    //
    @BindView(id = R.id.tv_bank_number)
    private TextView tv_bank_number;
    //
    @BindView(id = R.id.tv_leave_deduct_monet)
    private TextView tv_leave_deduct_monet;
    //
    @BindView(id = R.id.tv_leave_special_wage)
    private TextView tv_leave_special_wage;
    //
    @BindView(id = R.id.tv_work_wage)
    private TextView tv_work_wage;
    //
    @BindView(id = R.id.tv_cold_allowance)
    private TextView tv_cold_allowance;
    //
    @BindView(id = R.id.tv_post_allowance)
    private TextView tv_post_allowance;
    //
    @BindView(id = R.id.tv_overtime_wage)
    private TextView tv_overtime_wage;
    //
    @BindView(id = R.id.tv_timing_wage)
    private TextView tv_timing_wage;
    //
    @BindView(id = R.id.tv_job_wage)
    private TextView tv_job_wage;
    //
    @BindView(id = R.id.tv_income_tax_money)
    private TextView tv_income_tax_money;
    //
    @BindView(id = R.id.tv_personal_fund_money)
    private TextView tv_personal_fund_money;
    //
    @BindView(id = R.id.tv_social_security_money)
    private TextView tv_social_security_money;
    //
    @BindView(id = R.id.tv_after_tax_wage)
    private TextView tv_after_tax_wage;
    //
    @BindView(id = R.id.tv_hollday_overtime_money)
    private TextView tv_hollday_overtime_money;
    //
    @BindView(id = R.id.tv_should_get_wage)
    private TextView tv_should_get_wage;
    //
    @BindView(id = R.id.tv_performance_wage)
    private TextView tv_performance_wage;
    //
    @BindView(id = R.id.tv_performance_allowance)
    private TextView tv_performance_allowance;
    //
    @BindView(id = R.id.tv_moto_tax_moneye)
    private TextView tv_moto_tax_moneye;
    //
    @BindView(id = R.id.tv_real_all_money)
    private TextView tv_real_all_money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_salary_query);
        AnnotateManager.initBindView(this);//注解式绑定控件
        salary= (SalaryModle) getIntent().getSerializableExtra("salary");
        setData();
    }

    private void setData() {
        //tv_month/tv_name/tv_dep/tv_post_name/tv_job_name/tv_bank_number/tv_leave_deduct_monet
        //tv_leave_special_wage/tv_work_wage/tv_cold_allowance/tv_post_allowance/tv_overtime_wage
        //tv_timing_wage/tv_job_wage/tv_income_tax_money/tv_personal_fund_money/tv_social_security_money
        //tv_after_tax_wage/tv_hollday_overtime_money/tv_should_get_wage/tv_performance_wage
        //tv_performance_allowance/tv_moto_tax_moneye/tv_real_all_money
        tv_month.setText(salary.getMONTH());
        tv_name.setText(salary.getNAME());
        tv_dep.setText(salary.getDEPARTMENT_NAME());
        tv_post_name.setText(salary.getPOST_NAME());
        tv_job_name.setText(salary.getJOB_NAME());
        tv_bank_number.setText(salary.getBANK_NO()+"("+salary.getBANK_NAME()+")");
        tv_leave_deduct_monet.setText(salary.getLEAVE_DEDUCT_MONEY());

        tv_leave_special_wage.setText(salary.getSPECIAL_WAGE());
        tv_work_wage.setText(salary.getWORK_AGE_WAGE());
        tv_cold_allowance.setText(salary.getCOLD_ALLOWANCE());
        tv_post_allowance.setText(salary.getPOST_ALLOWANCE());
        tv_overtime_wage.setText(salary.getOVERTIME_WAGE());

        tv_timing_wage.setText(salary.getTIMING_WAGE());
        tv_job_wage.setText(salary.getJOB_WAGE());
        tv_income_tax_money.setText(salary.getINCOME_TAX_MONEY());
        tv_personal_fund_money.setText(salary.getPERSONAL_FUND_MONEY());
        tv_social_security_money.setText(salary.getSOCIAL_SECURITY_MONEY());

        tv_after_tax_wage.setText(salary.getAFTER_TAX_WAGE());
        tv_hollday_overtime_money.setText(salary.getHOLIDAY_OVERTIME_MONEY());
        tv_should_get_wage.setText(salary.getSHOULD_GET_WAGE());
        tv_performance_wage.setText(salary.getPERFORMANCE_WAGE());

        tv_performance_allowance.setText(salary.getPERFORMANCE_ALLOWANCE());
        tv_moto_tax_moneye.setText(salary.getMOTO_TAX_MONEY());
        tv_real_all_money.setText(salary.getREAL_ALL_MONEY());

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
