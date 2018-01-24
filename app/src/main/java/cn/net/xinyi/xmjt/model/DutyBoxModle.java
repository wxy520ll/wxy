package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/7/1.
 */
public class DutyBoxModle implements Serializable {
    private  String SID;//区域ID
    private  String F_BID;//巡段ID
    private  String SIGNBOX_DESCRIBE;//位置描述
    private  double BAIDU_COORDINATE_X;//百度坐标 X
    private  double BAIDU_COORDINATE_Y;//百度坐标 Y
    private  String SID_NAME;//百度坐标 Y
    private  int ISXL;//判断该点位是否在巡逻
    private  String CREATETIME;

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public int getISXL() {
        return ISXL;
    }

    public void setISXL(int ISXL) {
        this.ISXL = ISXL;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getF_BID() {
        return F_BID;
    }

    public void setF_BID(String f_BID) {
        F_BID = f_BID;
    }

    public String getSIGNBOX_DESCRIBE() {
        return SIGNBOX_DESCRIBE;
    }

    public void setSIGNBOX_DESCRIBE(String SIGNBOX_DESCRIBE) {
        this.SIGNBOX_DESCRIBE = SIGNBOX_DESCRIBE;
    }

    public double getBAIDU_COORDINATE_X() {
        return BAIDU_COORDINATE_X;
    }

    public void setBAIDU_COORDINATE_X(double BAIDU_COORDINATE_X) {
        this.BAIDU_COORDINATE_X = BAIDU_COORDINATE_X;
    }

    public double getBAIDU_COORDINATE_Y() {
        return BAIDU_COORDINATE_Y;
    }

    public void setBAIDU_COORDINATE_Y(double BAIDU_COORDINATE_Y) {
        this.BAIDU_COORDINATE_Y = BAIDU_COORDINATE_Y;
    }

    public String getSID_NAME() {
        return SID_NAME;
    }

    public void setSID_NAME(String SID_NAME) {
        this.SID_NAME = SID_NAME;
    }

    @Override
    public String toString() {
        return "DutyBoxModle{" +
                "SID='" + SID + '\'' +
                ", F_BID='" + F_BID + '\'' +
                ", SIGNBOX_DESCRIBE='" + SIGNBOX_DESCRIBE + '\'' +
                ", BAIDU_COORDINATE_X=" + BAIDU_COORDINATE_X +
                ", BAIDU_COORDINATE_Y=" + BAIDU_COORDINATE_Y +
                ", SID_NAME='" + SID_NAME + '\'' +
                ", ISXL=" + ISXL +
                ", CREATETIME='" + CREATETIME + '\'' +
                '}';
    }
}
