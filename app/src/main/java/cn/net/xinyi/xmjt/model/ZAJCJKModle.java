package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/3/19.
 */
@DatabaseTable(tableName = "ZAJCJKModle")
public class ZAJCJKModle implements Serializable {
    /**
     * id 为主键
     * generatedId  自增长id
     * ID
     **/
    @DatabaseField(generatedId = true)
    private  int JKID;
    public static  String sJKID="JKID";
    @DatabaseField
    private String JKSBH;

    @DatabaseField
    private String SXTZS;

    @DatabaseField
    private String ZCS;

    @DatabaseField
    private String MKSXTS;

    @DatabaseField
    private String IV_JKPMT;
    public static final String sIV_JKPMT="iV_JKPMT";
    @DatabaseField
    private String IV_SXTQJT;
    public static final String sIV_SXTQJT="iV_SXTQJT";
    /**关联ID **/
    @DatabaseField
    private  String GLID;

    public String getGLID() {
        return GLID;
    }

    public void setGLID(String GLID) {
        this.GLID = GLID;
    }


    public ZAJCJKModle() {
    }

    public String getJKSBH() {
        return JKSBH;
    }

    public void setJKSBH(String JKSBH) {
        this.JKSBH = JKSBH;
    }

    public int getJKID() {
        return JKID;
    }

    public String getSXTZS() {
        return SXTZS;
    }

    public String getZCS() {
        return ZCS;
    }

    public String getMKSXTS() {
        return MKSXTS;
    }

    public String getIV_JKPMT() {
        return IV_JKPMT;
    }

    public String getIV_SXTQJT() {
        return IV_SXTQJT;
    }

    public void setJKID(int JKID) {
        this.JKID = JKID;
    }

    public void setSXTZS(String SXTZS) {
        this.SXTZS = SXTZS;
    }

    public void setZCS(String ZCS) {
        this.ZCS = ZCS;
    }

    public void setMKSXTS(String MKSXTS) {
        this.MKSXTS = MKSXTS;
    }

    public void setIV_JKPMT(String IV_JKPMT) {
        this.IV_JKPMT = IV_JKPMT;
    }

    public void setIV_SXTQJT(String IV_SXTQJT) {
        this.IV_SXTQJT = IV_SXTQJT;
    }


}
