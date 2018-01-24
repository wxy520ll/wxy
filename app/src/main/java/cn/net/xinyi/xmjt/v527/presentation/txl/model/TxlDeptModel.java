package cn.net.xinyi.xmjt.v527.presentation.txl.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class TxlDeptModel implements MultiItemEntity {
    /**
     * ORGCODE : 11
     * ORGNAME : 管道公司
     * PID : 4
     * ID : 153
     * CREATETIME : 2017-01-16 12:02:52.000
     */

    private String ORGCODE;
    private String ORGNAME;
    private int PID;
    private int ID;
    private String CREATETIME;

    public String getORGCODE() {
        return ORGCODE;
    }

    public void setORGCODE(String ORGCODE) {
        this.ORGCODE = ORGCODE;
    }

    public String getORGNAME() {
        return ORGNAME;
    }

    public void setORGNAME(String ORGNAME) {
        this.ORGNAME = ORGNAME;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
