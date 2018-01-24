package cn.net.xinyi.xmjt.model;

import java.util.List;

/**
 * Created by hao.zhou on 2016/11/7.
 */
public class MachineModle {

    private  int ID ; // 编号

    private  String  PCSBM; // 派出所编码
    private  String   CJYH ;  //采集用户
    private  String   MC; // 机房名称
    private  String   SCSJ;//上传时间采集用户
    private  String   GXSJ;//  更新时间
    private  String USERNAME;//管理员姓名
    private  String DATALIST;//数据集合
    private  String SGWCSJ;//施工完成时间
    private  String NAME;//数据集合
    private  int ROOMID;//机房ID
    private  int   STATUS;//是否有效,1有效0无效

    private  List<MachineModle> lists;

    public List<MachineModle> getLists() {
        return lists;
    }

    public void setLists(List<MachineModle> lists) {
        this.lists = lists;
    }

    public String getSGWCSJ() {
        return SGWCSJ;
    }

    public void setSGWCSJ(String SGWCSJ) {
        this.SGWCSJ = SGWCSJ;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPCSBM() {
        return PCSBM;
    }

    public void setPCSBM(String PCSBM) {
        this.PCSBM = PCSBM;
    }

    public String getCJYH() {
        return CJYH;
    }

    public void setCJYH(String CJYH) {
        this.CJYH = CJYH;
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public String getGXSJ() {
        return GXSJ;
    }

    public void setGXSJ(String GXSJ) {
        this.GXSJ = GXSJ;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public int getROOMID() {
        return ROOMID;
    }

    public void setROOMID(int ROOMID) {
        this.ROOMID = ROOMID;
    }

    public String getDATALIST() {
        return DATALIST;
    }

    public void setDATALIST(String DATALIST) {
        this.DATALIST = DATALIST;
    }
}
