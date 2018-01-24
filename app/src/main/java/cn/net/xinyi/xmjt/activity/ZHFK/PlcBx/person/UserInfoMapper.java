package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.person;

/**
 * Created by AppleRen on 2017/3/24.
 */

public class UserInfoMapper implements Comparable {
    private String name;
    private String cellphone;
    private String firstNamePinYin;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getFirstNamePinYin() {
        return firstNamePinYin;
    }

    public void setFirstNamePinYin(String firstNamePinYin) {
        this.firstNamePinYin = firstNamePinYin;
    }


    @Override
    public int compareTo(Object another) {
        UserInfoMapper user0 = (UserInfoMapper) another;
        return this.getFirstNamePinYin().compareTo(user0.getFirstNamePinYin());
    }
}
