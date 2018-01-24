package cn.net.xinyi.xmjt.v527.presentation.txl.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class TxlTypeModel implements MultiItemEntity, Comparable {
    /**
     * ORGCODE : 11
     * ORGNAME : 管道公司
     * PID : 4
     * ID : 153
     * CREATETIME : 2017-01-16 12:02:52.000
     */

    private String ACCOUNTTYPE;
    private List<TxlPersonModel> person = new ArrayList<>();
    int px;//排序

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public List<TxlPersonModel> getPerson() {
        return person;
    }

    public void setPerson(List<TxlPersonModel> person) {
        this.person = person;
    }

    public String getACCOUNTTYPE() {
        return ACCOUNTTYPE;
    }

    public void setACCOUNTTYPE(String ACCOUNTTYPE) {
        this.ACCOUNTTYPE = ACCOUNTTYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TxlTypeModel that = (TxlTypeModel) o;

        return ACCOUNTTYPE.equals(that.ACCOUNTTYPE);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 3;
    }

    @Override
    public int compareTo(Object another) {
        TxlTypeModel user0 = (TxlTypeModel) another;
        return this.getPx() - user0.getPx();
    }
}
