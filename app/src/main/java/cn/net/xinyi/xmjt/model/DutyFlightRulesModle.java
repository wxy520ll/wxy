package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/7/12.
 */
public class DutyFlightRulesModle implements Serializable {
    private String ID;
    private int CREATOR_ID;
    private String CREATOR_NAME;
    private String SUBS_ID;
    private String SUBS_NAME;
    private String DEPT_ID;
    private String DEPT_NAME;
    private String RULE_NAME;
    private String RULE_DUTY;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCREATOR_ID(int CREATOR_ID) {
        this.CREATOR_ID = CREATOR_ID;
    }

    public void setCREATOR_NAME(String CREATOR_NAME) {
        this.CREATOR_NAME = CREATOR_NAME;
    }

    public void setSUBS_ID(String SUBS_ID) {
        this.SUBS_ID = SUBS_ID;
    }

    public void setSUBS_NAME(String SUBS_NAME) {
        this.SUBS_NAME = SUBS_NAME;
    }

    public void setDEPT_ID(String DEPT_ID) {
        this.DEPT_ID = DEPT_ID;
    }

    public void setDEPT_NAME(String DEPT_NAME) {
        this.DEPT_NAME = DEPT_NAME;
    }

    public void setRULE_NAME(String RULE_NAME) {
        this.RULE_NAME = RULE_NAME;
    }

    public void setRULE_DUTY(String RULE_DUTY) {
        this.RULE_DUTY = RULE_DUTY;
    }

    public int getCREATOR_ID() {
        return CREATOR_ID;
    }

    public String getCREATOR_NAME() {
        return CREATOR_NAME;
    }

    public String getSUBS_ID() {
        return SUBS_ID;
    }

    public String getSUBS_NAME() {
        return SUBS_NAME;
    }

    public String getDEPT_ID() {
        return DEPT_ID;
    }

    public String getDEPT_NAME() {
        return DEPT_NAME;
    }

    public String getRULE_NAME() {
        return RULE_NAME;
    }

    public String getRULE_DUTY() {
        return RULE_DUTY;
    }
}