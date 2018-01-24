package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2015/9/18.\
 * 监控室信息采集
 */
public class JKSInfoModle implements Serializable {

 //存入本地数据库
 public static final int ISUPDATE_LOCAL = 0;
 // id
 private  int id;
 // 采集用户
 private String CJYH;
 // 采集单位
 private  String CJDW;
 // 采集时间
 private String CJSJ;
 //采集的三张图片
 private String ZP1;
 private String ZP2;
 private String ZP3;
 // 监控室名字
 private String JKSMC;
 private String JKSBH;
 // 采集地点
 private  String JKSWZ;
 // 监控室业务分类
 private  String JKSYWFL;
 private  String JKSYWFLBM;
 //经度
 private String JD="0.00000";
 //纬度
 private String WD ="0.00000";
 //基站ID
 private String cellID="0";
 //基站区域码
 private String cellLocationCode="0";
 //基站类型：1：移动，2：联通，3：电信
 private String networkCode="";
 // 楼栋编号
 private String LDBH;
 // 摄像头数量
 private int SXTSL;
 // 正常摄像头数量
 private int ZCSYSXTSL;
 // 安装时间
 private String AZSJ;
 // 持证上岗人数
 private int CZSGRS;
 // 责任人
 private  String ZRR;
 // 联系电话
 private String LXDH;
 // 所在派出所
 private String SSPCS;
 // 所选择派出所ID
 private String policeId;
 // 所属社区
 private String SSWG;
 // 备注
 private String BZ;
 // 水平储备厂商
 private String SPCZCS;
 //定位方式
 private String LOCTYPE ="";
 //保存或上传标志
 private String isupdate="";
 //上传时间
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

 public String getJKSYWFL() {
  return JKSYWFL;
 }

 public void setJKSYWFL(String JKSYWFL) {
  this.JKSYWFL = JKSYWFL;
 }

 public String getJKSBH() {
  return JKSBH;
 }

 public void setJKSBH(String JKSBH) {
  this.JKSBH = JKSBH;
 }

 public String getPoliceId() {
  return policeId;
 }

 public void setPoliceId(String policeId) {
  this.policeId = policeId;
 }

 public String getJKSYWFLBM() {
  return JKSYWFLBM;
 }

 public void setJKSYWFLBM(String JKSYWFLBM) {
  this.JKSYWFLBM = JKSYWFLBM;
 }

 public String getCellLocationCode() {
  return cellLocationCode;
 }

 public void setCellLocationCode(String cellLocationCode) {
  this.cellLocationCode = cellLocationCode;
 }

 public String getCellID() {
  return cellID;
 }

 public void setCellID(String cellID) {
  this.cellID = cellID;
 }

 public String getNetworkCode() {
  return networkCode;
 }

 public void setNetworkCode(String networkCode) {
  this.networkCode = networkCode;
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

 public String getCJDW() {
  return CJDW;
 }

 public String getCJSJ() {
  return CJSJ;
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

 public String getJKSMC() {
  return JKSMC;
 }

 public String getJD() {
  return JD;
 }


 public String getAZSJ() {
  return AZSJ;
 }

 public String getJKSWZ() {
  return JKSWZ;
 }

 public String getWD() {
  return WD;
 }

 public String getLDBH() {
  return LDBH;
 }

 public int getSXTSL() {
  return SXTSL;
 }

 public int getZCSYSXTSL() {
  return ZCSYSXTSL;
 }

 public int getCZSGRS() {
  return CZSGRS;
 }

 public String getZRR() {
  return ZRR;
 }

 public String getLXDH() {
  return LXDH;
 }

 public String getSSPCS() {
  return SSPCS;
 }

 public String getSSWG() {
  return SSWG;
 }

 public String getBZ() {
  return BZ;
 }

 public String getSPCZCS() {
  return SPCZCS;
 }

 public void setCJYH(String CJYH) {
  this.CJYH = CJYH;
 }

 public void setCJDW(String CJDW) {
  this.CJDW = CJDW;
 }

 public void setCJSJ(String CJSJ) {
  this.CJSJ = CJSJ;
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

 public void setJKSMC(String JKSMC) {
  this.JKSMC = JKSMC;
 }

 public void setJKSWZ(String JKSWZ) {
  this.JKSWZ = JKSWZ;
 }

 public void setJD(String JD) {
  this.JD = JD;
 }

 public void setWD(String WD) {
  this.WD = WD;
 }

 public void setLDBH(String LDBH) {
  this.LDBH = LDBH;
 }


 public void setSXTSL(int SXTSL) {
  this.SXTSL = SXTSL;
 }

 public void setZCSYSXTSL(int ZCSYSXTSL) {
  this.ZCSYSXTSL = ZCSYSXTSL;
 }

 public void setAZSJ(String AZSJ) {
  this.AZSJ = AZSJ;
 }

 public void setCZSGRS(int CZSGRS) {
  this.CZSGRS = CZSGRS;
 }

 public void setZRR(String ZRR) {
  this.ZRR = ZRR;
 }

 public void setLXDH(String LXDH) {
  this.LXDH = LXDH;
 }

 public void setSSPCS(String SSPCS) {
  this.SSPCS = SSPCS;
 }

 public void setSSWG(String SSWG) {
  this.SSWG = SSWG;
 }

 public void setBZ(String BZ) {
  this.BZ = BZ;
 }

 public void setSPCZCS(String SPCZCS) {
  this.SPCZCS = SPCZCS;
 }


}
