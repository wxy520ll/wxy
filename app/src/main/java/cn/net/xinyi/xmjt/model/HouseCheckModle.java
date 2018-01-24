package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/9/14.
 */
public class HouseCheckModle implements Serializable {
    public HouseCheckModle() {
    }
    public static String lb_hyxq="花园小区";
    public static String lb_czc="城中村小区";
    public static String lb_gcss="工厂企业宿舍";
    public static String lb_xcqxx="小产权房屋信息";
    public static String lb_qt="其他";
    public static String[] sLB=new String[]{lb_hyxq,lb_czc,lb_gcss,lb_xcqxx,lb_qt};

    //类别
    public static String getType(String s) {
        String text = null;
        if (s.equals("01")){
            text=lb_hyxq;
        }else if (s .equals("02")){
            text=lb_czc;
        }else if (s .equals("03")){
            text=lb_gcss;
        }else if (s .equals("04")){
            text=lb_xcqxx;
        }else if (s .equals("05")){
            text=lb_qt;
        }
        return text;
    }

    public static String getTypeNum(String s) {
        String snum =null;
        if (s.equals(lb_hyxq)){
            snum="01";
        }else if (s.equals(lb_czc)){
            snum="02";
        }else if (s.equals(lb_gcss)){
            snum="03";
        }else if (s.equals(lb_xcqxx)){
            snum="04";
        }else if (s.equals(lb_qt)){
            snum="05";
        }
        return snum;
    }

    private int ID;//id
    private String TYPE;//出租屋类型
    private int SFKTJZDJZH;//是否开通居住登记账号
    private String WYMC;//物业名称
    private String ZZLDS;//住宅楼栋数
    private String BAYSL;//保安员数量
    private String MGSL;//门岗数量
    private String XLBARS;//巡逻保安人数
    private String BAPBQK;//保安排班情况
    private int SFYWYGLGS;//是否有物业管理公司
    private int SFYGLY;//是否有管理员
    private String BAZSQK;//保安值守情况
    private String SSGLQK;//宿舍管理情况
    private int SFPBLDZ;//是否配备楼栋长
    private int SFYZGL;//是否业主管理
    private String RFIMG;//人防图片
    private int SFPBLDFWZ;//是否配备楼栋服务站

    private int SFAZFDM;//是否安装防盗门
    private int SXSFBJS;//锁芯是否B级锁
    private int PSGSFAZPCHTMHY;//排水管是否安装爬刺和涂抹黄油
    private String WFIMG;//物防图片

    private int FWSFYXFQC;//房屋是否有消防器材
    private int SFYMHQ;//是否有灭火器
    private int SFYYJZMD;//是否有应急照明灯
    private int XFSZSFWH;//消防水闸是否完好
    private String XFIMG;//消防图片

    private String GGQYTTSL;//公共区域探头数量
    private String GQTTSL;//高清探头数量
    private String PTTTSL;//普通探头数量
    private int ZWWQSFAZTT;//周围外墙是否安装探头
    private int SFAZYPCSLWDYJBJ;//是否安装与派出所联网的一键报警
    private int SFAZSPMJ;//是否安装视频门禁
    private int SFYPCSLW;//是否与派出所联网
    private String SPSHSL;//视频损坏数量
    private int SFAZDJSPMJ;//是否安装单机视频门禁
    private String LXZLBCSJ;//录像资料保存时间
    private int MJSFSXSK;//门禁是否双向刷卡
    private String MJSHSL;//门禁损坏数量
    private String JFIMG;//技防图片

    private String CJYH;//采集用户
    private String SSPCS;//所属派出所
    private double LON;//经度
    private double LAT;//纬度
    private String LDID;//楼栋ID
    private String DZ;//地址
    private int LOCTYPE;//定位类型

    private String JD;//街道
    private String SQ;//街道
    private String HY;//花园
    private String DZX;//街道
    private String MPH;//门牌号
    private String XQZS;//统计用到  类别总数

    public String getXQZS() {
        return XQZS;
    }

    public void setXQZS(String XQZS) {
        this.XQZS = XQZS;
    }

    public String getMPH() {
        return MPH;
    }

    public void setMPH(String MPH) {
        this.MPH = MPH;
    }

    public String getDZX() {
        return DZX;
    }

    public void setDZX(String DZX) {
        this.DZX = DZX;
    }

    public String getHY() {
        return HY;
    }

    public void setHY(String HY) {
        this.HY = HY;
    }

    public String getSQ() {
        return SQ;
    }

    public void setSQ(String SQ) {
        this.SQ = SQ;
    }

    public String getJD() {
        return JD;
    }

    public void setJD(String JD) {
        this.JD = JD;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public int getLOCTYPE() {
        return LOCTYPE;
    }

    public void setLOCTYPE(int LOCTYPE) {
        this.LOCTYPE = LOCTYPE;
    }

    public double getLON() {
        return LON;
    }

    public void setLON(double LON) {
        this.LON = LON;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public String getLDID() {
        return LDID;
    }

    public void setLDID(String LDID) {
        this.LDID = LDID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public int getSFKTJZDJZH() {
        return SFKTJZDJZH;
    }

    public void setSFKTJZDJZH(int SFKTJZDJZH) {
        this.SFKTJZDJZH = SFKTJZDJZH;
    }

    public String getWYMC() {
        return WYMC;
    }

    public void setWYMC(String WYMC) {
        this.WYMC = WYMC;
    }

    public String getZZLDS() {
        return ZZLDS;
    }

    public void setZZLDS(String ZZLDS) {
        this.ZZLDS = ZZLDS;
    }

    public String getBAYSL() {
        return BAYSL;
    }

    public void setBAYSL(String BAYSL) {
        this.BAYSL = BAYSL;
    }

    public String getMGSL() {
        return MGSL;
    }

    public void setMGSL(String MGSL) {
        this.MGSL = MGSL;
    }

    public String getXLBARS() {
        return XLBARS;
    }

    public void setXLBARS(String XLBARS) {
        this.XLBARS = XLBARS;
    }

    public String getBAPBQK() {
        return BAPBQK;
    }

    public void setBAPBQK(String BAPBQK) {
        this.BAPBQK = BAPBQK;
    }

    public int getSFYWYGLGS() {
        return SFYWYGLGS;
    }

    public void setSFYWYGLGS(int SFYWYGLGS) {
        this.SFYWYGLGS = SFYWYGLGS;
    }

    public int getSFYGLY() {
        return SFYGLY;
    }

    public void setSFYGLY(int SFYGLY) {
        this.SFYGLY = SFYGLY;
    }

    public String getBAZSQK() {
        return BAZSQK;
    }

    public void setBAZSQK(String BAZSQK) {
        this.BAZSQK = BAZSQK;
    }

    public String getSSGLQK() {
        return SSGLQK;
    }

    public void setSSGLQK(String SSGLQK) {
        this.SSGLQK = SSGLQK;
    }

    public int getSFPBLDZ() {
        return SFPBLDZ;
    }

    public void setSFPBLDZ(int SFPBLDZ) {
        this.SFPBLDZ = SFPBLDZ;
    }

    public int getSFYZGL() {
        return SFYZGL;
    }

    public void setSFYZGL(int SFYZGL) {
        this.SFYZGL = SFYZGL;
    }

    public String getRFIMG() {
        return RFIMG;
    }

    public void setRFIMG(String RFIMG) {
        this.RFIMG = RFIMG;
    }

    public int getSFPBLDFWZ() {
        return SFPBLDFWZ;
    }

    public void setSFPBLDFWZ(int SFPBLDFWZ) {
        this.SFPBLDFWZ = SFPBLDFWZ;
    }

    public int getSFAZFDM() {
        return SFAZFDM;
    }

    public void setSFAZFDM(int SFAZFDM) {
        this.SFAZFDM = SFAZFDM;
    }

    public int getSXSFBJS() {
        return SXSFBJS;
    }

    public void setSXSFBJS(int SXSFBJS) {
        this.SXSFBJS = SXSFBJS;
    }

    public int getPSGSFAZPCHTMHY() {
        return PSGSFAZPCHTMHY;
    }

    public void setPSGSFAZPCHTMHY(int PSGSFAZPCHTMHY) {
        this.PSGSFAZPCHTMHY = PSGSFAZPCHTMHY;
    }

    public String getWFIMG() {
        return WFIMG;
    }

    public void setWFIMG(String WFIMG) {
        this.WFIMG = WFIMG;
    }

    public int getFWSFYXFQC() {
        return FWSFYXFQC;
    }

    public void setFWSFYXFQC(int FWSFYXFQC) {
        this.FWSFYXFQC = FWSFYXFQC;
    }

    public int getSFYMHQ() {
        return SFYMHQ;
    }

    public void setSFYMHQ(int SFYMHQ) {
        this.SFYMHQ = SFYMHQ;
    }

    public int getSFYYJZMD() {
        return SFYYJZMD;
    }

    public void setSFYYJZMD(int SFYYJZMD) {
        this.SFYYJZMD = SFYYJZMD;
    }

    public int getXFSZSFWH() {
        return XFSZSFWH;
    }

    public void setXFSZSFWH(int XFSZSFWH) {
        this.XFSZSFWH = XFSZSFWH;
    }

    public String getXFIMG() {
        return XFIMG;
    }

    public void setXFIMG(String XFIMG) {
        this.XFIMG = XFIMG;
    }

    public String getGGQYTTSL() {
        return GGQYTTSL;
    }

    public void setGGQYTTSL(String GGQYTTSL) {
        this.GGQYTTSL = GGQYTTSL;
    }

    public String getGQTTSL() {
        return GQTTSL;
    }

    public void setGQTTSL(String GQTTSL) {
        this.GQTTSL = GQTTSL;
    }

    public String getPTTTSL() {
        return PTTTSL;
    }

    public void setPTTTSL(String PTTTSL) {
        this.PTTTSL = PTTTSL;
    }

    public int getZWWQSFAZTT() {
        return ZWWQSFAZTT;
    }

    public void setZWWQSFAZTT(int ZWWQSFAZTT) {
        this.ZWWQSFAZTT = ZWWQSFAZTT;
    }

    public int getSFAZYPCSLWDYJBJ() {
        return SFAZYPCSLWDYJBJ;
    }

    public void setSFAZYPCSLWDYJBJ(int SFAZYPCSLWDYJBJ) {
        this.SFAZYPCSLWDYJBJ = SFAZYPCSLWDYJBJ;
    }

    public int getSFAZSPMJ() {
        return SFAZSPMJ;
    }

    public void setSFAZSPMJ(int SFAZSPMJ) {
        this.SFAZSPMJ = SFAZSPMJ;
    }

    public int getSFYPCSLW() {
        return SFYPCSLW;
    }

    public void setSFYPCSLW(int SFYPCSLW) {
        this.SFYPCSLW = SFYPCSLW;
    }

    public String getSPSHSL() {
        return SPSHSL;
    }

    public void setSPSHSL(String SPSHSL) {
        this.SPSHSL = SPSHSL;
    }

    public int getSFAZDJSPMJ() {
        return SFAZDJSPMJ;
    }

    public void setSFAZDJSPMJ(int SFAZDJSPMJ) {
        this.SFAZDJSPMJ = SFAZDJSPMJ;
    }

    public String getLXZLBCSJ() {
        return LXZLBCSJ;
    }

    public void setLXZLBCSJ(String LXZLBCSJ) {
        this.LXZLBCSJ = LXZLBCSJ;
    }

    public int getMJSFSXSK() {
        return MJSFSXSK;
    }

    public void setMJSFSXSK(int MJSFSXSK) {
        this.MJSFSXSK = MJSFSXSK;
    }

    public String getMJSHSL() {
        return MJSHSL;
    }

    public void setMJSHSL(String MJSHSL) {
        this.MJSHSL = MJSHSL;
    }

    public String getJFIMG() {
        return JFIMG;
    }

    public void setJFIMG(String JFIMG) {
        this.JFIMG = JFIMG;
    }

    public String getCJYH() {
        return CJYH;
    }

    public void setCJYH(String CJYH) {
        this.CJYH = CJYH;
    }

    public String getSSPCS() {
        return SSPCS;
    }

    public void setSSPCS(String SSPCS) {
        this.SSPCS = SSPCS;
    }



    //    //花园小区采集
//    public HouseCheckModle(String ID, String TYPE, String SFKTJZDJZH, String WYMC, String ZZLDS, String BAYSL, String XLBARS, String MGSL, String BAPBQK, String SFAZFDM, String SXSFBJS, String PSGSFAZPCHTMHY, String FWSFYXFQC, String SFYMHQ, int SFYYJZMD, int XFSZSFWH, int GGQYTTSL, int GQTTSL, int PTTTSL, int ZWWQSFAZTT, String SFAZYPCSLWDYJBJ) {
//        this.ID = ID;
//        this.TYPE = TYPE;
//        this.SFKTJZDJZH = SFKTJZDJZH;
//        this.WYMC = WYMC;
//        this.ZZLDS = ZZLDS;
//        this.BAYSL = BAYSL;
//        this.XLBARS = XLBARS;
//        this.MGSL = MGSL;
//        this.BAPBQK = BAPBQK;
//        this.SFAZFDM = SFAZFDM;
//        this.SXSFBJS = SXSFBJS;
//        this.PSGSFAZPCHTMHY = PSGSFAZPCHTMHY;
//        this.FWSFYXFQC = FWSFYXFQC;
//        this.SFYMHQ = SFYMHQ;
//        this.SFYYJZMD = SFYYJZMD;
//        this.XFSZSFWH = XFSZSFWH;
//        this.GGQYTTSL = GGQYTTSL;
//        this.GQTTSL = GQTTSL;
//        this.PTTTSL = PTTTSL;
//        this.ZWWQSFAZTT = ZWWQSFAZTT;
//        this.SFAZYPCSLWDYJBJ = SFAZYPCSLWDYJBJ;
//    }

//    //城中村采集
//    public HouseCheckModle(int ID, int TYPE, int SFKTJZDJZH, int SFYWYGLGS, String WYMC, int ZZLDS, int MGSL, int BAYSL, int XLBARS, String BAPBQK, int SFPBLDZ, int SFYZGL, int SFAZFDM, int SXSFBJS, int PSGSFAZPCHTMHY, int FWSFYXFQC, int SFYMHQ, int SFYYJZMD, int XFSZSFWH, int SFAZSPMJ, int GGQYTTSL, int GQTTSL, int PTTTSL, int ZWWQSFAZTT, int SFAZYPCSLWDYJBJ, int SFAZDJSPMJ, int SFPBLDFWZ, int MJSFSXSK, int LXZLBCSJ) {
//        this.ID = ID;
//        this.TYPE = TYPE;
//        this.SFKTJZDJZH = SFKTJZDJZH;
//        this.SFYWYGLGS = SFYWYGLGS;
//        this.WYMC = WYMC;
//        this.ZZLDS = ZZLDS;
//        this.MGSL = MGSL;
//        this.BAYSL = BAYSL;
//        this.XLBARS = XLBARS;
//        this.BAPBQK = BAPBQK;
//        this.SFPBLDZ = SFPBLDZ;
//        this.SFYZGL = SFYZGL;
//        this.SFAZFDM = SFAZFDM;
//        this.SXSFBJS = SXSFBJS;
//        this.PSGSFAZPCHTMHY = PSGSFAZPCHTMHY;
//        this.FWSFYXFQC = FWSFYXFQC;
//        this.SFYMHQ = SFYMHQ;
//        this.SFYYJZMD = SFYYJZMD;
//        this.XFSZSFWH = XFSZSFWH;
//        this.SFAZSPMJ = SFAZSPMJ;
//        this.GGQYTTSL = GGQYTTSL;
//        this.GQTTSL = GQTTSL;
//        this.PTTTSL = PTTTSL;
//        this.ZWWQSFAZTT = ZWWQSFAZTT;
//        this.SFAZYPCSLWDYJBJ = SFAZYPCSLWDYJBJ;
//        this.SFAZDJSPMJ = SFAZDJSPMJ;
//        this.SFPBLDFWZ = SFPBLDFWZ;
//        this.MJSFSXSK = MJSFSXSK;
//        this.LXZLBCSJ = LXZLBCSJ;
//    }
//
//    //工厂宿舍采集
//    public HouseCheckModle(int ID, int TYPE, int SFYGLY, int BAZSQK, int SSGLQK, int SFAZFDM, int SXSFBJS, int PSGSFAZPCHTMHY, int FWSFYXFQC, int SFYMHQ, int SFYYJZMD, int XFSZSFWH, int SFAZSPMJ, int GGQYTTSL, int GQTTSL, int PTTTSL, int SPSHSL, int SFAZYPCSLWDYJBJ, int ZWWQSFAZTT) {
//        this.ID = ID;
//        this.TYPE = TYPE;
//        this.SFYGLY = SFYGLY;
//        this.BAZSQK = BAZSQK;
//        this.SSGLQK = SSGLQK;
//        this.SFAZFDM = SFAZFDM;
//        this.SXSFBJS = SXSFBJS;
//        this.PSGSFAZPCHTMHY = PSGSFAZPCHTMHY;
//        this.FWSFYXFQC = FWSFYXFQC;
//        this.SFYMHQ = SFYMHQ;
//        this.SFYYJZMD = SFYYJZMD;
//        this.XFSZSFWH = XFSZSFWH;
//        this.SFAZSPMJ = SFAZSPMJ;
//        this.GGQYTTSL = GGQYTTSL;
//        this.GQTTSL = GQTTSL;
//        this.PTTTSL = PTTTSL;
//        this.SPSHSL = SPSHSL;
//        this.SFAZYPCSLWDYJBJ = SFAZYPCSLWDYJBJ;
//        this.ZWWQSFAZTT = ZWWQSFAZTT;
//    }
//
//    public HouseCheckModle(int ID, int TYPE, int SFKTJZDJZH, int SFYWYGLGS, int SFYGLY, int SFPBLDZ, int SFYZGL, int SFPBLDFWZ, int SFAZFDM, int SXSFBJS, int PSGSFAZPCHTMHY, int FWSFYXFQC, int SFYMHQ, int SFYYJZMD, int XFSZSFWH, int SFAZDJSPMJ, int SFAZYPCSLWDYJBJ) {
//        this.ID = ID;
//        this.TYPE = TYPE;
//        this.SFKTJZDJZH = SFKTJZDJZH;
//        this.SFYWYGLGS = SFYWYGLGS;
//        this.SFYGLY = SFYGLY;
//        this.SFPBLDZ = SFPBLDZ;
//        this.SFYZGL = SFYZGL;
//        this.SFPBLDFWZ = SFPBLDFWZ;
//        this.SFAZFDM = SFAZFDM;
//        this.SXSFBJS = SXSFBJS;
//        this.PSGSFAZPCHTMHY = PSGSFAZPCHTMHY;
//        this.FWSFYXFQC = FWSFYXFQC;
//        this.SFYMHQ = SFYMHQ;
//        this.SFYYJZMD = SFYYJZMD;
//        this.XFSZSFWH = XFSZSFWH;
//        this.SFAZDJSPMJ = SFAZDJSPMJ;
//        this.SFAZYPCSLWDYJBJ = SFAZYPCSLWDYJBJ;
//    }
//
//    public HouseCheckModle(String RFIMG, int ID, int TYPE, int SFKTJZDJZH, String JFIMG, String XFIMG, String WFIMG) {
//        this.RFIMG = RFIMG;
//        this.ID = ID;
//        this.TYPE = TYPE;
//        this.SFKTJZDJZH = SFKTJZDJZH;
//        this.JFIMG = JFIMG;
//        this.XFIMG = XFIMG;
//        this.WFIMG = WFIMG;
//    }


}
