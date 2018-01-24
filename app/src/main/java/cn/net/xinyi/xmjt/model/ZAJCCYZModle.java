package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/3/19.
 */
@DatabaseTable(tableName = "ZAJCCYZModle")
public class ZAJCCYZModle implements Serializable {
    /**
     * id 为主键
     * generatedId  自增长id
     * ID
     **/
    @DatabaseField(generatedId = true)
    private  int CYZID;
    /** 从业者姓名**/
    @DatabaseField
    private String CYZXM;
    /**从业者身份证 **/
    @DatabaseField
    private String CYZSFZ;
    /**从业者电话 **/
    @DatabaseField
    private String CYZDH;
    /**从业者全身照 **/
    @DatabaseField
    private String IV_CYZQSZ;
    public static final String sIV_CYZQSZ="iV_CYZQSZ";
    /**关联ID **/
    @DatabaseField
    private  String GLID;

    public String getGLID() {
        return GLID;
    }

    public void setGLID(String GLID) {
        this.GLID = GLID;
    }


    public ZAJCCYZModle() {

    }

    public int getCYZID() {
        return CYZID;
    }

    public String getCYZXM() {
        return CYZXM;
    }

    public String getCYZSFZ() {
        return CYZSFZ;
    }

    public String getCYZDH() {
        return CYZDH;
    }

    public String getIV_CYZQSZ() {
        return IV_CYZQSZ;
    }

    public void setCYZID(int CYZID) {
        this.CYZID = CYZID;
    }

    public void setCYZXM(String CYZXM) {
        this.CYZXM = CYZXM;
    }

    public void setCYZSFZ(String CYZSFZ) {
        this.CYZSFZ = CYZSFZ;
    }

    public void setCYZDH(String CYZDH) {
        this.CYZDH = CYZDH;
    }

    public void setIV_CYZQSZ(String IV_CYZQSZ) {
        this.IV_CYZQSZ = IV_CYZQSZ;
    }

//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof ZAJCCYZModle)) return false;
//
//        ZAJCCYZModle that = (ZAJCCYZModle) o;
//
//        if (CYZXM != null ? !CYZXM.equals(that.CYZXM) : that.CYZXM != null) return false;
//        if (CYZSFZ != null ? !CYZSFZ.equals(that.CYZSFZ) : that.CYZSFZ != null) return false;
//        return !(GLID != null ? !GLID.equals(that.GLID) : that.GLID != null);
//
//    }

    @Override
    public int hashCode() {
        int result = CYZXM != null ? CYZXM.hashCode() : 0;
        result = 31 * result + (CYZSFZ != null ? CYZSFZ.hashCode() : 0);
        result = 31 * result + (GLID != null ? GLID.hashCode() : 0);
        return result;
    }
}
