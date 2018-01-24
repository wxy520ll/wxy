package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/4/11.
 */
public class KQModle implements Serializable{
    private int ID;
    /***类型(上班or下班)*/
    private String LX;
    /***年月日*/
    private String YEAR;
    /***时分秒*/
    private String DATA;
    /***星期*/
    private String WEEK;
    /***地址*/
    private String DZ;
    /***纬度*/
    private Double WD;
    /***经度*/
    private Double JD;
    /***照片1*/
    private String IV_ZP;
    /***采集时间*/
    private String CJSJ;
    /***定位类型*/
    private  int LOCTYPE;
    /***上传时间**/
    private String SCSJ;

    public KQModle() {
    }

    public String getLX() {
        return LX;
    }

    public void setLX(String LX) {
        this.LX = LX;
    }

    public int getID() {
        return ID;
    }

    public String getYEAR() {
        return YEAR;
    }

    public String getDATA() {
        return DATA;
    }

    public String getWEEK() {
        return WEEK;
    }

    public String getDZ() {
        return DZ;
    }

    public Double getWD() {
        return WD;
    }

    public Double getJD() {
        return JD;
    }

    public String getIV_ZP() {
        return IV_ZP;
    }

    public String getCJSJ() {
        return CJSJ;
    }

    public int getLOCTYPE() {
        return LOCTYPE;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public void setWEEK(String WEEK) {
        this.WEEK = WEEK;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public void setWD(Double WD) {
        this.WD = WD;
    }

    public void setJD(Double JD) {
        this.JD = JD;
    }

    public void setIV_ZP(String IV_ZP) {
        this.IV_ZP = IV_ZP;
    }

    public void setCJSJ(String CJSJ) {
        this.CJSJ = CJSJ;
    }

    public void setLOCTYPE(int LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }
}
