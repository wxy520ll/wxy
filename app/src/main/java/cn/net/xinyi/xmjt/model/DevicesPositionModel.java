package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by jiajun.wang on 2017/10/11.
 */

public class DevicesPositionModel implements Serializable{

    /**
     * command : deviceData
     * deviceId : 1
     * latitude : 31.171776
     * longitude : 121.394128
     * time : 2017-10-11 12:10:10
     * escape : 1
     * lowpower : 0
     * tapebreak : 0
     * singledrop : 0
     */

    private String command;
    private String deviceId;
    private String latitude;
    private String longitude;
    private String time;
    private String escape;
    private String lowpower;
    private String tapebreak;
    private String singledrop;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEscape() {
        return escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }

    public String getLowpower() {
        return lowpower;
    }

    public void setLowpower(String lowpower) {
        this.lowpower = lowpower;
    }

    public String getTapebreak() {
        return tapebreak;
    }

    public void setTapebreak(String tapebreak) {
        this.tapebreak = tapebreak;
    }

    public String getSingledrop() {
        return singledrop;
    }

    public void setSingledrop(String singledrop) {
        this.singledrop = singledrop;
    }
}
