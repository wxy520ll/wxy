package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by mazhongwang on 15/4/14.
 */
public class OrgansModle implements Serializable {
    private String id ;
    private String pcs;
    private String jws;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }

    public String getJws() {
        return jws;
    }

    public void setJws(String jws) {
        this.jws = jws;
    }
}