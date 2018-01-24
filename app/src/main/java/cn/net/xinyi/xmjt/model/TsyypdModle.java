package cn.net.xinyi.xmjt.model;

/**
 * Created by zhouhao on 2016/12/29.
 */

public class TsyypdModle {
    private int ID;//ID
    private String FJBH;//房间编号
    private String PDBH;//排队编号
    private String PDRJH;//排队人警号
    private String ZT;//状态（1排队中 2.取消排队 3 使用中 4.已用完）
    private String KSSJ;//开始使用时间
    private String JSSJ;//结束使用时间
    private String PDLRSJ;//排队录入时间
    private String ALTERNATE_FIELD01;//预留字段1
    private String ALTERNATE_FIELD02;//预留字段2
    private String ALTERNATE_FIELD03;//预留字段3

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

    public String getPDBH() {
        return PDBH;
    }

    public void setPDBH(String PDBH) {
        this.PDBH = PDBH;
    }

    public String getPDRJH() {
        return PDRJH;
    }

    public void setPDRJH(String PDRJH) {
        this.PDRJH = PDRJH;
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

    public String getPDLRSJ() {
        return PDLRSJ;
    }

    public void setPDLRSJ(String PDLRSJ) {
        this.PDLRSJ = PDLRSJ;
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
