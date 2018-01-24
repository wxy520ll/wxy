package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/6/29.
 */
public class DutyBeatsModle implements Serializable {
    private String BID;//巡段ID
    private String BEAT_LEVEL;//巡段级别
    private String BEAT_TYPE;//巡段类型
    private String BEAT_LENGTH;//巡段长度
    private String SUBS_ID;//分局ID
    private String SUBS_NAME;//分局名称
    private String DEPT_ID;//单位ID
    private String DEPT_NAME;//单位名称
    private String BEAT_DESCRIBE;//巡段描述
    private String BAIDU_COORDINATE;//百度坐标
    private String CREATTIME;//创建时间
    private String UPDATETIME;//更新时间
    private String BID_NAME;//巡段名称
    private String CREATOR_NAME;//创建人姓名
    private int CREATOR_ID;//创建人ID
    private String CREATOR_SJHM;//创建人手机号码
    private String FRID;//排班规则ID
    private String FRNAME;//规则名称




    public String getFRNAME() {
        return FRNAME;
    }

    public void setFRNAME(String FRNAME) {
        this.FRNAME = FRNAME;
    }

    public String getFRID() {
        return FRID;
    }

    public void setFRID(String FRID) {
        this.FRID = FRID;
    }

    public String getBID() {
        return BID;
    }

    public String getBEAT_LEVEL() {
        return BEAT_LEVEL;
    }

    public String getBEAT_TYPE() {
        return BEAT_TYPE;
    }

    public String getBEAT_LENGTH() {
        return BEAT_LENGTH;
    }

    public String getSUBS_ID() {
        return SUBS_ID;
    }

    public String getSUBS_NAME() {
        return SUBS_NAME;
    }

    public String getDEPT_ID() {
        return DEPT_ID;
    }

    public String getDEPT_NAME() {
        return DEPT_NAME;
    }

    public String getBEAT_DESCRIBE() {
        return BEAT_DESCRIBE;
    }

    public String getBAIDU_COORDINATE() {
        return BAIDU_COORDINATE;
    }

    public String getCREATTIME() {
        return CREATTIME;
    }

    public String getUPDATETIME() {
        return UPDATETIME;
    }

    public String getBID_NAME() {
        return BID_NAME;
    }

    public String getCREATOR_NAME() {
        return CREATOR_NAME;
    }

    public int getCREATOR_ID() {
        return CREATOR_ID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public void setBEAT_LEVEL(String BEAT_LEVEL) {
        this.BEAT_LEVEL = BEAT_LEVEL;
    }

    public void setBEAT_TYPE(String BEAT_TYPE) {
        this.BEAT_TYPE = BEAT_TYPE;
    }

    public void setBEAT_LENGTH(String BEAT_LENGTH) {
        this.BEAT_LENGTH = BEAT_LENGTH;
    }

    public void setSUBS_ID(String SUBS_ID) {
        this.SUBS_ID = SUBS_ID;
    }

    public void setSUBS_NAME(String SUBS_NAME) {
        this.SUBS_NAME = SUBS_NAME;
    }

    public void setDEPT_ID(String DEPT_ID) {
        this.DEPT_ID = DEPT_ID;
    }

    public void setDEPT_NAME(String DEPT_NAME) {
        this.DEPT_NAME = DEPT_NAME;
    }

    public void setBEAT_DESCRIBE(String BEAT_DESCRIBE) {
        this.BEAT_DESCRIBE = BEAT_DESCRIBE;
    }

    public void setBAIDU_COORDINATE(String BAIDU_COORDINATE) {
        this.BAIDU_COORDINATE = BAIDU_COORDINATE;
    }

    public void setCREATTIME(String CREATTIME) {
        this.CREATTIME = CREATTIME;
    }

    public void setUPDATETIME(String UPDATETIME) {
        this.UPDATETIME = UPDATETIME;
    }

    public void setBID_NAME(String BID_NAME) {
        this.BID_NAME = BID_NAME;
    }

    public void setCREATOR_NAME(String CREATOR_NAME) {
        this.CREATOR_NAME = CREATOR_NAME;
    }

    public void setCREATOR_ID(int CREATOR_ID) {
        this.CREATOR_ID = CREATOR_ID;
    }

    public String getCREATOR_SJHM() {
        return CREATOR_SJHM;
    }

    public void setCREATOR_SJHM(String CREATOR_SJHM) {
        this.CREATOR_SJHM = CREATOR_SJHM;
    }
}
