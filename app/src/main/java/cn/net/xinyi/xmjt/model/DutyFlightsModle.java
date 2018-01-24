package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/7/12.
 */
public class DutyFlightsModle implements Serializable {
    private String FLIGHT_NAME ;
    private String START_TIME ;
    private String END_TIME ;
    private String LENGTH ;
    private int CREATOR_ID ;
    private String id ;
    private String CREATOR_NAME ;
    private String FR_FRID ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public void setLENGTH(String LENGTH) {
        this.LENGTH = LENGTH;
    }

    public void setCREATOR_ID(int CREATOR_ID) {
        this.CREATOR_ID = CREATOR_ID;
    }

    public void setCREATOR_NAME(String CREATOR_NAME) {
        this.CREATOR_NAME = CREATOR_NAME;
    }

    public void setFR_FRID(String FR_FRID) {
        this.FR_FRID = FR_FRID;
    }

    public void setFLIGHT_NAME(String FLIGHT_NAME) {
        this.FLIGHT_NAME = FLIGHT_NAME;
    }

    public String getFLIGHT_NAME() {
        return FLIGHT_NAME;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public String getLENGTH() {
        return LENGTH;
    }

    public int getCREATOR_ID() {
        return CREATOR_ID;
    }

    public String getCREATOR_NAME() {
        return CREATOR_NAME;
    }

    public String getFR_FRID() {
        return FR_FRID;
    }
}