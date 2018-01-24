package cn.net.xinyi.xmjt.v527.presentation.task.csxc.model;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2017/12/30.
 */

public class CsxcModel implements Serializable {

    private String csmc;//场所名称
    private String pfr;//派发人
    private String pfrDw;//派发人单位
    private String taskName;//任务名称
    private String lx;//类型
    private String fqsj;//发起时间
    private String xqsj;//限期时间
    private String taskJj;//任务简介


    private String id;//任务id
    private String csId;//
    private String cscode;//代码
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCsmc() {
        return csmc;
    }

    public void setCsmc(String csmc) {
        this.csmc = csmc;
    }

    public String getPfr() {
        return pfr;
    }

    public void setPfr(String pfr) {
        this.pfr = pfr;
    }

    public String getPfrDw() {
        return pfrDw;
    }

    public void setPfrDw(String pfrDw) {
        this.pfrDw = pfrDw;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getFqsj() {
        return fqsj;
    }

    public void setFqsj(String fqsj) {
        this.fqsj = fqsj;
    }

    public String getXqsj() {
        return xqsj;
    }

    public void setXqsj(String xqsj) {
        this.xqsj = xqsj;
    }

    public String getTaskJj() {
        return taskJj;
    }

    public void setTaskJj(String taskJj) {
        this.taskJj = taskJj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public String getCscode() {
        return cscode;
    }

    public void setCscode(String cscode) {
        this.cscode = cscode;
    }
}
