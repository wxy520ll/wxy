package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/10/1.
 */
public class SalaryModle implements Serializable {

    private long  ID;//主键：身份证+月份
    private String MONTH;//月份
    private String  FILE_ID;//档案号
    private String  DEPARTMENT_NAME;// 部门
    private String  POST_NAME;// 职务
    private String  JOB_NAME;//岗位
    private String  NAME;//姓名
    private String  ID_CARD_NO;//身份证号
    private String  ACCOUNT_NO;// 帐号
    private String  BANK_NAME;// 银行名称
    private String  BANK_NO;//银行账号
    private String  LEAVE_DEDUCT_MONEY;//   请假扣款
    private String  SPECIAL_WAGE;//   特殊工资
    private String  WORK_AGE_WAGE;// 工龄工资
    private String  COLD_ALLOWANCE;// 降温津贴
    private String  POST_ALLOWANCE;//职务津贴
    private String  OVERTIME_WAGE;//  加班工资
    private String  TIMING_WAGE;//正时工资
    private String  JOB_WAGE;// 岗位工资
    private String  INCOME_TAX_MONEY;//个人所得税
    private String  PERSONAL_FUND_MONEY;// 公积金个人扣款
    private String  SOCIAL_SECURITY_MONEY;//  社保个人扣款
    private String  AFTER_TAX_WAGE;// 税后工资
    private String  SHOULD_GET_WAGE;//应发工资
    private String  HOLIDAY_OVERTIME_MONEY;//节假日工资
    private String  PERFORMANCE_WAGE;//绩效工资
    private String  PERFORMANCE_ALLOWANCE;// 绩效津贴
    private String  MOTO_TAX_MONEY;// 摩托车费用
    private String  REAL_ALL_MONEY;//当月合计

    public SalaryModle() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getFILE_ID() {
        return FILE_ID;
    }

    public void setFILE_ID(String FILE_ID) {
        this.FILE_ID = FILE_ID;
    }

    public String getDEPARTMENT_NAME() {
        return DEPARTMENT_NAME;
    }

    public void setDEPARTMENT_NAME(String DEPARTMENT_NAME) {
        this.DEPARTMENT_NAME = DEPARTMENT_NAME;
    }

    public String getPOST_NAME() {
        return POST_NAME;
    }

    public void setPOST_NAME(String POST_NAME) {
        this.POST_NAME = POST_NAME;
    }

    public String getJOB_NAME() {
        return JOB_NAME;
    }

    public void setJOB_NAME(String JOB_NAME) {
        this.JOB_NAME = JOB_NAME;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getID_CARD_NO() {
        return ID_CARD_NO;
    }

    public void setID_CARD_NO(String ID_CARD_NO) {
        this.ID_CARD_NO = ID_CARD_NO;
    }

    public String getACCOUNT_NO() {
        return ACCOUNT_NO;
    }

    public void setACCOUNT_NO(String ACCOUNT_NO) {
        this.ACCOUNT_NO = ACCOUNT_NO;
    }

    public String getBANK_NAME() {
        return BANK_NAME;
    }

    public void setBANK_NAME(String BANK_NAME) {
        this.BANK_NAME = BANK_NAME;
    }

    public String getBANK_NO() {
        return BANK_NO;
    }

    public void setBANK_NO(String BANK_NO) {
        this.BANK_NO = BANK_NO;
    }

    public String getLEAVE_DEDUCT_MONEY() {
        return LEAVE_DEDUCT_MONEY;
    }

    public void setLEAVE_DEDUCT_MONEY(String LEAVE_DEDUCT_MONEY) {
        this.LEAVE_DEDUCT_MONEY = LEAVE_DEDUCT_MONEY;
    }

    public String getSPECIAL_WAGE() {
        return SPECIAL_WAGE;
    }

    public void setSPECIAL_WAGE(String SPECIAL_WAGE) {
        this.SPECIAL_WAGE = SPECIAL_WAGE;
    }

    public String getWORK_AGE_WAGE() {
        return WORK_AGE_WAGE;
    }

    public void setWORK_AGE_WAGE(String WORK_AGE_WAGE) {
        this.WORK_AGE_WAGE = WORK_AGE_WAGE;
    }

    public String getCOLD_ALLOWANCE() {
        return COLD_ALLOWANCE;
    }

    public void setCOLD_ALLOWANCE(String COLD_ALLOWANCE) {
        this.COLD_ALLOWANCE = COLD_ALLOWANCE;
    }

    public String getPOST_ALLOWANCE() {
        return POST_ALLOWANCE;
    }

    public void setPOST_ALLOWANCE(String POST_ALLOWANCE) {
        this.POST_ALLOWANCE = POST_ALLOWANCE;
    }

    public String getOVERTIME_WAGE() {
        return OVERTIME_WAGE;
    }

    public void setOVERTIME_WAGE(String OVERTIME_WAGE) {
        this.OVERTIME_WAGE = OVERTIME_WAGE;
    }

    public String getTIMING_WAGE() {
        return TIMING_WAGE;
    }

    public void setTIMING_WAGE(String TIMING_WAGE) {
        this.TIMING_WAGE = TIMING_WAGE;
    }

    public String getJOB_WAGE() {
        return JOB_WAGE;
    }

    public void setJOB_WAGE(String JOB_WAGE) {
        this.JOB_WAGE = JOB_WAGE;
    }

    public String getINCOME_TAX_MONEY() {
        return INCOME_TAX_MONEY;
    }

    public void setINCOME_TAX_MONEY(String INCOME_TAX_MONEY) {
        this.INCOME_TAX_MONEY = INCOME_TAX_MONEY;
    }

    public String getPERSONAL_FUND_MONEY() {
        return PERSONAL_FUND_MONEY;
    }

    public void setPERSONAL_FUND_MONEY(String PERSONAL_FUND_MONEY) {
        this.PERSONAL_FUND_MONEY = PERSONAL_FUND_MONEY;
    }

    public String getSOCIAL_SECURITY_MONEY() {
        return SOCIAL_SECURITY_MONEY;
    }

    public void setSOCIAL_SECURITY_MONEY(String SOCIAL_SECURITY_MONEY) {
        this.SOCIAL_SECURITY_MONEY = SOCIAL_SECURITY_MONEY;
    }

    public String getAFTER_TAX_WAGE() {
        return AFTER_TAX_WAGE;
    }

    public void setAFTER_TAX_WAGE(String AFTER_TAX_WAGE) {
        this.AFTER_TAX_WAGE = AFTER_TAX_WAGE;
    }

    public String getSHOULD_GET_WAGE() {
        return SHOULD_GET_WAGE;
    }

    public void setSHOULD_GET_WAGE(String SHOULD_GET_WAGE) {
        this.SHOULD_GET_WAGE = SHOULD_GET_WAGE;
    }

    public String getHOLIDAY_OVERTIME_MONEY() {
        return HOLIDAY_OVERTIME_MONEY;
    }

    public void setHOLIDAY_OVERTIME_MONEY(String HOLIDAY_OVERTIME_MONEY) {
        this.HOLIDAY_OVERTIME_MONEY = HOLIDAY_OVERTIME_MONEY;
    }

    public String getPERFORMANCE_WAGE() {
        return PERFORMANCE_WAGE;
    }

    public void setPERFORMANCE_WAGE(String PERFORMANCE_WAGE) {
        this.PERFORMANCE_WAGE = PERFORMANCE_WAGE;
    }

    public String getPERFORMANCE_ALLOWANCE() {
        return PERFORMANCE_ALLOWANCE;
    }

    public void setPERFORMANCE_ALLOWANCE(String PERFORMANCE_ALLOWANCE) {
        this.PERFORMANCE_ALLOWANCE = PERFORMANCE_ALLOWANCE;
    }

    public String getMOTO_TAX_MONEY() {
        return MOTO_TAX_MONEY;
    }

    public void setMOTO_TAX_MONEY(String MOTO_TAX_MONEY) {
        this.MOTO_TAX_MONEY = MOTO_TAX_MONEY;
    }

    public String getREAL_ALL_MONEY() {
        return REAL_ALL_MONEY;
    }

    public void setREAL_ALL_MONEY(String REAL_ALL_MONEY) {
        this.REAL_ALL_MONEY = REAL_ALL_MONEY;
    }
}
