package cn.net.xinyi.xmjt.v527.data.remote.retrofit.entity;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;

import java.util.List;

/**
 * Created by Fracesuit on 2017/12/31.
 */

public class ZajcEntity {
    /**
     * CKDESC_CN : 是，否
     * DEFAULTCHECK_CN : 否
     * HANDLEDESC : 根据《报废汽车回收管理办法》第12条和第22条的规定，由公安机关没收违法所得，并处2000元以上2万元以下的罚款。
     * ITEMCNAME : 是否非法赠与、转让报废汽车
     * LAWDES : 违反本办法第十二条的规定，将报废汽车出售、赠予或者以其他方式转让给非报废汽车回收企业的单位或者个人的，或者自行拆解报废汽车的，由公安机关没收违法所得，并处2000元以上2万元以下的罚款。
     * PROJECTCNAME : 经营行为
     * TITLE : 《报废汽车回收管理办法》第二十二条
     */

    private String CIID;
    private String CKDESC_CN;
    private String CKDESC;
    private String DEFAULTCHECK_CN;
    private String DEFAULTCHECK;
    private String HANDLEDESC;
    private String ITEMCNAME;
    private String LAWDES;
    private String PROJECTCNAME;
    private String TITLE;
    private String RES;
    private String LIST;
    private List<JSONObject> gfList;
    private String test;


    public List<JSONObject> getGfList() {
        return gfList;
    }

    public void setGfList(List<JSONObject> gfList) {
        this.gfList = gfList;
    }

    public String getLIST() {
        return LIST;
    }

    public void setLIST(String LIST) {
        final String replace = LIST.replace("\\", "");
        final List<JSONObject> jsonObjects = JSONObject.parseArray(replace, JSONObject.class);
        setGfList(jsonObjects);
        this.LIST = LIST;
    }

    public String getRES() {
        return RES;
    }

    public void setRES(String RES) {
        this.RES = RES;
    }

    public String getDEFAULTCHECK() {
        if (!StringUtils.isEmpty(RES)) {
            return RES;
        }
        return DEFAULTCHECK;
    }

    public void setDEFAULTCHECK(String DEFAULTCHECK) {
        if (!StringUtils.isEmpty(RES)) {
            this.DEFAULTCHECK = RES;
        } else {
            this.DEFAULTCHECK = DEFAULTCHECK;
        }

    }

    public String getCIID() {
        return CIID;
    }

    public void setCIID(String CIID) {
        this.CIID = CIID;
    }

    public String getCKDESC() {
        return CKDESC;
    }

    public void setCKDESC(String CKDESC) {
        this.CKDESC = CKDESC;
    }

    public String getCKDESC_CN() {
        final String[] names = CKDESC_CN.split("，");
        final String[] codes = CKDESC.split("，");
        final StringBuffer sb = new StringBuffer();
        for (int i = 0, len = names.length; i < len; i++) {
            sb.append(names[i] + "_" + codes[i] + ",");
        }
        sb.append("check_" + getDEFAULTCHECK());
        return sb.toString();
    }

    public void setCKDESC_CN(String CKDESC_CN) {
        this.CKDESC_CN = CKDESC_CN;
    }

    public String getDEFAULTCHECK_CN() {
        return DEFAULTCHECK_CN;
    }

    public void setDEFAULTCHECK_CN(String DEFAULTCHECK_CN) {
        this.DEFAULTCHECK_CN = DEFAULTCHECK_CN;
    }

    public String getHANDLEDESC() {
        return HANDLEDESC;
    }

    public void setHANDLEDESC(String HANDLEDESC) {
        this.HANDLEDESC = HANDLEDESC;
    }

    public String getITEMCNAME() {
        return ITEMCNAME;
    }

    public void setITEMCNAME(String ITEMCNAME) {
        this.ITEMCNAME = ITEMCNAME;
    }

    public String getLAWDES() {
        return LAWDES;
    }

    public void setLAWDES(String LAWDES) {
        this.LAWDES = LAWDES;
    }

    public String getPROJECTCNAME() {
        return PROJECTCNAME;
    }

    public void setPROJECTCNAME(String PROJECTCNAME) {
        this.PROJECTCNAME = PROJECTCNAME;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }
}
