package cn.net.xinyi.xmjt.model;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * Created by studyjun on 2016/3/31.
 */
public class LocaionInfo implements Serializable{
//           MyLocationData locationData = new MyLocationData.Builder().accuracy(location.getRadius()).latitude(location.getLatitude()).longitude(location.getLongitude()).direction(location.getDirection()).build();

    public double lat;
    public double longt;
    public String address;
    public float radius;
    public float direction;
    public int loctype;

    public LocaionInfo() {
    }

    public LocaionInfo(double lat, double longt, String address) {
        this.lat = lat;
        this.longt = longt;
        this.address = address;
    }
    public LocaionInfo(double lat, double longt, String address,int loctype) {
        this.lat = lat;
        this.longt = longt;
        this.address = address;
        this.loctype = loctype;
    }

    public int getLoctype() {
        return loctype;
    }

    public void setLoctype(int loctype) {
        this.loctype = loctype;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public LocaionInfo(double lat, double longt, String address, float radius, float direction) {
        this.lat = lat;
        this.longt = longt;
        this.address = address;
        this.radius = radius;
        this.direction = direction;
    }

    public LatLng getPoint(){
        return new LatLng(lat,longt);
    }
}