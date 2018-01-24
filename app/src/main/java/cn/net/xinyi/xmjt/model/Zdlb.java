package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2015/10/12.
 */
public class Zdlb implements Serializable {
    //字典
    private String ZDJB;
    //列表编码
    private String LBBM;
    //列表名称
    private String LBMC;
    //
    private String LY;
    //拼音
    private String PY;
     //备注
    private String BZ;
    //id
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getZDJB() {
        return ZDJB;
    }

    public void setZDJB(String ZDJB) {
        this.ZDJB = ZDJB;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getPY() {
        return PY;
    }

    public void setPY(String PY) {
        this.PY = PY;
    }

    public String getLY() {
        return LY;
    }

    public void setLY(String LY) {
        this.LY = LY;
    }

    public String getLBMC() {
        return LBMC;
    }

    public void setLBMC(String LBMC) {
        this.LBMC = LBMC;
    }

    public String getLBBM() {
        return LBBM;
    }

    public void setLBBM(String LBBM) {
        this.LBBM = LBBM;
    }
}
