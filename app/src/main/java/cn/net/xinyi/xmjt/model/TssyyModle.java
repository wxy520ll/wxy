package cn.net.xinyi.xmjt.model;

/**
 * Created by zhouhao on 2016/12/29.
 */

public class TssyyModle {

    private int ID;//ID
    private String FJBH;//房间编号
    private String YYH;//预约号
    private String YYRQ;//预约日期
    private String YYLX;//预约类型（0:9-12 、1：14-16 2、16-18）
    private String YYRJH;//预约人警号
    private String YYRSJH;//预约人手机号
    private String ZT;//（1预约中 2.取消预约 3使用中 4已用完 5.失信）
    private String KSSJ;//开始使用时间
    private String JSSJ;//结束使用时间
    private String YYLRSJ;//预约录入时间
    private String MC;//名称
    private String MJ;//面积
    private String SBBH;//预约录入时间
    private String MS;//预约录入时间
    private String YYSD;//预约录入时间
    private String ALTERNATE_FIELD01;//预留字段1
    private String ALTERNATE_FIELD02;//预留字段2
    private String ALTERNATE_FIELD03;//预留字段3

    private String PDBH;//排队编号
    private String PDLRSJ;//排队编号

    private String YYSJD;//预约时间段  同YYLX  为了区分和提审记录的类型区分；

    public String getYYSD() {
        return YYSD;
    }

    public void setYYSD(String YYSD) {
        this.YYSD = YYSD;
    }

    public String getYYSJD() {
        return YYSJD;
    }

    public void setYYSJD(String YYSJD) {
        this.YYSJD = YYSJD;
    }

    public String getPDLRSJ() {
        return PDLRSJ;
    }

    public void setPDLRSJ(String PDLRSJ) {
        this.PDLRSJ = PDLRSJ;
    }

    public String getPDBH() {
        return PDBH;
    }

    public void setPDBH(String PDBH) {
        this.PDBH = PDBH;
    }

    public String getMJ() {
        return MJ;
    }

    public void setMJ(String MJ) {
        this.MJ = MJ;
    }

    public String getSBBH() {
        return SBBH;
    }

    public void setSBBH(String SBBH) {
        this.SBBH = SBBH;
    }

    public String getMS() {
        return MS;
    }

    public void setMS(String MS) {
        this.MS = MS;
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
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

    public String getYYH() {
        return YYH;
    }

    public void setYYH(String YYH) {
        this.YYH = YYH;
    }

    public String getYYRQ() {
        return YYRQ;
    }

    public void setYYRQ(String YYRQ) {
        this.YYRQ = YYRQ;
    }

    public String getYYLX() {
        return YYLX;
    }

    public void setYYLX(String YYLX) {
        this.YYLX = YYLX;
    }

    public String getYYRJH() {
        return YYRJH;
    }

    public void setYYRJH(String YYRJH) {
        this.YYRJH = YYRJH;
    }

    public String getYYRSJH() {
        return YYRSJH;
    }

    public void setYYRSJH(String YYRSJH) {
        this.YYRSJH = YYRSJH;
    }

    public String getZT() {
        return ZT;
    }

    public void setZT(String ZT) {
        this.ZT = ZT;
    }

    public String getKSSJ() {
        return KSSJ;
    }

    public void setKSSJ(String KSSJ) {
        this.KSSJ = KSSJ;
    }

    public String getJSSJ() {
        return JSSJ;
    }

    public void setJSSJ(String JSSJ) {
        this.JSSJ = JSSJ;
    }

    public String getYYLRSJ() {
        return YYLRSJ;
    }

    public void setYYLRSJ(String YYLRSJ) {
        this.YYLRSJ = YYLRSJ;
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
