package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2015/10/12.
 */
@DatabaseTable(tableName = "table_zdxx")
public class Zdxx implements Serializable {

    //id
    @DatabaseField(generatedId = true)
    private int ID;
    //字典列表
    @DatabaseField
    private String ZDLB;
    //父字典编码
    @DatabaseField
    private String FZDBM;
    //字典编码
    @DatabaseField
    private String ZDBM;
    //字典值
    @DatabaseField
    private String ZDZ;
    //拼音
    @DatabaseField
    private String PY;
    //
    @DatabaseField
    private String XH;
    //备注
    @DatabaseField
    private String BZ;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getZDLB() {
        return ZDLB;
    }

    public void setZDLB(String ZDLB) {
        this.ZDLB = ZDLB;
    }

    public String getFZDBM() {
        return FZDBM;
    }

    public void setFZDBM(String FZDBM) {
        this.FZDBM = FZDBM;
    }

    public String getZDBM() {
        return ZDBM;
    }

    public void setZDBM(String ZDBM) {
        this.ZDBM = ZDBM;
    }

    public String getZDZ() {
        return ZDZ;
    }

    public void setZDZ(String ZDZ) {
        this.ZDZ = ZDZ;
    }

    public String getPY() {
        return PY;
    }

    public void setPY(String PY) {
        this.PY = PY;
    }

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }
}
