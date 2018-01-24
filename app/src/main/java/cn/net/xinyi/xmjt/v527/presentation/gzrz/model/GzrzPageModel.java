package cn.net.xinyi.xmjt.v527.presentation.gzrz.model;

import java.io.Serializable;

/**
 * Created by jiajun.wang on 2017/12/30.
 */

public class GzrzPageModel implements Serializable {

    /**
     * RECORD_TYPE : 07
     * RECORD_BODY : 早上点到，步巡
     下午（1）走访辖区内的工地以及嘱咐工地负责人，不能出现拖欠农民工工资的现象。
     （2）坂田街道联合宝岗派出所在万科城广场举行文艺汇演，街道领导和辖区副所长庄所，社区队杨队长，辖区民警李永刚和万科城物业经理出席活动彩排现场，就现场安保防控进行交谈
     * PCS_CODE : 440307590000
     * USER_CODE : 055080
     * RECORD_TITLE : 周三
     * IP : 10.235.136.220
     * LNG : 114.06451199
     * FILE_ID : ,201712272322275485,20171227232236846,20171227232303364
     * DEPT_CODE : 440307590300
     * UPDATED_TIME : 2017-12-28 00:01:15.000
     * APPROVAL_MSG : 己阅
     * APPROVAL_TIME : 2017-12-28 00:01:15.000
     * LAT : 22.64793294
     * USER_NAME : 李永刚
     * ROWNUM_ : 10
     * PHONE_MAC_ID : 865970038065800
     * APPROVAL_STATUS : 1
     * ID : 2406590
     * REPORTED : 1
     * FJ_CODE : 440307000000
     * RECORD_DATE : 2017-12-27 00:00:00.000
     * APPROVER : 062817
     * DELETED : 0
     * CREATED_TIME : 2017-12-27 23:25:48.000
     */

    private String RECORD_TYPE;
    private String RECORD_BODY;
    private String PCS_CODE;
    private String USER_CODE;
    private String RECORD_TITLE;
    private String IP;
    private double LNG;

    private String SBRY;
    private String DEPT_CODE;
    private String UPDATED_TIME;
    private String APPROVAL_MSG;
    private String APPROVAL_TIME;
    private double LAT;
    private String USER_NAME;
    private int ROWNUM_;
    private String PHONE_MAC_ID;
    private int APPROVAL_STATUS;
    private int ID;
    private int REPORTED;
    private String FJ_CODE;
    private String RECORD_DATE;
    private String APPROVER;
    private int DELETED;
    private String CREATED_TIME;
    private String ATT_ID;//附件id
    private String FILE_ID;//文件id
    private String FILE_ID2;//文件id
    private String FILE_ID3;//文件id

    private String RECORD_TYPE_NAME;

    public String getRECORD_TYPE_NAME() {
        return RECORD_TYPE_NAME;
    }

    public void setRECORD_TYPE_NAME(String RECORD_TYPE_NAME) {
        this.RECORD_TYPE_NAME = RECORD_TYPE_NAME;
    }

    public String getSBRY() {
        return SBRY;
    }

    public void setSBRY(String SBRY) {
        this.SBRY = SBRY;
    }

    public String getFILE_ID2() {
        return FILE_ID2;
    }

    public void setFILE_ID2(String FILE_ID2) {
        this.FILE_ID2 = FILE_ID2;
    }

    public String getFILE_ID3() {
        return FILE_ID3;
    }

    public void setFILE_ID3(String FILE_ID3) {
        this.FILE_ID3 = FILE_ID3;
    }

    public String getATT_ID() {
        return ATT_ID;
    }

    public void setATT_ID(String ATT_ID) {
        this.ATT_ID = ATT_ID;
    }

    public String getRECORD_TYPE() {
        return RECORD_TYPE;
    }

    public void setRECORD_TYPE(String RECORD_TYPE) {
        this.RECORD_TYPE = RECORD_TYPE;
    }

    public String getRECORD_BODY() {
        return RECORD_BODY;
    }

    public void setRECORD_BODY(String RECORD_BODY) {
        this.RECORD_BODY = RECORD_BODY;
    }

    public String getPCS_CODE() {
        return PCS_CODE;
    }

    public void setPCS_CODE(String PCS_CODE) {
        this.PCS_CODE = PCS_CODE;
    }

    public String getUSER_CODE() {
        return USER_CODE;
    }

    public void setUSER_CODE(String USER_CODE) {
        this.USER_CODE = USER_CODE;
    }

    public String getRECORD_TITLE() {
        return RECORD_TITLE;
    }

    public void setRECORD_TITLE(String RECORD_TITLE) {
        this.RECORD_TITLE = RECORD_TITLE;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public double getLNG() {
        return LNG;
    }

    public void setLNG(double LNG) {
        this.LNG = LNG;
    }

    public String getFILE_ID() {
        return FILE_ID;
    }

    public void setFILE_ID(String FILE_ID) {
        this.FILE_ID = FILE_ID;
    }

    public String getDEPT_CODE() {
        return DEPT_CODE;
    }

    public void setDEPT_CODE(String DEPT_CODE) {
        this.DEPT_CODE = DEPT_CODE;
    }

    public String getUPDATED_TIME() {
        return UPDATED_TIME;
    }

    public void setUPDATED_TIME(String UPDATED_TIME) {
        this.UPDATED_TIME = UPDATED_TIME;
    }

    public String getAPPROVAL_MSG() {
        return APPROVAL_MSG;
    }

    public void setAPPROVAL_MSG(String APPROVAL_MSG) {
        this.APPROVAL_MSG = APPROVAL_MSG;
    }

    public String getAPPROVAL_TIME() {
        return APPROVAL_TIME;
    }

    public void setAPPROVAL_TIME(String APPROVAL_TIME) {
        this.APPROVAL_TIME = APPROVAL_TIME;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public int getROWNUM_() {
        return ROWNUM_;
    }

    public void setROWNUM_(int ROWNUM_) {
        this.ROWNUM_ = ROWNUM_;
    }

    public String getPHONE_MAC_ID() {
        return PHONE_MAC_ID;
    }

    public void setPHONE_MAC_ID(String PHONE_MAC_ID) {
        this.PHONE_MAC_ID = PHONE_MAC_ID;
    }

    public int getAPPROVAL_STATUS() {
        return APPROVAL_STATUS;
    }

    public void setAPPROVAL_STATUS(int APPROVAL_STATUS) {
        this.APPROVAL_STATUS = APPROVAL_STATUS;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getREPORTED() {
        return REPORTED;
    }

    public void setREPORTED(int REPORTED) {
        this.REPORTED = REPORTED;
    }

    public String getFJ_CODE() {
        return FJ_CODE;
    }

    public void setFJ_CODE(String FJ_CODE) {
        this.FJ_CODE = FJ_CODE;
    }

    public String getRECORD_DATE() {
        return RECORD_DATE;
    }

    public void setRECORD_DATE(String RECORD_DATE) {
        this.RECORD_DATE = RECORD_DATE;
    }

    public String getAPPROVER() {
        return APPROVER;
    }

    public void setAPPROVER(String APPROVER) {
        this.APPROVER = APPROVER;
    }

    public int getDELETED() {
        return DELETED;
    }

    public void setDELETED(int DELETED) {
        this.DELETED = DELETED;
    }

    public String getCREATED_TIME() {
        return CREATED_TIME;
    }

    public void setCREATED_TIME(String CREATED_TIME) {
        this.CREATED_TIME = CREATED_TIME;
    }
}
