package cn.net.xinyi.xmjt.v527.presentation.task.csxc.model;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2017/12/31.
 */

public class ZajcModel implements Serializable, Cloneable {
    //@JSONField(name = "SCZWT")
    private String sczwt;
    //@JSONField(name = "SMJCLYJ")
    private String smjclyj;
    private String PD_ID;//任务id
    // @JSONField(name = "SYSTEMID")
    private String systemid;
    private String resultDetail;//155,0;213,0;214,0;219,3
    private String qmzp;//155,0;213,0;214,0;219,3
    private String jczp;//155,0;213,0;214,0;219,3

    public String getQmzp() {
        return qmzp;
    }

    public void setQmzp(String qmzp) {
        this.qmzp = qmzp;
    }

    public String getJczp() {
        return jczp;
    }

    public void setJczp(String jczp) {
        this.jczp = jczp;
    }

    public String getSczwt() {
        return sczwt;
    }

    public void setSczwt(String sczwt) {
        this.sczwt = sczwt;
    }

    public String getSmjclyj() {
        return smjclyj;
    }

    public void setSmjclyj(String smjclyj) {
        this.smjclyj = smjclyj;
    }

    public String getSystemid() {
        return systemid;
    }

    public void setSystemid(String systemid) {
        this.systemid = systemid;
    }

    public String getPD_ID() {
        return PD_ID;
    }

    public void setPD_ID(String PD_ID) {
        this.PD_ID = PD_ID;
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    @Override
    public ZajcModel clone() {
        try {
            return (ZajcModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }
    }
}
