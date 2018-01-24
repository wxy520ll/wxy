package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by zhouhao on 2017/2/28.
 * 寄递物流
 */

public class LogisticsDeliveryCheckModle implements Serializable {
    private int ID ;//编号
    private int ZAJCXXID;// 采集信息ID
    private int SFKXYS;//收件是否开箱验视
    private int SFSMDJ;//收件是否实名登记
    private int SFCJRYXX ;//从业人员是否到公安机关进行人员信息采集
    private int SFCZSG;//工作人员是否持证上岗
    private int SFKZPXHD;//是否开展安全培训活动
    private int SFLSABZD;//是否落实内部安保制度
    private int SFZTXCZL;//是否张贴有奖举报等宣传资料
    private int SFJKBCSST;// 视频监控是否安装并至少保存30天
    private int SFYXGJJC;//中转站、集散仓库是否有X光机检查
    private int SFGSAZAPP ;//寄递公司是否安装快递卫士APP
    private int SFRYAZAPP;//寄递公司从业人员是否全部安装快递卫士APP
    private int SFZQSYAPP;//是否按要求使用快递卫士APP收件
    private String HCSJ;//核查时间
    private String HCYH;// 核查人员
    private String HCDW;//核查单位
    private String NAME;//名字

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setZAJCXXID(int ZAJCXXID) {
        this.ZAJCXXID = ZAJCXXID;
    }

    public void setSFKXYS(int SFKXYS) {
        this.SFKXYS = SFKXYS;
    }

    public void setSFSMDJ(int SFSMDJ) {
        this.SFSMDJ = SFSMDJ;
    }

    public void setSFCJRYXX(int SFCJRYXX) {
        this.SFCJRYXX = SFCJRYXX;
    }

    public void setSFCZSG(int SFCZSG) {
        this.SFCZSG = SFCZSG;
    }

    public void setSFKZPXHD(int SFKZPXHD) {
        this.SFKZPXHD = SFKZPXHD;
    }

    public void setSFLSABZD(int SFLSABZD) {
        this.SFLSABZD = SFLSABZD;
    }

    public void setSFZTXCZL(int SFZTXCZL) {
        this.SFZTXCZL = SFZTXCZL;
    }

    public void setSFJKBCSST(int SFJKBCSST) {
        this.SFJKBCSST = SFJKBCSST;
    }

    public void setSFYXGJJC(int SFYXGJJC) {
        this.SFYXGJJC = SFYXGJJC;
    }

    public void setSFGSAZAPP(int SFGSAZAPP) {
        this.SFGSAZAPP = SFGSAZAPP;
    }

    public void setSFRYAZAPP(int SFRYAZAPP) {
        this.SFRYAZAPP = SFRYAZAPP;
    }

    public void setSFZQSYAPP(int SFZQSYAPP) {
        this.SFZQSYAPP = SFZQSYAPP;
    }

    public void setHCSJ(String HCSJ) {
        this.HCSJ = HCSJ;
    }

    public void setHCYH(String HCYH) {
        this.HCYH = HCYH;
    }

    public void setHCDW(String HCDW) {
        this.HCDW = HCDW;
    }

    public int getID() {
        return ID;
    }

    public int getZAJCXXID() {
        return ZAJCXXID;
    }

    public int getSFKXYS() {
        return SFKXYS;
    }

    public int getSFSMDJ() {
        return SFSMDJ;
    }

    public int getSFCJRYXX() {
        return SFCJRYXX;
    }

    public int getSFCZSG() {
        return SFCZSG;
    }

    public int getSFKZPXHD() {
        return SFKZPXHD;
    }

    public int getSFLSABZD() {
        return SFLSABZD;
    }

    public int getSFZTXCZL() {
        return SFZTXCZL;
    }

    public int getSFJKBCSST() {
        return SFJKBCSST;
    }

    public int getSFYXGJJC() {
        return SFYXGJJC;
    }

    public int getSFGSAZAPP() {
        return SFGSAZAPP;
    }

    public int getSFRYAZAPP() {
        return SFRYAZAPP;
    }

    public int getSFZQSYAPP() {
        return SFZQSYAPP;
    }

    public String getHCSJ() {
        return HCSJ;
    }

    public String getHCYH() {
        return HCYH;
    }

    public String getHCDW() {
        return HCDW;
    }
}
