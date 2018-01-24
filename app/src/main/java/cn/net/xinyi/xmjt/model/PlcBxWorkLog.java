package cn.net.xinyi.xmjt.model;

/**
 * Created by studyjun on 2016/4/23.
 */
public class PlcBxWorkLog {


    /**
     * SCSJ : 2016-04-22 10:46:08
     * ID : 8
     * GTZSID : 9
     * LX : 1
     * LAT : 112.000
     * LNGT : 118: 000
     * LOCTYPE : 116
     * MS : 描述
     */

    private String SCSJ;
    private int ID;
    private int GTZSID;
    private String LX;
    private double LAT;
    private double LNGT;
    private String LOCTYPE;
    private String MS;
    private int TOTALTIME;

    public int getTOTALTIME() {
        return TOTALTIME;
    }

    public void setTOTALTIME(int TOTALTIME) {
        this.TOTALTIME = TOTALTIME;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getGTZSID() {
        return GTZSID;
    }

    public void setGTZSID(int GTZSID) {
        this.GTZSID = GTZSID;
    }

    public String getLX() {
        return LX;
    }

    public void setLX(String LX) {
        this.LX = LX;
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

    public String getLOCTYPE() {
        return LOCTYPE;
    }

    public void setLOCTYPE(String LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }

    public String getMS() {
        return MS;
    }

    public void setMS(String MS) {
        this.MS = MS;
    }
}
