package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/3/19.
 */
@DatabaseTable(tableName = "ZAJCXFModle")
public class ZAJCXFModle implements Serializable {
    /**
     * id 为主键
     * generatedId  自增长id
     * ID
     **/
    @DatabaseField(generatedId = true)
    private  int XFID;
    /** 灭火器数量**/
    @DatabaseField
    private String MHQSL;
    /** 正常数 **/
    @DatabaseField
    private String ZCS;
    /**到期日期 **/
    @DatabaseField
    private String DQRQ;
    /**消防全景图 **/
    @DatabaseField
    private String IV_XFQJT;
    public static final String sIV_XFQJT="iV_XFQJT";
    /**关联ID **/
    @DatabaseField
    private  String GLID;

    public String getGLID() {
        return GLID;
    }

    public void setGLID(String GLID) {
        this.GLID = GLID;
    }


    public ZAJCXFModle() {
    }

    public int getXFID() {
        return XFID;
    }

    public String getMHQSL() {
        return MHQSL;
    }

    public String getZCS() {
        return ZCS;
    }

    public String getDQRQ() {
        return DQRQ;
    }

    public String getIV_XFQJT() {
        return IV_XFQJT;
    }

    public void setXFID(int XFID) {
        this.XFID = XFID;
    }

    public void setMHQSL(String MHQSL) {
        this.MHQSL = MHQSL;
    }

    public void setZCS(String ZCS) {
        this.ZCS = ZCS;
    }

    public void setDQRQ(String DQRQ) {
        this.DQRQ = DQRQ;
    }

    public void setIV_XFQJT(String IV_XFQJT) {
        this.IV_XFQJT = IV_XFQJT;
    }


}
