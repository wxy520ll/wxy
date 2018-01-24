package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by mazhongwang on 15/3/27.
 */
public class PoliceContacts implements Serializable{

    private String id;
    //警号
    private String polno;
    //姓名
    private String name;
    //单位
    private String organ;
    //部门
    private String dept;
    //职务
    private String title;
    //移动电话
    private String mobileno;
    //座机
    private String telno;
    //姓名拼音缩写
    private String namespell;
    //单位拼音缩写
    private String organspell;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolno() {
        return polno;
    }

    public void setPolno(String polno) {
        this.polno = polno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getNamespell() {
        return namespell;
    }

    public void setNamespell(String namespell) {
        this.namespell = namespell;
    }

    public String getOrganspell() {
        return organspell;
    }

    public void setOrganspell(String organspell) {
        this.organspell = organspell;
    }
}
