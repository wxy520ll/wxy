package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/11/16.
 */
public class MachineFowModle implements Serializable {
    private int   ID	;//	编号
    private String DW	;//	单位
    private String XM	;//	姓名
    private String SFZH;//	身份证号
    private String SJHM;//	手机号码
    private String MANAGER_SJHM;//	负责人手机号码
    private String SCJFCRZZP;//手持机房出入证照片
    private String ISCHECKED;//本次是否进入机房
    private String GXSJ;//更新时间


    public String getGXSJ() {
        return GXSJ;
    }

    public void setGXSJ(String GXSJ) {
        this.GXSJ = GXSJ;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDW() {
        return DW;
    }

    public void setDW(String DW) {
        this.DW = DW;
    }

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getSFZH() {
        return SFZH;
    }

    public void setSFZH(String SFZH) {
        this.SFZH = SFZH;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getMANAGER_SJHM() {
        return MANAGER_SJHM;
    }

    public void setMANAGER_SJHM(String MANAGER_SJHM) {
        this.MANAGER_SJHM = MANAGER_SJHM;
    }

    public String getSCJFCRZZP() {
        return SCJFCRZZP;
    }

    public void setSCJFCRZZP(String SCJFCRZZP) {
        this.SCJFCRZZP = SCJFCRZZP;
    }

    public String getISCHECKED() {
        return ISCHECKED;
    }

    public void setISCHECKED(String ISCHECKED) {
        this.ISCHECKED = ISCHECKED;
    }
}
