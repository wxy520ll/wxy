package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/6/23.
 */
public class PloceMapModle implements Serializable {
    private String CJYH; //采集用户手机号码
    private String NAME; //名字
    private Double WD; //纬度
    private Double JD; //经度
    private String ZDZ; //派出所名字
    private String ACCOUNTTYPE; //人员身份
    private String SCSJ; //上传时间


    public String getCJYH() {
        return CJYH;
    }

    public void setCJYH(String CJYH) {
        this.CJYH = CJYH;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Double getWD() {
        return WD;
    }

    public void setWD(Double WD) {
        this.WD = WD;
    }

    public Double getJD() {
        return JD;
    }

    public void setJD(Double JD) {
        this.JD = JD;
    }

    public String getZDZ() {
        return ZDZ;
    }

    public void setZDZ(String ZDZ) {
        this.ZDZ = ZDZ;
    }

    public String getACCOUNTTYPE() {
        return ACCOUNTTYPE;
    }

    public void setACCOUNTTYPE(String ACCOUNTTYPE) {
        this.ACCOUNTTYPE = ACCOUNTTYPE;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }
}
