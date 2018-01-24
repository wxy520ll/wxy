package cn.net.xinyi.xmjt.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by studyjun on 2016/5/1.
 */
public class YinyanEntity implements Serializable{

    /**
     * entity_name : 13066966107
     * create_time : 2016-04-27 06:12:49
     * modify_time : 2016-05-01 10:14:26
     * realtime_point : {"loc_time":1462068864,"location":[114.12910695788,22.609901029289],"radius":40,"speed":0,"direction":0}
     */

    private String entity_name;
    private String create_time;
    private String modify_time;
    /**
     * loc_time : 1462068864
     * location : [114.12910695788,22.609901029289]
     * radius : 40
     * speed : 0
     * direction : 0
     *
     */

    private String name;
    private String phone;

    private RealtimePointBean realtime_point;

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public RealtimePointBean getRealtime_point() {
        return realtime_point;
    }

    public void setRealtime_point(RealtimePointBean realtime_point) {
        this.realtime_point = realtime_point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static class RealtimePointBean implements Serializable{
        private int loc_time;
        private int radius;
        private int speed;
        private int direction;
        private String phone;
        private String name;
        private List<Double> location;

        public int getLoc_time() {
            return loc_time;
        }

        public void setLoc_time(int loc_time) {
            this.loc_time = loc_time;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}