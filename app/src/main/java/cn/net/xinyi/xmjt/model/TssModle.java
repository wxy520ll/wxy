package cn.net.xinyi.xmjt.model;

import java.util.List;

/**
 * Created by zhouhao on 2016/12/29.
 */

public class TssModle {
    private  int  ID;//ID
    private String FJBH;//房间编号
    private String MC;//名称
    private String FL;//分类（1、网上预约提审室 2、现场排队提审室）
    private String ZT;//'0'		状态（0空闲 1使用中）
    private String SBBH;//红外线设备编号
    private String MJ;//面积
    private String MS;//描述
    private String ALTERNATE_FIELD01;//预留字段1
    private String ALTERNATE_FIELD02;//预留字段2
    private String ALTERNATE_FIELD03;//预留字段3

    private  String DATALIST;//数据集合
    private List<TssyyModle> lists;


    public String getDATALIST() {
        return DATALIST;
    }

    public void setDATALIST(String DATALIST) {
        this.DATALIST = DATALIST;
    }

    public List<TssyyModle> getLists() {
        return lists;
    }

    public void setLists(List<TssyyModle> lists) {
        this.lists = lists;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFJBH() {
        return FJBH;
    }

    public void setFJBH(String FJBH) {
        this.FJBH = FJBH;
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getFL() {
        return FL;
    }

    public void setFL(String FL) {
        this.FL = FL;
    }

    public String getZT() {
        return ZT;
    }

    public void setZT(String ZT) {
        this.ZT = ZT;
    }

    public String getSBBH() {
        return SBBH;
    }

    public void setSBBH(String SBBH) {
        this.SBBH = SBBH;
    }

    public String getMJ() {
        return MJ;
    }

    public void setMJ(String MJ) {
        this.MJ = MJ;
    }

    public String getMS() {
        return MS;
    }

    public void setMS(String MS) {
        this.MS = MS;
    }

    public String getALTERNATE_FIELD01() {
        return ALTERNATE_FIELD01;
    }

    public void setALTERNATE_FIELD01(String ALTERNATE_FIELD01) {
        this.ALTERNATE_FIELD01 = ALTERNATE_FIELD01;
    }

    public String getALTERNATE_FIELD02() {
        return ALTERNATE_FIELD02;
    }

    public void setALTERNATE_FIELD02(String ALTERNATE_FIELD02) {
        this.ALTERNATE_FIELD02 = ALTERNATE_FIELD02;
    }

    public String getALTERNATE_FIELD03() {
        return ALTERNATE_FIELD03;
    }

    public void setALTERNATE_FIELD03(String ALTERNATE_FIELD03) {
        this.ALTERNATE_FIELD03 = ALTERNATE_FIELD03;
    }
}
