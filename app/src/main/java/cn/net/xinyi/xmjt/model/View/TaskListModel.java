package cn.net.xinyi.xmjt.model.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajun.wang on 2017/10/11.
 */

public class TaskListModel implements Serializable{
    /**
     * error : 0
     * errorMsg : 操作成功
     * data : [{"id":"1","name":"外出就医任务","escortType":"外出就医","destination":"上海市六医院","state":"state","devices":[1,2,3],"unitId":"1"}]
     */

    private String error;
    private String errorMsg;
    private List<DataBean> data=new ArrayList<>();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * name : 外出就医任务
         * escortType : 外出就医
         * destination : 上海市六医院
         * state : state
         * devices : [1,2,3]
         * unitId : 1
         */

        private String id;
        private String name;
        private String escortType;
        private String destination;
        private String state;
        private String unitId;
        private List<Integer> devices;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEscortType() {
            return escortType;
        }

        public void setEscortType(String escortType) {
            this.escortType = escortType;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public List<Integer> getDevices() {
            return devices;
        }

        public void setDevices(List<Integer> devices) {
            this.devices = devices;
        }
    }
    /**
     * {
     “error”: ”0”,
     “errorMsg”: ”操作成功”,
     “data”: [
     {
     “id”: “1”,
     “name”: “外出就医任务”,
     “escortType”: “外出就医”,
     “destination”: “上海市六医院”,
     “state”: “1”,
     “devices”: [
     1,
     2,
     3
     ],
     “unitId”: “1”,
     }
     ]
     }
     */





}
