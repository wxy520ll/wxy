package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by studyjun on 2016/4/19.
 */
public class PoliceBoxModle implements Serializable{

    private int ID; //ID
    private String TYPE; //岗亭类别
    private String STREET; //街道
    private String ADDRESS; //地址
    private String UNIFIEDNO; //统一编号
    private String DEMAND; //建设需求
    private String INITIATOR; //领头部门
    private String POLICESTATION; //所属派出所
    private String fINISHTIME; //完成时间
    private String WATCHOVERUNIT; //值守单位
    private String WORKTIME; //勤务模式
    private double LAT; //纬度
    private double LNGT; //经度
    private String LATLONGT; //经纬度
    private String LINKMAN; //联系人
    private String PHONENO; //联系人电话
    private String INTERPHONETYPE; //对讲机类型
    private String INTERPHONENO; //对讲机号码
    private int ZSZT; //0从未值守过1开始值守，2结束值守，3离岗报备
    private String IMG1;//图片1
    private String IMG2;//图片2
    private String SCSJ;//上传时间
    private String ZSRY;//值守人员
    private String FRNAME;//规则名字、

    public String getFRNAME() {
        return FRNAME;
    }

    public void setFRNAME(String FRNAME) {
        this.FRNAME = FRNAME;
    }

    public String getZSRY() {
        return ZSRY;
    }

    public void setZSRY(String ZSRY) {
        this.ZSRY = ZSRY;
    }

    public String getSCSJ() {
        return SCSJ;
    }
    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }
    public String getIMG1() {
        return IMG1;
    }

    public void setIMG1(String IMG1) {
        this.IMG1 = IMG1;
    }

    public String getIMG2() {
        return IMG2;
    }

    public void setIMG2(String IMG2) {
        this.IMG2 = IMG2;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSTREET() {
        return STREET;
    }

    public void setSTREET(String STREET) {
        this.STREET = STREET;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getUNIFIEDNO() {
        return UNIFIEDNO;
    }

    public void setUNIFIEDNO(String UNIFIEDNO) {
        this.UNIFIEDNO = UNIFIEDNO;
    }

    public String getDEMAND() {
        return DEMAND;
    }

    public void setDEMAND(String DEMAND) {
        this.DEMAND = DEMAND;
    }

    public String getINITIATOR() {
        return INITIATOR;
    }

    public void setINITIATOR(String INITIATOR) {
        this.INITIATOR = INITIATOR;
    }

    public String getPOLICESTATION() {
        return POLICESTATION;
    }

    public void setPOLICESTATION(String POLICESTATION) {
        this.POLICESTATION = POLICESTATION;
    }

    public String getfINISHTIME() {
        return fINISHTIME;
    }

    public void setfINISHTIME(String fINISHTIME) {
        this.fINISHTIME = fINISHTIME;
    }

    public String getWATCHOVERUNIT() {
        return WATCHOVERUNIT;
    }

    public void setWATCHOVERUNIT(String WATCHOVERUNIT) {
        this.WATCHOVERUNIT = WATCHOVERUNIT;
    }

    public String getWORKTIME() {
        return WORKTIME;
    }

    public void setWORKTIME(String WORKTIME) {
        this.WORKTIME = WORKTIME;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public double getLNGT() {
        return LNGT;
    }

    public void setLNGT(double LNGT) {
        this.LNGT = LNGT;
    }

    public String getLATLONGT() {
        return LATLONGT;
    }

    public void setLATLONGT(String LATLONGT) {
        this.LATLONGT = LATLONGT;
    }

    public String getLINKMAN() {
        return LINKMAN;
    }

    public void setLINKMAN(String LINKMAN) {
        this.LINKMAN = LINKMAN;
    }

    public String getPHONENO() {
        return PHONENO;
    }

    public void setPHONENO(String PHONENO) {
        this.PHONENO = PHONENO;
    }

    public String getINTERPHONETYPE() {
        return INTERPHONETYPE;
    }

    public void setINTERPHONETYPE(String INTERPHONETYPE) {
        this.INTERPHONETYPE = INTERPHONETYPE;
    }

    public String getINTERPHONENO() {
        return INTERPHONENO;
    }

    public void setINTERPHONENO(String INTERPHONENO) {
        this.INTERPHONENO = INTERPHONENO;
    }

    public int getZSZT() {
        return ZSZT;
    }

    public void setZSZT(int ZSZT) {
        this.ZSZT = ZSZT;
    }

    public PoliceBoxModle() {
    }

    public PoliceBoxModle(int ID, String TYPE, String STREET, String ADDRESS, String UNIFIEDNO, String DEMAND, String INITIATOR, String POLICESTATION, String fINISHTIME, String WATCHOVERUNIT, String WORKTIME, double LAT, double LNGT, String LATLONGT, String LINKMAN, String PHONENO, String INTERPHONETYPE, String INTERPHONENO) {
        this.ID = ID;
        this.TYPE = TYPE;
        this.STREET = STREET;
        this.ADDRESS = ADDRESS;
        this.UNIFIEDNO = UNIFIEDNO;
        this.DEMAND = DEMAND;
        this.INITIATOR = INITIATOR;
        this.POLICESTATION = POLICESTATION;
        this.fINISHTIME = fINISHTIME;
        this.WATCHOVERUNIT = WATCHOVERUNIT;
        this.WORKTIME = WORKTIME;
        this.LAT = LAT;
        this.LNGT = LNGT;
        this.LATLONGT = LATLONGT;
        this.LINKMAN = LINKMAN;
        this.PHONENO = PHONENO;
        this.INTERPHONETYPE = INTERPHONETYPE;
        this.INTERPHONENO = INTERPHONENO;
    }

    @Override
    public String toString() {
        return getUNIFIEDNO();
    }





}
