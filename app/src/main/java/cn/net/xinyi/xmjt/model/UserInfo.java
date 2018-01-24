package cn.net.xinyi.xmjt.model;

import java.io.Serializable;

/**
 * Created by mazhongwang on 15/10/11.
 */
public class UserInfo implements Serializable {
    private int  id;
    private int  isregester;//是否注册权限  0 有 1没有  （安全管理模块添加 去掉了注册短信限制 但是没有添加注册权限只能查看安全管理模块）
    private String name;//名字
    private String username;//用户名 和cellphone一致
    private String cellphone;
    private String fjbm;//分局编码
    private String organcode;//派出所编码
    private String jwscode;//警务室编码
    private String accounttype;//身份标识
    private String policeno;//警号
    private String password;//密码
    private String createtime;
    private String fj;///分局
    private String pcs;//派出所
    private String jws;//警务室
    private String ssjd;//所属街道
    private String sssq;//所属 社区
    private String sswg;//所属网格
    private String isremember;//是否记住密码
    private String sfzh;//身份证号
    private String companyname;//第三方厂家注册粗腰填写身份证号码
    private String sfzzp;//身份证照片
    private String txzp;//头像
    private String roleNmae;//权限的名字

    public String getRoleNmae() {
        return roleNmae;
    }

    public void setRoleNmae(String roleNmae) {
        this.roleNmae = roleNmae;
    }

    public String getTxzp() {
        return txzp;
    }

    public void setTxzp(String txzp) {
        this.txzp = txzp;
    }

    public String getFjbm() {
        return fjbm;
    }

    public void setFjbm(String fjbm) {
        this.fjbm = fjbm;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public int getIsregester() {
        return isregester;
    }

    public void setIsregester(int isregester) {
        this.isregester = isregester;
    }

    public String getSfzzp() {
        return sfzzp;
    }

    public void setSfzzp(String sfzzp) {
        this.sfzzp = sfzzp;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getSsjd() {
        return ssjd;
    }

    public void setSsjd(String ssjd) {
        this.ssjd = ssjd;
    }

    public String getSssq() {
        return sssq;
    }

    public void setSssq(String sssq) {
        this.sssq = sssq;
    }

    public String getSswg() {
        return sswg;
    }

    public void setSswg(String sswg) {
        this.sswg = sswg;
    }

    public String getIsremember() {
        return isremember;
    }

    public void setIsremember(String isremember) {
        this.isremember = isremember;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getOrgancode() {
        return organcode;
    }

    public void setOrgancode(String organcode) {
        this.organcode = organcode;
    }

    public String getJwscode() {
        return jwscode;
    }

    public void setJwscode(String jwscode) {
        this.jwscode = jwscode;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getPoliceno() {
        return policeno;
    }

    public void setPoliceno(String policeno) {
        this.policeno = policeno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}