package cn.net.xinyi.xmjt.model;

import java.util.List;

/**
 * Created by studyjun on 2016/4/27.
 * 百度地理围栏
 */
public class BaiduFence {


    /**
     * fence_id : 1
     * name : 33
     * description : 18566266518轨迹
     * valid_times : [{"begin_time":"0800","end_time":"2300"}]
     * valid_cycle : 4
     * valid_days : []
     * shape : 1
     * center : {"longitude":113.949964,"latitude":22.5397}
     * radius : 100
     * vertexes : []
     * alarm_condition : 3
     * creator : 18566266518
     * observers : ["18566266518"]
     * monitored_persons : ["18566266518"]
     * create_time : 2016-04-27 12:21:10
     * update_time : 2016-04-27 12:21:10
     * coord_type : 3
     */

    private int fence_id;
    private String name;
    private String description;
    private int valid_cycle;
    private int shape;
    /**
     * longitude : 113.949964
     * latitude : 22.5397
     */

    private CenterBean center;
    private int radius;
    private int alarm_condition;
    private String creator;
    private String create_time;
    private String update_time;
    private int coord_type;
    /**
     * begin_time : 0800
     * end_time : 2300
     */

    private List<ValidTimesBean> valid_times;
    private List<?> valid_days;
    private List<?> vertexes;
    private List<String> observers;
    private List<String> monitored_persons;
    /**
     * valid_date : 20160516
     */

    private String valid_date;

    public int getFence_id() {
        return fence_id;
    }

    public void setFence_id(int fence_id) {
        this.fence_id = fence_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValid_cycle() {
        return valid_cycle;
    }

    public void setValid_cycle(int valid_cycle) {
        this.valid_cycle = valid_cycle;
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public CenterBean getCenter() {
        return center;
    }

    public void setCenter(CenterBean center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getAlarm_condition() {
        return alarm_condition;
    }

    public void setAlarm_condition(int alarm_condition) {
        this.alarm_condition = alarm_condition;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(int coord_type) {
        this.coord_type = coord_type;
    }

    public List<ValidTimesBean> getValid_times() {
        return valid_times;
    }

    public void setValid_times(List<ValidTimesBean> valid_times) {
        this.valid_times = valid_times;
    }

    public List<?> getValid_days() {
        return valid_days;
    }

    public void setValid_days(List<?> valid_days) {
        this.valid_days = valid_days;
    }

    public List<?> getVertexes() {
        return vertexes;
    }

    public void setVertexes(List<?> vertexes) {
        this.vertexes = vertexes;
    }

    public List<String> getObservers() {
        return observers;
    }

    public void setObservers(List<String> observers) {
        this.observers = observers;
    }

    public List<String> getMonitored_persons() {
        return monitored_persons;
    }

    public void setMonitored_persons(List<String> monitored_persons) {
        this.monitored_persons = monitored_persons;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public static class CenterBean {
        private double longitude;
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }

    public static class ValidTimesBean {
        private String begin_time;
        private String end_time;

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }
}
