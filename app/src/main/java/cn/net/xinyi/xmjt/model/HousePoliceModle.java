package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by hao.zhou on 2016/9/16.
 */
public class HousePoliceModle implements Serializable {
    private String ID;//楼栋ID
    private String LDID;//楼栋ID
    private String FJH;//房间号

    private int SFTZBADZ;//是否停租办案地址
    private int SFJZZBADZ;//是否居住证办案地址
    private String PSFTTP;//拍摄封条图片

    private int SFTZZDL;//是否停租整栋楼
    private int SFTZTJ;//是否停租套间
    private String TZTJFH;//停租套间房号

    private int SFDRYCF;//是否对人员处罚
    private int SFDRYZJZZF;//是否对人员做居住执法
    private String CFRYLX;//处罚人员类型（01:租客,02:业主03:实际代管人）
    private String XM;//姓名
    private String SFZH;//身份证号
    private String CFXX;//处罚信息（01：罚款，02：拘留）
    private int SFJF;//是否解封
    private String TZRQ;//停租日期
    private String JFRQ;//解封日期
    private String TZQX;//停租期限
    private String PARENT_ID;//处罚人员的时候必填项，用来确定因为哪个楼栋哪间房的执法而受到的处罚
    private int CZWXXCJ_ID;//出租屋采集ID

    private String IV_RF;//人防
    private String  IV_JF;//技防
    private String  IV_WF;//物防
    private String  IV_XF;//消防

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIV_RF() {
        return IV_RF;
    }

    public void setIV_RF(String IV_RF) {
        this.IV_RF = IV_RF;
    }

    public String getIV_JF() {
        return IV_JF;
    }

    public void setIV_JF(String IV_JF) {
        this.IV_JF = IV_JF;
    }

    public String getIV_WF() {
        return IV_WF;
    }

    public void setIV_WF(String IV_WF) {
        this.IV_WF = IV_WF;
    }

    public String getIV_XF() {
        return IV_XF;
    }

    public void setIV_XF(String IV_XF) {
        this.IV_XF = IV_XF;
    }

    public String getLDID() {
        return LDID;
    }

    public void setLDID(String LDID) {
        this.LDID = LDID;
    }

    public String getFJH() {
        return FJH;
    }

    public void setFJH(String FJH) {
        this.FJH = FJH;
    }

    public int getSFTZBADZ() {
        return SFTZBADZ;
    }

    public void setSFTZBADZ(int SFTZBADZ) {
        this.SFTZBADZ = SFTZBADZ;
    }

    public int getSFJZZBADZ() {
        return SFJZZBADZ;
    }

    public void setSFJZZBADZ(int SFJZZBADZ) {
        this.SFJZZBADZ = SFJZZBADZ;
    }

    public String getPSFTTP() {
        return PSFTTP;
    }

    public void setPSFTTP(String PSFTTP) {
        this.PSFTTP = PSFTTP;
    }

    public int getSFTZZDL() {
        return SFTZZDL;
    }

    public void setSFTZZDL(int SFTZZDL) {
        this.SFTZZDL = SFTZZDL;
    }

    public int getSFTZTJ() {
        return SFTZTJ;
    }

    public void setSFTZTJ(int SFTZTJ) {
        this.SFTZTJ = SFTZTJ;
    }

    public String getTZTJFH() {
        return TZTJFH;
    }

    public void setTZTJFH(String TZTJFH) {
        this.TZTJFH = TZTJFH;
    }

    public int getSFDRYCF() {
        return SFDRYCF;
    }

    public void setSFDRYCF(int SFDRYCF) {
        this.SFDRYCF = SFDRYCF;
    }

    public int getSFDRYZJZZF() {
        return SFDRYZJZZF;
    }

    public void setSFDRYZJZZF(int SFDRYZJZZF) {
        this.SFDRYZJZZF = SFDRYZJZZF;
    }

    public String getCFRYLX() {
        return CFRYLX;
    }

    public void setCFRYLX(String CFRYLX) {
        this.CFRYLX = CFRYLX;
    }

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getSFZH() {
        return SFZH;
    }

    public void setSFZH(String SFZH) {
        this.SFZH = SFZH;
    }

    public String getCFXX() {
        return CFXX;
    }

    public void setCFXX(String CFXX) {
        this.CFXX = CFXX;
    }

    public int getSFJF() {
        return SFJF;
    }

    public void setSFJF(int SFJF) {
        this.SFJF = SFJF;
    }

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public String getJFRQ() {
        return JFRQ;
    }

    public void setJFRQ(String JFRQ) {
        this.JFRQ = JFRQ;
    }

    public String getTZQX() {
        return TZQX;
    }

    public void setTZQX(String TZQX) {
        this.TZQX = TZQX;
    }

    public String getPARENT_ID() {
        return PARENT_ID;
    }

    public void setPARENT_ID(String PARENT_ID) {
        this.PARENT_ID = PARENT_ID;
    }

    public int getCZWXXCJ_ID() {
        return CZWXXCJ_ID;
    }

    public void setCZWXXCJ_ID(int CZWXXCJ_ID) {
        this.CZWXXCJ_ID = CZWXXCJ_ID;
    }



    //  处罚对象
    public static String cfdx_yz="业主";
    public static String cfdx_dgr="实际代管人";
    public static String cfdx_zk="租客";
    public static String[] sCJDX=new String[]{cfdx_yz,cfdx_dgr,cfdx_zk};
    //  处罚信息
    public static String cfxx_fk="罚款";
    public static String cfxx_jl="拘留";
    public static String[] sCFXX=new String[]{cfxx_fk,cfxx_jl};

    //类别
    public static String getCFDXType(String s) {
        String text = null;
        if (s.equals("01")){
            text=cfdx_yz;
        }else if (s .equals("02")){
            text=cfdx_dgr;
        }else if (s .equals("03")){
            text=cfdx_zk;
        }
        return text;
    }

    public static String getCFDXTypeNum(String s) {
        String snum =null;
        if (s.equals(cfdx_yz)){
            snum="01";
        }else if (s.equals(cfdx_dgr)){
            snum="02";
        }else if (s.equals(cfdx_zk)){
            snum="03";
        }
        return snum;
    }

    //类别
    public static String getCFXXType(String s) {
        String text = null;
        if (s.equals("01")){
            text=cfxx_fk;
        }else if (s .equals("02")){
            text=cfxx_jl;
        }
        return text;
    }

    public static String getCFXXTypeNum(String s) {
        String snum =null;
        if (s.equals(cfxx_fk)){
            snum="01";
        }else if (s.equals(cfxx_jl)){
            snum="02";
        }
        return snum;
    }



}
