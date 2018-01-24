package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/2/25.
 */
@DatabaseTable(tableName = "PerReturnModle")
public class PerReturnModle  implements Serializable{

    /**
     * id 为主键
     * generatedId  自增长id
     * ID
     **/
    @DatabaseField(generatedId = true)
    private  int id;
    /***走访人姓名*/
    @DatabaseField
    private String NAME;
    /***走访人身份证*/
    @DatabaseField
    private String SFZH;
    /***走访地址*/
    @DatabaseField
    private String ZFDZ;
    /***走访结果*/
    @DatabaseField
    private String HFJG;
    /***走访楼栋编号*/
    @DatabaseField
    private String HFLDBM;
    /***走访后地址*/
    @DatabaseField
    private String ZFHDZ;
    /***坐标  经纬度*/
    @DatabaseField
    private Double jd;
    @DatabaseField
    private Double wd;
    /***照片*/
    @DatabaseField
    private String BZ;
    /***楼宇照片1*/
    @DatabaseField
    private String IV_LYQZZP;
    /***门牌照片*/
    @DatabaseField
    private String IV_MPHZP;
    /***其他照片*/
    @DatabaseField
    private String IV_QTZP;
    /***更新时间*/
    @DatabaseField
    private String CJSJ;
    /***定位类型**/
    @DatabaseField
    private String LOCTYPE;
    /***上传时间**/
    @DatabaseField
    private String SCSJ;

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    /***楼宇照片1*/
    public static String sLYQZZP="iV_LYQZZP";
    /***门牌照片*/
    public static String sMPHZP="iV_MPHZP";
    /***其他照片*/
    public static String sQTZP="iV_QTZP";

    public PerReturnModle() {
    }

    public String getLOCTYPE() {
        return LOCTYPE;
    }

    public void setLOCTYPE(String LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }

    public int getId() {
        return id;
    }

    public String getZFDZ() {
        return ZFDZ;
    }

    public void setZFDZ(String ZFDZ) {
        this.ZFDZ = ZFDZ;
    }

    public String getNAME() {
        return NAME;
    }

    public String getSFZH() {
        return SFZH;
    }


    public String getHFJG() {
        return HFJG;
    }

    public String getHFLDBM() {
        return HFLDBM;
    }

    public String getZFHDZ() {
        return ZFHDZ;
    }

    public Double getJd() {
        return jd;
    }

    public Double getWd() {
        return wd;
    }

    public String getBZ() {
        return BZ;
    }

    public String getIV_LYQZZP() {
        return IV_LYQZZP;
    }

    public String getCJSJ() {
        return CJSJ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setSFZH(String SFZH) {
        this.SFZH = SFZH;
    }

    public void setHFJG(String HFJG) {
        this.HFJG = HFJG;
    }

    public void setHFLDBM(String HFLDBM) {
        this.HFLDBM = HFLDBM;
    }

    public void setZFHDZ(String ZFHDZ) {
        this.ZFHDZ = ZFHDZ;
    }

    public void setJd(Double jd) {
        this.jd = jd;
    }

    public void setWd(Double wd) {
        this.wd = wd;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public void setIV_LYQZZP(String IV_LYQZZP) {
        this.IV_LYQZZP = IV_LYQZZP;
    }


    public void setCJSJ(String CJSJ) {
        this.CJSJ = CJSJ;
    }
}
