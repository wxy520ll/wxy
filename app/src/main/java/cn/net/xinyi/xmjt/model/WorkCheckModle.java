package cn.net.xinyi.xmjt.model;

/**
 * Created by hao.zhou on 2016/6/21.
 */
public class WorkCheckModle {
    private String ID;
    private String BZ;//文本备注信息
    private String IV_ZP1 ;//照片1
    private String IV_ZP2 ;//照片2
    private String IV_ZP3 ;//照片3
    private Double WD    ;//纬度
    private Double JD    ;//经度
    private String DZ    ;//地址
    private String DCRY  ;//督查人员
    private String DCDW  ;//督查单位
    private String SCSJ  ;//上传时间
    private int LOCTYPE  ;//定位类型

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getIV_ZP1() {
        return IV_ZP1;
    }

    public void setIV_ZP1(String IV_ZP1) {
        this.IV_ZP1 = IV_ZP1;
    }

    public String getIV_ZP2() {
        return IV_ZP2;
    }

    public void setIV_ZP2(String IV_ZP2) {
        this.IV_ZP2 = IV_ZP2;
    }

    public String getIV_ZP3() {
        return IV_ZP3;
    }

    public void setIV_ZP3(String IV_ZP3) {
        this.IV_ZP3 = IV_ZP3;
    }

    public Double getWD() {
        return WD;
    }

    public void setWD(Double WD) {
        this.WD = WD;
    }

    public Double getJD() {
        return JD;
    }

    public void setJD(Double JD) {
        this.JD = JD;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public String getDCRY() {
        return DCRY;
    }

    public void setDCRY(String DCRY) {
        this.DCRY = DCRY;
    }

    public String getDCDW() {
        return DCDW;
    }

    public void setDCDW(String DCDW) {
        this.DCDW = DCDW;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public int getLOCTYPE() {
        return LOCTYPE;
    }

    public void setLOCTYPE(int LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }
}
