package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by hao.zhou on 2016/3/2.
 */
@DatabaseTable(tableName = "SSPInfoModle")
public class SSPInfoModle {
    /**
     * id 为主键
     * generatedId  自增长id
     * ID
     **/
    @DatabaseField(generatedId = true)
    private  int id;
    /**描述**/
    @DatabaseField
    private  String DESCRIPTION;
    /**经度**/
    @DatabaseField
    private  Double JD;
    /**纬度**/
    @DatabaseField
    private  Double WD;
    /**地址**/
    @DatabaseField
    private  String DZ;
    /**照片1**/
    @DatabaseField
    private  String ZP1;
    public static  String sZP1="zP1";
    /**照片2**/
    @DatabaseField
    private  String ZP2;
    public static  String sZP2="zP2";
    /**照片3**/
    @DatabaseField
    private  String ZP3;
    public static  String sZP3="zP3";
    /**采集时间**/
    @DatabaseField
    private  String CJSJ;
    /**随手拍类型**/
    @DatabaseField
    private  String TYPE;
    /**定位类型**/
    @DatabaseField
    private  String LOCTYPE;
    /***上传时间**/
    @DatabaseField
    private String SCSJ;

    public int getId() {
        return id;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public Double getJD() {
        return JD;
    }

    public Double getWD() {
        return WD;
    }

    public String getDZ() {
        return DZ;
    }

    public String getZP1() {
        return ZP1;
    }

    public String getZP2() {
        return ZP2;
    }

    public String getZP3() {
        return ZP3;
    }

    public String getCJSJ() {
        return CJSJ;
    }

    public String getTYPE() {
        return TYPE;
    }

    public String getLOCTYPE() {
        return LOCTYPE;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public void setJD(Double JD) {
        this.JD = JD;
    }

    public void setWD(Double WD) {
        this.WD = WD;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public void setZP1(String ZP1) {
        this.ZP1 = ZP1;
    }

    public void setZP2(String ZP2) {
        this.ZP2 = ZP2;
    }

    public void setZP3(String ZP3) {
        this.ZP3 = ZP3;
    }

    public void setCJSJ(String CJSJ) {
        this.CJSJ = CJSJ;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public void setLOCTYPE(String LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }
}
