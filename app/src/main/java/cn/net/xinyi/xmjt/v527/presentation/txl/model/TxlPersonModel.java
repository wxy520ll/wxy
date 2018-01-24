package cn.net.xinyi.xmjt.v527.presentation.txl.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.io.Serializable;

import cn.net.xinyi.xmjt.utils.StringUtils;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class TxlPersonModel implements MultiItemEntity, Serializable, Comparable {
    /**
     * ORGANCODE : 440307000000
     * ACCOUNTTYPE : 辅警
     * USERNAME : 18128835109
     * SFAPPGLY : 0
     * NAME : 刘文斌
     * ORGANNAME : 龙岗分局
     */
    /**
     * "USERNAME": "13502819168",
     * "ORGANCODE": "440307000000",
     * "POLICENO": "064717",
     * "ZDBM": "1",
     * "ID": 6334,
     * "NAME": "罗明",
     * "ORGANNAME": "龙岗分局",
     * "SFAPPGLY": "1",
     * "ACCOUNTTYPE": "民警",
     * "ISDELETE": 0
     */
    private String ORGANCODE;
    private String ACCOUNTTYPE;
    private String USERNAME;
    private String SFAPPGLY;
    private String NAME;
    private String ORGANNAME;
    private String POLICENO;

    @Override
    public String toString() {
        return NAME + "-" + USERNAME;
    }

    public String getPOLICENO() {
        return POLICENO;
    }

    public void setPOLICENO(String POLICENO) {
        this.POLICENO = POLICENO;
    }

    public String getORGANCODE() {
        return ORGANCODE;
    }

    public void setORGANCODE(String ORGANCODE) {
        this.ORGANCODE = ORGANCODE;
    }

    public String getACCOUNTTYPE() {
        return ACCOUNTTYPE;
    }

    public void setACCOUNTTYPE(String ACCOUNTTYPE) {
        this.ACCOUNTTYPE = ACCOUNTTYPE;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getSFAPPGLY() {
        return SFAPPGLY;
    }

    public void setSFAPPGLY(String SFAPPGLY) {
        this.SFAPPGLY = SFAPPGLY;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getORGANNAME() {
        return ORGANNAME;
    }

    public void setORGANNAME(String ORGANNAME) {
        this.ORGANNAME = ORGANNAME;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    private String getPinYinFirst() {
        String shortPinyin = null;
        try {
            if (StringUtils.isEmpty(NAME)) return "A";
            shortPinyin = PinyinHelper.getShortPinyin(NAME);
            if (StringUtils.isEmpty(shortPinyin)) shortPinyin = "A";

        } catch (Exception e) {
            e.printStackTrace();
            return "A";
        }
        return shortPinyin.substring(0, 1).toUpperCase();
    }

    @Override
    public int compareTo(Object another) {
        TxlPersonModel user0 = (TxlPersonModel) another;
        return this.getPinYinFirst().compareTo(user0.getPinYinFirst());
    }

}
