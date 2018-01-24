package cn.net.xinyi.xmjt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/3/19.
 */
@DatabaseTable(tableName = "ZAJCModle")
public class ZAJCModle implements Serializable {
    /**
     * id 为主键
     * generatedId  自增长id
     * ID
     **/
    @DatabaseField(generatedId = true)
    private  int ZAJCID;
    private  int ID;
    public static   String sZAJCID="zAJCID";
    @DatabaseField
    private String LB;
    @DatabaseField
    private String CJFL;
    /***  名称 */
    @DatabaseField
    private String MC;
    /***  地址*/
    @DatabaseField
    private String DZ;
    /***  门牌号*/
    @DatabaseField
    private String MPH;
    /***  楼栋编码 */
    @DatabaseField
    private String LDBM;
    /*** 门牌号照片  */
    @DatabaseField
    private String IV_MPHQJT;
    public static final String sIV_MPHQJT="iV_MPHQJT";
    /***   店门照片*/
    @DatabaseField
    private String IV_DMQJT;
    public static final String sIV_DMQJT="iV_DMQJT";
    /***  经营者性质 */
    @DatabaseField
    private String JYZXM;
    /*** 经营者电话  */
    @DatabaseField
    private String JYZDH;
    /***  经营者服务电话 */
    @DatabaseField
    private String FWDH;
    /***  其他 */
    @DatabaseField
    private String IV_QT;
    public static final String sIV_QT="iV_QT";
    /***  营业执照 */
    @DatabaseField
    private String IV_YYZZ;
    public static final String sIV_YYZZ="iV_YYZZ";
    /***  环保批文 */
    @DatabaseField
    private String IV_HBPW;
    public static final String sIV_HBPW="iV_HBPW";
    /***  消防验收 */
    @DatabaseField
    private String IV_XFYS;
    public static final String sIV_XFYS="iV_XFYS";
    /***  特种经营许可 */
    @DatabaseField
    private String IV_TZHY;
    public static final String sIV_TZHY="iV_TZHY";
    /*** 治安负责人  */
    @DatabaseField
    private String ZAFZR;
    /***  所属派出所 */
    @DatabaseField
    private String SSPCS;
    /*** 所属警务室  */
    @DatabaseField
    private String SSJWS;
    /*** 经度  */
    @DatabaseField
    private Double JD;
    /***  纬度*/
    @DatabaseField
    private Double WD;
    /*** 上传时间 */
    @DatabaseField
    private String SCSJ;
    /*** 治安负责人电话*/
    @DatabaseField
    private String ZAFZRDH;
    /*** 是否有监控*/
    @DatabaseField
    private int SFCJJK;
    /*** 是否有消防设施  */
    @DatabaseField
    private int SFCJXF;
    /*** 采集时间  */
    @DatabaseField
    private String CJSJ;
    /*** 定位类型  */
    @DatabaseField
    private String LOCTYPE;
    /*** 备注  */
    @DatabaseField
    private String BZ;
    /**关联ID **/
    @DatabaseField
    private  String GLID;

    private  String HCSJ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHCSJ() {
        return HCSJ;
    }

    public String getLB() {
        return LB;
    }

    public void setLB(String LB) {
        this.LB = LB;
    }

    public String getIV_QT() {
        return IV_QT;
    }

    public void setIV_QT(String IV_QT) {
        this.IV_QT = IV_QT;
    }

    public String getIV_HBPW() {
        return IV_HBPW;
    }

    public void setIV_HBPW(String IV_HBPW) {
        this.IV_HBPW = IV_HBPW;
    }

    public String getIV_XFYS() {
        return IV_XFYS;
    }

    public void setIV_XFYS(String IV_XFYS) {
        this.IV_XFYS = IV_XFYS;
    }

    public String getIV_TZHY() {
        return IV_TZHY;
    }

    public void setIV_TZHY(String IV_TZHY) {
        this.IV_TZHY = IV_TZHY;
    }

    public String getMPH() {
        return MPH;
    }

    public void setMPH(String MPH) {
        this.MPH = MPH;
    }

    public String getGLID() {
        return GLID;
    }

    public void setGLID(String GLID) {
        this.GLID = GLID;
    }

    public ZAJCModle() {
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public int getZAJCID() {
        return ZAJCID;
    }

    public String getCJFL() {
        return CJFL;
    }

    public String getMC() {
        return MC;
    }

    public String getDZ() {
        return DZ;
    }

    public String getLDBM() {
        return LDBM;
    }

    public String getIV_MPHQJT() {
        return IV_MPHQJT;
    }

    public String getIV_DMQJT() {
        return IV_DMQJT;
    }

    public String getJYZXM() {
        return JYZXM;
    }

    public String getJYZDH() {
        return JYZDH;
    }

    public String getFWDH() {
        return FWDH;
    }

    public String getIV_YYZZ() {
        return IV_YYZZ;
    }

    public String getZAFZR() {
        return ZAFZR;
    }

    public String getSSPCS() {
        return SSPCS;
    }

    public String getSSJWS() {
        return SSJWS;
    }

    public Double getJD() {
        return JD;
    }

    public Double getWD() {
        return WD;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public String getZAFZRDH() {
        return ZAFZRDH;
    }

    public int getSFCJJK() {
        return SFCJJK;
    }

    public int getSFCJXF() {
        return SFCJXF;
    }

    public String getCJSJ() {
        return CJSJ;
    }

    public String getLOCTYPE() {
        return LOCTYPE;
    }

    public void setZAJCID(int ZAJCID) {
        this.ZAJCID = ZAJCID;
    }

    public void setCJFL(String CJFL) {
        this.CJFL = CJFL;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public void setLDBM(String LDBM) {
        this.LDBM = LDBM;
    }

    public void setIV_MPHQJT(String IV_MPHQJT) {
        this.IV_MPHQJT = IV_MPHQJT;
    }

    public void setIV_DMQJT(String IV_DMQJT) {
        this.IV_DMQJT = IV_DMQJT;
    }

    public void setJYZXM(String JYZXM) {
        this.JYZXM = JYZXM;
    }

    public void setJYZDH(String JYZDH) {
        this.JYZDH = JYZDH;
    }

    public void setFWDH(String FWDH) {
        this.FWDH = FWDH;
    }

    public void setIV_YYZZ(String IV_YYZZ) {
        this.IV_YYZZ = IV_YYZZ;
    }

    public void setZAFZR(String ZAFZR) {
        this.ZAFZR = ZAFZR;
    }

    public void setSSPCS(String SSPCS) {
        this.SSPCS = SSPCS;
    }

    public void setSSJWS(String SSJWS) {
        this.SSJWS = SSJWS;
    }

    public void setJD(Double JD) {
        this.JD = JD;
    }

    public void setWD(Double WD) {
        this.WD = WD;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public void setZAFZRDH(String ZAFZRDH) {
        this.ZAFZRDH = ZAFZRDH;
    }

    public void setSFCJJK(int SFCJJK) {
        this.SFCJJK = SFCJJK;
    }

    public void setSFCJXF(int SFCJXF) {
        this.SFCJXF = SFCJXF;
    }

    public void setCJSJ(String CJSJ) {
        this.CJSJ = CJSJ;
    }

    public void setLOCTYPE(String LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }

    public void setHCSJ(String HCSJ) {
        this.HCSJ = HCSJ;
    }
}
