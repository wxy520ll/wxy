package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by zhouhao on 2017/6/9.
 */

public class SalaryNewModle implements Serializable {
    private int ID;
    private int  TOTALWAGES;//合计应发
    private int  FAX;//扣税
    private int  HOUSEFUND;//住房公积金
    private int  SOCIALFUND;//社保
    private int  BASEWAGES;//基本工资
    private int  GWGZ;//岗位工资
    private int  ZJBT;//职级补贴
    private int  JXGZ;//绩效工资
    private int  ZWJT;//职务津贴
    private int  WORKAGEWAGE;//工龄工资
    private int  GWBT;//高温补贴
    private int  DATAYEAR;//年份
    private int  DATAMONTH;//月份
    private String CARDNO;//证件号码
    private String REALNAME;//姓名
    private String ORGANNAME;//单位
    private int  DEDUCTALL;//合计扣款
    private int  SHOUDGIVE;//实发工资
    private int  USERORGAN;//单位ID
    private int  EMPLOYEEID	;//聘员ID
    private String BANKNO;//银行卡号
    private String POSTTYPE	;//岗位类别
    private String POSTNAME	;//岗位
    private int  ZMJBGZ;//周末加班工资
    private int  FDJRJBGZ;//法定假日加班工资
    private int  JBGZ;//加班工资汇总
    private int  SHGZ;//税后工资
    private int  BANK;//银行类型
    private int INFILEFLAG;//是否存档(1.未存档  2.存档)
    private int OTHERWAGE;//其他工资
    private String OTHERWAGEMARK;//其他工资备注(特殊情况处理，比如上月有误本月补发等等..)
    private int TSGWBT;//特殊岗位补贴
    private int XJGBT;//小教官补贴
    private int  WORKAGE;//工龄
    private int  SFLZGZ	;//1	是否离职工资(1正常工资  2离职工资)
    private String MONTHPERFORM	;//	当月绩效
    private int CHUSHU	;//
    private int CHENGSHU;//
    private int SPECIALFLAG	;//

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTOTALWAGES() {
        return TOTALWAGES;
    }

    public void setTOTALWAGES(int TOTALWAGES) {
        this.TOTALWAGES = TOTALWAGES;
    }

    public int getFAX() {
        return FAX;
    }

    public void setFAX(int FAX) {
        this.FAX = FAX;
    }

    public int getHOUSEFUND() {
        return HOUSEFUND;
    }

    public void setHOUSEFUND(int HOUSEFUND) {
        this.HOUSEFUND = HOUSEFUND;
    }

    public int getSOCIALFUND() {
        return SOCIALFUND;
    }

    public void setSOCIALFUND(int SOCIALFUND) {
        this.SOCIALFUND = SOCIALFUND;
    }

    public int getBASEWAGES() {
        return BASEWAGES;
    }

    public void setBASEWAGES(int BASEWAGES) {
        this.BASEWAGES = BASEWAGES;
    }

    public int getGWGZ() {
        return GWGZ;
    }

    public void setGWGZ(int GWGZ) {
        this.GWGZ = GWGZ;
    }

    public int getZJBT() {
        return ZJBT;
    }

    public void setZJBT(int ZJBT) {
        this.ZJBT = ZJBT;
    }

    public int getJXGZ() {
        return JXGZ;
    }

    public void setJXGZ(int JXGZ) {
        this.JXGZ = JXGZ;
    }

    public int getZWJT() {
        return ZWJT;
    }

    public void setZWJT(int ZWJT) {
        this.ZWJT = ZWJT;
    }

    public int getWORKAGEWAGE() {
        return WORKAGEWAGE;
    }

    public void setWORKAGEWAGE(int WORKAGEWAGE) {
        this.WORKAGEWAGE = WORKAGEWAGE;
    }

    public int getGWBT() {
        return GWBT;
    }

    public void setGWBT(int GWBT) {
        this.GWBT = GWBT;
    }

    public int getDATAYEAR() {
        return DATAYEAR;
    }

    public void setDATAYEAR(int DATAYEAR) {
        this.DATAYEAR = DATAYEAR;
    }

    public int getDATAMONTH() {
        return DATAMONTH;
    }

    public void setDATAMONTH(int DATAMONTH) {
        this.DATAMONTH = DATAMONTH;
    }

    public String getCARDNO() {
        return CARDNO;
    }

    public void setCARDNO(String CARDNO) {
        this.CARDNO = CARDNO;
    }

    public String getREALNAME() {
        return REALNAME;
    }

    public void setREALNAME(String REALNAME) {
        this.REALNAME = REALNAME;
    }

    public String getORGANNAME() {
        return ORGANNAME;
    }

    public void setORGANNAME(String ORGANNAME) {
        this.ORGANNAME = ORGANNAME;
    }

    public int getDEDUCTALL() {
        return DEDUCTALL;
    }

    public void setDEDUCTALL(int DEDUCTALL) {
        this.DEDUCTALL = DEDUCTALL;
    }

    public int getSHOUDGIVE() {
        return SHOUDGIVE;
    }

    public void setSHOUDGIVE(int SHOUDGIVE) {
        this.SHOUDGIVE = SHOUDGIVE;
    }

    public int getUSERORGAN() {
        return USERORGAN;
    }

    public void setUSERORGAN(int USERORGAN) {
        this.USERORGAN = USERORGAN;
    }

    public int getEMPLOYEEID() {
        return EMPLOYEEID;
    }

    public void setEMPLOYEEID(int EMPLOYEEID) {
        this.EMPLOYEEID = EMPLOYEEID;
    }

    public String getBANKNO() {
        return BANKNO;
    }

    public void setBANKNO(String BANKNO) {
        this.BANKNO = BANKNO;
    }

    public String getPOSTTYPE() {
        return POSTTYPE;
    }

    public void setPOSTTYPE(String POSTTYPE) {
        this.POSTTYPE = POSTTYPE;
    }

    public String getPOSTNAME() {
        return POSTNAME;
    }

    public void setPOSTNAME(String POSTNAME) {
        this.POSTNAME = POSTNAME;
    }

    public int getZMJBGZ() {
        return ZMJBGZ;
    }

    public void setZMJBGZ(int ZMJBGZ) {
        this.ZMJBGZ = ZMJBGZ;
    }

    public int getFDJRJBGZ() {
        return FDJRJBGZ;
    }

    public void setFDJRJBGZ(int FDJRJBGZ) {
        this.FDJRJBGZ = FDJRJBGZ;
    }

    public int getJBGZ() {
        return JBGZ;
    }

    public void setJBGZ(int JBGZ) {
        this.JBGZ = JBGZ;
    }

    public int getSHGZ() {
        return SHGZ;
    }

    public void setSHGZ(int SHGZ) {
        this.SHGZ = SHGZ;
    }

    public int getBANK() {
        return BANK;
    }

    public void setBANK(int BANK) {
        this.BANK = BANK;
    }

    public int getINFILEFLAG() {
        return INFILEFLAG;
    }

    public void setINFILEFLAG(int INFILEFLAG) {
        this.INFILEFLAG = INFILEFLAG;
    }

    public int getOTHERWAGE() {
        return OTHERWAGE;
    }

    public void setOTHERWAGE(int OTHERWAGE) {
        this.OTHERWAGE = OTHERWAGE;
    }

    public String getOTHERWAGEMARK() {
        return OTHERWAGEMARK;
    }

    public void setOTHERWAGEMARK(String OTHERWAGEMARK) {
        this.OTHERWAGEMARK = OTHERWAGEMARK;
    }

    public int getTSGWBT() {
        return TSGWBT;
    }

    public void setTSGWBT(int TSGWBT) {
        this.TSGWBT = TSGWBT;
    }

    public int getXJGBT() {
        return XJGBT;
    }

    public void setXJGBT(int XJGBT) {
        this.XJGBT = XJGBT;
    }

    public int getWORKAGE() {
        return WORKAGE;
    }

    public void setWORKAGE(int WORKAGE) {
        this.WORKAGE = WORKAGE;
    }

    public int getSFLZGZ() {
        return SFLZGZ;
    }

    public void setSFLZGZ(int SFLZGZ) {
        this.SFLZGZ = SFLZGZ;
    }

    public String getMONTHPERFORM() {
        return MONTHPERFORM;
    }

    public void setMONTHPERFORM(String MONTHPERFORM) {
        this.MONTHPERFORM = MONTHPERFORM;
    }

    public int getCHUSHU() {
        return CHUSHU;
    }

    public void setCHUSHU(int CHUSHU) {
        this.CHUSHU = CHUSHU;
    }

    public int getCHENGSHU() {
        return CHENGSHU;
    }

    public void setCHENGSHU(int CHENGSHU) {
        this.CHENGSHU = CHENGSHU;
    }

    public int getSPECIALFLAG() {
        return SPECIALFLAG;
    }

    public void setSPECIALFLAG(int SPECIALFLAG) {
        this.SPECIALFLAG = SPECIALFLAG;
    }
}
