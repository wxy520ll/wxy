package cn.net.xinyi.xmjt.model;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cn.net.xinyi.xmjt.utils.DateUtil;

/**
 * Created by AppleRen on 2017/3/16.
 */

public class HisTraceQueryModel {
    /**
     * status : 0
     * message : 成功
     * total : 1
     * size : 1
     * entity_name : 18566266518
     * distance : 0
     * toll_distance : 0
     * start_point : {"longitude":113.94942800975,"latitude":22.541890374035,"coord_type":3,"loc_time":1489738127}
     * end_point : {"longitude":113.94942800975,"latitude":22.541890374035,"coord_type":3,"loc_time":1489738127}
     * points : [{"loc_time":1489738127,"location":[113.94942800975,22.541890374035],"create_time":"2017-03-17 16:08:48","floor":"","radius":163,"speed":0,"direction":0}]
     */

    private int status;
    private String message;
    private int total;
    private int size;
    private String entity_name;
    private String distance;
    private String toll_distance;
    private StartPointBean start_point;
    private EndPointBean end_point;
    private List<PointsBean> points;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getToll_distance() {
        return toll_distance;
    }

    public void setToll_distance(String toll_distance) {
        this.toll_distance = toll_distance;
    }

    public StartPointBean getStart_point() {
        return start_point;
    }

    public void setStart_point(StartPointBean start_point) {
        this.start_point = start_point;
    }

    public EndPointBean getEnd_point() {
        return end_point;
    }

    public void setEnd_point(EndPointBean end_point) {
        this.end_point = end_point;
    }

    public List<PointsBean> getPoints() {
        return points;
    }

    public void setPoints(List<PointsBean> points) {
        this.points = points;
    }

    public static class StartPointBean {
        /**
         * longitude : 113.94942800975
         * latitude : 22.541890374035
         * coord_type : 3
         * loc_time : 1489738127
         */

        private double longitude;
        private double latitude;
        private int coord_type;
        private String loc_time;

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

        public int getCoord_type() {
            return coord_type;
        }

        public void setCoord_type(int coord_type) {
            this.coord_type = coord_type;
        }

        public String getLoc_time() {
            return loc_time;
        }

        public void setLoc_time(String loc_time) {
            this.loc_time = loc_time;
        }
    }

    public static class EndPointBean {
        /**
         * longitude : 113.94942800975
         * latitude : 22.541890374035
         * coord_type : 3
         * loc_time : 1489738127
         */

        private double longitude;
        private double latitude;
        private int coord_type;
        private String loc_time;

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

        public int getCoord_type() {
            return coord_type;
        }

        public void setCoord_type(int coord_type) {
            this.coord_type = coord_type;
        }

        public String getLoc_time() {
            return loc_time;
        }

        public void setLoc_time(String loc_time) {
            this.loc_time = loc_time;
        }
    }

    public static class PointsBean {
        /**
         * loc_time : 1489738127
         * location : [113.94942800975,22.541890374035]
         * create_time : 2017-03-17 16:08:48
         * floor :
         * radius : 163
         * speed : 0
         * direction : 0
         */

        private Long loc_time;
        private String create_time;
        private String floor;
        private String radius;
        private String speed;
        private String direction;
        private List<Double> location;

        public Long getLoc_time() {
            return loc_time;
        }

        public void setLoc_time(Long loc_time) {
            this.loc_time = loc_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }

        @Override
        public String toString() {

            return "PointsBean{" +
                    "location=" + location +
                    ", loc_time='" +  DateUtil.date2String(new Date(loc_time), DateUtil.DATE_FULL_STR) + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", floor='" + floor + '\'' +
                    ", radius='" + radius + '\'' +
                    ", speed='" + speed + '\'' +
                    ", direction='" + direction + '\'' +
                    '}';
        }
    }

    public LatLng getStartLatLng() {

        return new LatLng(start_point.getLatitude(), start_point.getLongitude());
    }

    public LatLng getEndLatLng() {

        return new LatLng(end_point.getLatitude(), end_point.getLongitude());
    }

    public List<PointsBean> getListPoints() {
        List<PointsBean> list = new ArrayList<PointsBean>();
        if (points == null || points.size() == 0) {
            return null;

        }
        Iterator<PointsBean> it = points.iterator();

        while (it.hasNext()) {
            PointsBean pois = it.next();

            List<Double> location = pois.getLocation();
            if (Math.abs(location.get(0) - 0.0) < 0.01 && Math.abs(location.get(1) - 0.0) < 0.01) {
                continue;
            } else {
                list.add(pois);
            }

        }
        return list;

    }


    public List<LatLng> getListPoints2() {
        List<LatLng> list = new ArrayList<LatLng>();

        if (points == null || points.size() == 0) {
            return null;

        }
        Iterator<PointsBean> it = points.iterator();

        while (it.hasNext()) {
            PointsBean pois = (PointsBean) it.next();

            List<Double> location = pois.getLocation();
            if (Math.abs(location.get(0) - 0.0) < 0.01 && Math.abs(location.get(1) - 0.0) < 0.01) {
                continue;
            } else {
                LatLng latLng = new LatLng(location.get(1), location.get(0));
                list.add(latLng);
            }

        }
        return list;

    }
}
