package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/7/4.
 */
public class DutyOperationModle implements Serializable {
    private String ID;
    private String DUTY_OPR_TYPE;//勤务操作类型 01 巡逻 02 签到 03 报备 04 出警
    private String DUTY_OPR_NAME;//勤务操作名称 01 巡逻 02 签到 03 报备 04 出警
    private String USERID;//APP用户ID
    private double LON;//经度（百度坐标）
    private double LAT;//纬度（百度坐标）
    private String ADDRESS;//地址
    private String SHOWNAME;//APP用户名
    private String TEL_NUMBER;//联系电话
    private String PARENT_ID;//父ID
    private String DUTY_BEATS_ID;//巡逻段ID
    private String DUTY_SIGNBOX_ID;//签到箱ID
    private String DUTY_SIGNBOX_NAME;//签到箱名称
    private String DISTANCE;//距离
    private String LAST_TYPE;//最新状态
    private String DESCRIPTION;//报备内容
    private String CREATETIME;//创建时间
    private String ENDDTIME;//结束时间
    private int TOTALTIME;//勤务时间
    private String PLATENO;//车牌号码


    public String getPLATENO() {
        return PLATENO;
    }

    public void setPLATENO(String PLATENO) {
        this.PLATENO = PLATENO;
    }

    public int getTOTALTIME() {
        return TOTALTIME;
    }

    public void setTOTALTIME(int TOTALTIME) {
        this.TOTALTIME = TOTALTIME;
    }

    public String getENDDTIME() {
        return ENDDTIME;
    }

    public void setENDDTIME(String ENDDTIME) {
        this.ENDDTIME = ENDDTIME;
    }

    public String getDUTY_SIGNBOX_NAME() {
        return DUTY_SIGNBOX_NAME;
    }

    public void setDUTY_SIGNBOX_NAME(String DUTY_SIGNBOX_NAME) {
        this.DUTY_SIGNBOX_NAME = DUTY_SIGNBOX_NAME;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public void setDUTY_OPR_TYPE(String DUTY_OPR_TYPE) {
        this.DUTY_OPR_TYPE = DUTY_OPR_TYPE;
    }

    public void setDUTY_OPR_NAME(String DUTY_OPR_NAME) {
        this.DUTY_OPR_NAME = DUTY_OPR_NAME;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public void setLON(double LON) {
        this.LON = LON;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public void setSHOWNAME(String SHOWNAME) {
        this.SHOWNAME = SHOWNAME;
    }

    public void setTEL_NUMBER(String TEL_NUMBER) {
        this.TEL_NUMBER = TEL_NUMBER;
    }

    public void setPARENT_ID(String PARENT_ID) {
        this.PARENT_ID = PARENT_ID;
    }

    public void setDUTY_BEATS_ID(String DUTY_BEATS_ID) {
        this.DUTY_BEATS_ID = DUTY_BEATS_ID;
    }

    public void setDUTY_SIGNBOX_ID(String DUTY_SIGNBOX_ID) {
        this.DUTY_SIGNBOX_ID = DUTY_SIGNBOX_ID;
    }

    public void setDISTANCE(String DISTANCE) {
        this.DISTANCE = DISTANCE;
    }

    public void setLAST_TYPE(String LAST_TYPE) {
        this.LAST_TYPE = LAST_TYPE;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getDUTY_OPR_TYPE() {
        return DUTY_OPR_TYPE;
    }

    public String getDUTY_OPR_NAME() {
        return DUTY_OPR_NAME;
    }

    public String getUSERID() {
        return USERID;
    }

    public double getLON() {
        return LON;
    }

    public double getLAT() {
        return LAT;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getSHOWNAME() {
        return SHOWNAME;
    }

    public String getTEL_NUMBER() {
        return TEL_NUMBER;
    }

    public String getPARENT_ID() {
        return PARENT_ID;
    }

    public String getDUTY_BEATS_ID() {
        return DUTY_BEATS_ID;
    }

    public String getDUTY_SIGNBOX_ID() {
        return DUTY_SIGNBOX_ID;
    }

    public String getDISTANCE() {
        return DISTANCE;
    }

    public String getLAST_TYPE() {
        return LAST_TYPE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
