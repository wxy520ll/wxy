package cn.net.xinyi.xmjt.model;

import java.util.List;

/**
 * Created by jiajun.wang on 2017/10/11.
 */

public class TaskModel {
    /**
     * command : taskData
     * taskId : 1
     * name : 外出就医任务
     * escortType : 外出就医
     * destination : 上海市六医院
     * state : 1
     * devices : [1,2,3]
     * unitId : 1
     */

    private String command;
    private String taskId;
    private String name;
    private String escortType;
    private String destination;
    private String state;
    private String unitId;
    private List<Integer> devices;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
    /**
     * {
     “command”:”taskData”,
     “taskId” : “1”,
     “name” : “外出就医任务”,
     “escortType” : “外出就医”,
     “destination” : “上海市六医院”,
     “state” : “1”,
     “devices” : [1, 2, 3],
     “unitId” : “1”,
     }
     */


}
