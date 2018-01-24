package cn.net.xinyi.xmjt.v527.presentation.gzrz.model;

/**
 * Created by jiajun.wang on 2017/12/30.
 * 工作日志类型
 */

public class GzrzTypeModel {

    /**
     * CODE : 01
     * ID : 2024762
     * NAME : 社区会议
     * ORDER_NUM : 5
     * PARENT_CODE : work_record_type
     */

    private String CODE;
    private int ID;
    private String NAME;
    private int ORDER_NUM;
    private String PARENT_CODE;

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getORDER_NUM() {
        return ORDER_NUM;
    }

    public void setORDER_NUM(int ORDER_NUM) {
        this.ORDER_NUM = ORDER_NUM;
    }

    public String getPARENT_CODE() {
        return PARENT_CODE;
    }

    public void setPARENT_CODE(String PARENT_CODE) {
        this.PARENT_CODE = PARENT_CODE;
    }
}
