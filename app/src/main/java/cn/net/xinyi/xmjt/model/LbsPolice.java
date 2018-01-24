package cn.net.xinyi.xmjt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by studyjun on 2016/4/26.
 */
public class LbsPolice implements Serializable{

    /**
     * last_time : 1461660133542
     * tags : 巡逻
     * uid : 1677249267
     * province : 广东省
     * geotable_id : 139079
     * modify_time : 1461661017
     * district : 龙岗区
     * user_status : 1
     * create_time : 1461660268
     * city : 深圳市
     * user : 13268120165
     * location : [114.07323721744194,22.622289144303508]
     * address : 深圳市南山
     * title : 张三
     * coord_type : 3
     * direction : 西南
     * type : 0
     * distance : 917
     * weight : 0
     */

    private long last_time;
    private String tags;
    private int uid;
    private String province;
    private int geotable_id;
    private int modify_time;
    private String district;
    private String user_status;
    private int create_time;
    private String city;
    private String user;
    private String address;
    private String title;
    private int coord_type;
    private String direction;
    private int type;
    private int distance;
    private int weight;
    private String user_phone;
    private List<Double> location;

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getGeotable_id() {
        return geotable_id;
    }

    public void setGeotable_id(int geotable_id) {
        this.geotable_id = geotable_id;
    }

    public int getModify_time() {
        return modify_time;
    }

    public void setModify_time(int modify_time) {
        this.modify_time = modify_time;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(int coord_type) {
        this.coord_type = coord_type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}
