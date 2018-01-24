package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2015/9/18.
 */
public class SXTInfoModle implements Serializable {
    // id
    private  int id;
    // 采集用户
    private String CJYH;
    // 采集用户
    private String SSWG;
    // 采集单位
    private  String CJDW;
    // 采集时间
    private String CJSJ;
    //采集的三张图片
    private String ZP1;
    private String ZP2;
    private String ZP3;
    //是否正常
    private String SFZC;
    // 采集地点
    private  String SXTWZ;
    //经度
    private String JD ="0.00000";
    //纬度
    private String WD ="0.00000";
    // 楼栋编号
    private String LDBH;
    // 安装时间
    private String AZSJ;
    // 监控室名称
    private String JKSBH;
    //监控室名称
    private String JKSMC;
    //摄像头编号
    private String SXTBH;
    // 摄像头类型
    private String SXTLX;
    // 楼栋方向
    private String SXTFX;
    // 摄像头类别
    private  String SXTLB;
    // 摄像头在的场所
    private String CSFL;
    // 视频保存时间
    private String SPBCQX;
    // 所属的环境
    private String SSHJ;
    // 备注
    private String BZ;
    // 生产厂家
    private String SCCS;
    // 建设单位
    private String JSDW;
    // ATMb编号
    private String ATMBH;
    //定位方式
    private String LOCTYPE="";
    //保存或上传标志
    private String isupdate="";
    private String cellID="0";
    //基站区域码
    private String cellLocationCode="0";
    //基站区域码
    private String SCSJ;
    //审核失败原因
    private String SHSBYY;
    //审核失败备注
    private String SHSBSM;

    public String getSHSBYY() {
        return SHSBYY;
    }

    public void setSHSBYY(String SHSBYY) {
        this.SHSBYY = SHSBYY;
    }

    public String getSHSBSM() {
        return SHSBSM;
    }

    public void setSHSBSM(String SHSBSM) {
        this.SHSBSM = SHSBSM;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public String getSSWG() {
        return SSWG;
    }

    public void setSSWG(String SSWG) {
        this.SSWG = SSWG;
    }

    public String getJKSMC() {
        return JKSMC;
    }

    public void setJKSMC(String JKSMC) {
        this.JKSMC = JKSMC;
    }

    public String getSXTBH() {
        return SXTBH;
    }

    public void setSXTBH(String SXTBH) {
        this.SXTBH = SXTBH;
    }

    public String getCellID() {
        return cellID;
    }

    public void setCellID(String cellID) {
        this.cellID = cellID;
    }

    public String getCellLocationCode() {
        return cellLocationCode;
    }

    public void setCellLocationCode(String cellLocationCode) {
        this.cellLocationCode = cellLocationCode;
    }

    public String getSFZC() {
        return SFZC;
    }

    public void setSFZC(String SFZC) {
        this.SFZC = SFZC;
    }
    public String getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getCJYH() {
        return CJYH;
    }

    public void setCJYH(String CJYH) {
        this.CJYH = CJYH;
    }

    public String getCJDW() {
        return CJDW;
    }

    public void setCJDW(String CJDW) {
        this.CJDW = CJDW;
    }

    public String getZP2() {
        return ZP2;
    }

    public void setZP2(String ZP2) {
        this.ZP2 = ZP2;
    }

    public String getZP1() {
        return ZP1;
    }

    public void setZP1(String ZP1) {
        this.ZP1 = ZP1;
    }

    public String getCJSJ() {
        return CJSJ;
    }

    public void setCJSJ(String CJSJ) {
        this.CJSJ = CJSJ;
    }

    public String getSSHJ() {
        return SSHJ;
    }

    public void setSSHJ(String SSHJ) {
        this.SSHJ = SSHJ;
    }

    public String getZP3() {
        return ZP3;
    }

    public void setZP3(String ZP3) {
        this.ZP3 = ZP3;
    }

    public String getSXTWZ() {
        return SXTWZ;
    }

    public void setSXTWZ(String SXTWZ) {
        this.SXTWZ = SXTWZ;
    }

    public String getJD() {
        return JD;
    }

    public void setJD(String JD) {
        this.JD = JD;
    }

    public String getWD() {
        return WD;
    }

    public void setWD(String WD) {
        this.WD = WD;
    }

    public String getLDBH() {
        return LDBH;
    }

    public void setLDBH(String LDBH) {
        this.LDBH = LDBH;
    }

    public String getAZSJ() {
        return AZSJ;
    }

    public void setAZSJ(String AZSJ) {
        this.AZSJ = AZSJ;
    }

    public String getJKSBH() {
        return JKSBH;
    }

    public void setJKSBH(String JKSBH) {
        this.JKSBH = JKSBH;
    }

    public String getSXTLX() {
        return SXTLX;
    }

    public void setSXTLX(String SXTLX) {
        this.SXTLX = SXTLX;
    }

    public String getSXTFX() {
        return SXTFX;
    }

    public void setSXTFX(String SXTFX) {
        this.SXTFX = SXTFX;
    }

    public String getSXTLB() {
        return SXTLB;
    }

    public void setSXTLB(String SXTLB) {
        this.SXTLB = SXTLB;
    }

    public String getCSFL() {
        return CSFL;
    }

    public void setCSFL(String CSFL) {
        this.CSFL = CSFL;
    }

    public String getSPBCQX() {
        return SPBCQX;
    }

    public void setSPBCQX(String SPBCQX) {
        this.SPBCQX = SPBCQX;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getSCCS() {
        return SCCS;
    }

    public void setSCCS(String SCCS) {
        this.SCCS = SCCS;
    }

    public String getJSDW() {
        return JSDW;
    }

    public void setJSDW(String JSDW) {
        this.JSDW = JSDW;
    }

    public String getATMBH() {
        return ATMBH;
    }

    public void setATMBH(String ATMBH) {
        this.ATMBH = ATMBH;
    }


}
