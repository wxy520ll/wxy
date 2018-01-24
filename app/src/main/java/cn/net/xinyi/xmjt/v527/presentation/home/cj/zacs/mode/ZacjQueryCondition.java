package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs.mode;

/**
 * Created by Fracesuit on 2018/1/19.
 */

public class ZacjQueryCondition {
    /**
     * MC : 深圳市龙岗区平湖远程汽配店
     * JL : 5000
     * JD : 114.140537
     * WD : 22.688796
     * PAGESIZE : 10
     * PAGENUMBER : 1
     */

    private String MC;
    private String JL="50";
    private String JD;
    private String WD;
    private int PAGESIZE;
    private int PAGENUMBER;

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getJL() {
        return JL;
    }

    public void setJL(String JL) {
        this.JL = JL;
    }

    public String getJD() {
        return JD;
    }

    public void setJD(String JD) {
        this.JD = JD;
    }

    public String getWD() {
        return WD;
    }

    public void setWD(String WD) {
        this.WD = WD;
    }

    public int getPAGESIZE() {
        return PAGESIZE;
    }

    public void setPAGESIZE(int PAGESIZE) {
        this.PAGESIZE = PAGESIZE;
    }

    public int getPAGENUMBER() {
        return PAGENUMBER;
    }

    public void setPAGENUMBER(int PAGENUMBER) {
        this.PAGENUMBER = PAGENUMBER;
    }
}
