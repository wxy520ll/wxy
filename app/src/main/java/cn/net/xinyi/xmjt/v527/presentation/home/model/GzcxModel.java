package cn.net.xinyi.xmjt.v527.presentation.home.model;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class GzcxModel {
    private String pm;
    private String name;
    private String mobile;
    private String typeName;
    private String num;

    public GzcxModel(String pm, String name, String mobile, String typeName, String num) {
        this.pm = pm;
        this.name = name;
        this.mobile = mobile;
        this.typeName = typeName;
        this.num = num;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
