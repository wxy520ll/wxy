package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/3/31.
 */
@DatabaseTable(tableName = "JQInfoModle")
public class JQInfoModle implements Serializable {
    /**
     * id 为主键
     * generatedId  自增长id
     * ID
     **/
    @DatabaseField(generatedId = true)
    private  int id;
    /***警情编号*/
    @DatabaseField
    private String JQBH;
    /***是否案发地点*/
    @DatabaseField
    private int SFAFDD;
    /***是否报警地点*/
    @DatabaseField
    private int SFBJDD;
    /***是否涉刀案件*/
    @DatabaseField
    private int SFSDAJ;
    /***联系电话*/
    @DatabaseField
    private String LXDH;
    /***联系电话1*/
    @DatabaseField
    private String LXDH1;
    /***联系电话2*/
    @DatabaseField
    private String LXDH2;
    /***警情地址*/
    @DatabaseField
    private String DZ;
    /***经度*/
    @DatabaseField
    private Double JD;
    /***纬度*/
    @DatabaseField
    private Double WD;
    /***警情备注*/
    @DatabaseField
    private String BZ;
    /***照片1*/
    @DatabaseField
    private String IV_ZP1;
    /***照片2*/
    @DatabaseField
    private String IV_ZP2;
    /***照片3*/
    @DatabaseField
    private String IV_ZP3;
    /***采集时间*/
    @DatabaseField
    private String CJSJ;
    /***定位类型*/
    @DatabaseField
    private  String LOCTYPE;
    /***上传时间**/
    @DatabaseField
    private String SCSJ;
    /***报警地点**/
    @DatabaseField
    private String BJDD;
    /***警情类别**/
    @DatabaseField
    private String JQLB;
    /***报警人姓名**/
    @DatabaseField
    private String BJRXM;
    /***报警人姓别**/
    @DatabaseField
    private String BJRXB;
    /***报警人学历**/
    @DatabaseField
    private String BJRXL;
    /***报警人年龄**/
    @DatabaseField
    private String BJRNL;
    /***报警人籍贯**/
    @DatabaseField
    private String BJRJG;
    /***报警人居住地**/
    @DatabaseField
    private String BJRJZD;




    public JQInfoModle() {
    }


    public int getSFSDAJ() {
        return SFSDAJ;
    }

    public void setSFSDAJ(int SFSDAJ) {
        this.SFSDAJ = SFSDAJ;
    }

    public int getId() {
        return id;
    }

    public String getJQBH() {
        return JQBH;
    }

    public int getSFAFDD() {
        return SFAFDD;
    }

    public int getSFBJDD() {
        return SFBJDD;
    }

    public String getLXDH() {
        return LXDH;
    }

    public String getLXDH1() {
        return LXDH1;
    }

    public String getLXDH2() {
        return LXDH2;
    }

    public String getDZ() {
        return DZ;
    }

    public Double getJD() {
        return JD;
    }

    public Double getWD() {
        return WD;
    }

    public String getBZ() {
        return BZ;
    }

    public String getIV_ZP1() {
        return IV_ZP1;
    }

    public String getIV_ZP2() {
        return IV_ZP2;
    }

    public String getIV_ZP3() {
        return IV_ZP3;
    }

    public String getBJDD() {
        return BJDD;
    }

    public String getJQLB() {
        return JQLB;
    }

    public String getBJRXM() {
        return BJRXM;
    }

    public String getBJRXB() {
        return BJRXB;
    }

    public String getBJRXL() {
        return BJRXL;
    }

    public String getBJRNL() {
        return BJRNL;
    }

    public String getBJRJG() {
        return BJRJG;
    }

    public String getBJRJZD() {
        return BJRJZD;
    }

    public void setBJDD(String BJDD) {
        this.BJDD = BJDD;
    }

    public void setJQLB(String JQLB) {
        this.JQLB = JQLB;
    }

    public void setBJRXM(String BJRXM) {
        this.BJRXM = BJRXM;
    }

    public void setBJRXB(String BJRXB) {
        this.BJRXB = BJRXB;
    }

    public void setBJRXL(String BJRXL) {
        this.BJRXL = BJRXL;
    }

    public void setBJRNL(String BJRNL) {
        this.BJRNL = BJRNL;
    }

    public void setBJRJG(String BJRJG) {
        this.BJRJG = BJRJG;
    }

    public void setBJRJZD(String BJRJZD) {
        this.BJRJZD = BJRJZD;
    }

    public String getCJSJ() {
        return CJSJ;
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

    public void setJQBH(String JQBH) {
        this.JQBH = JQBH;
    }

    public void setSFAFDD(int SFAFDD) {
        this.SFAFDD = SFAFDD;
    }

    public void setSFBJDD(int SFBJDD) {
        this.SFBJDD = SFBJDD;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public void setLXDH1(String LXDH1) {
        this.LXDH1 = LXDH1;
    }

    public void setLXDH2(String LXDH2) {
        this.LXDH2 = LXDH2;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public void setJD(Double JD) {
        this.JD = JD;
    }

    public void setWD(Double WD) {
        this.WD = WD;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public void setIV_ZP1(String IV_ZP1) {
        this.IV_ZP1 = IV_ZP1;
    }

    public void setIV_ZP2(String IV_ZP2) {
        this.IV_ZP2 = IV_ZP2;
    }

    public void setIV_ZP3(String IV_ZP3) {
        this.IV_ZP3 = IV_ZP3;
    }


    public void setCJSJ(String CJSJ) {
        this.CJSJ = CJSJ;
    }

    public void setLOCTYPE(String LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }
}
