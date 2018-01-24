package cn.net.xinyi.xmjt.model;

import java.util.List;

/**
 * Created by studyjun on 2016/4/22.
 */
public class UserPlcBxInfo {
    /**
     * DATALIST : [{'SCSJ':'2016-04-22 10:46:08','ID':8,'GTZSID':9','LX':'1','LAT':'112.000','LNGT':'118:000','LOCTYPE':'116','MS':'描述'},{'SCSJ':'2016-04-22 10:47:14','ID':9,'GTZSID':9','LX':'3','LAT':'112.000','LNGT':'118:000','LOCTYPE':'116','MS':'描述'},{'SCSJ':'2016-04-22 10:47:35','ID':10,'GTZSID':9','LX':'4','LAT':'112.000','LNGT':'118:000','LOCTYPE':'116','MS':'描述'},{'SCSJ':'2016-04-22 10:47:39','ID':11,'GTZSID':9','LX':'2','LAT':'112.000','LNGT':'118:000','LOCTYPE':'116','MS':'描述'}]
     * DW : 布吉所
     * GTID : 60
     * ID : 9
     * SCSJ : 2016-04-22 10:46:08.000
     * YH : 13603056628
     * ZSZT : 2
     */

    private String DATALIST;
    private String DW;
    private int GTID;
    private int ID;
    private String SCSJ;
    private String YH;
    private int ZSZT;
    private int TOTALTIME;

    private List<PlcBxWorkLog> lists;

    public int getTOTALTIME() {
        return TOTALTIME;
    }

    public void setTOTALTIME(int TOTALTIME) {
        this.TOTALTIME = TOTALTIME;
    }
    public void setDATALIST(String DATALIST) {
        this.DATALIST = DATALIST;
    }

    public void setDW(String DW) {
        this.DW = DW;
    }

    public void setGTID(int GTID) {
        this.GTID = GTID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public void setYH(String YH) {
        this.YH = YH;
    }

    public void setZSZT(int ZSZT) {
        this.ZSZT = ZSZT;
    }

    public String getDATALIST() {
        return DATALIST;
    }

    public String getDW() {
        return DW;
    }

    public int getGTID() {
        return GTID;
    }

    public int getID() {
        return ID;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public String getYH() {
        return YH;
    }

    public int getZSZT() {
        return ZSZT;
    }

    public List<PlcBxWorkLog> getLists() {
        return lists;
    }

    public void setLists(List<PlcBxWorkLog> lists) {
        this.lists = lists;
    }
}
