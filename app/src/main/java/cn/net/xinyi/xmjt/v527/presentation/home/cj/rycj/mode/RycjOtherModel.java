package cn.net.xinyi.xmjt.v527.presentation.home.cj.rycj.mode;

/**
 * Created by Fracesuit on 2018/1/18.
 */

public class RycjOtherModel implements Cloneable {
    private String person_no;
    private String id_number;
    private String name;
    private String gender;
    private String age;
    private String height;
    private String permanent_addr;
    private String contact_phone;
    private String guardian;
    private String photo_1;
    private String photo_2;
    private String photo_3;
    private String person_type;
    private String feature_comment;
    private String find_time;
    private String addr;
    private String x;
    private String y;
    private String cjsj;
    private String cjyh;
    private String create_time;

    public String getPerson_no() {
        return person_no;
    }

    public void setPerson_no(String person_no) {
        this.person_no = person_no;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPermanent_addr() {
        return permanent_addr;
    }

    public void setPermanent_addr(String permanent_addr) {
        this.permanent_addr = permanent_addr;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getPhoto_1() {
        return photo_1;
    }

    public void setPhoto_1(String photo_1) {
        this.photo_1 = photo_1;
    }

    public String getPhoto_2() {
        return photo_2;
    }

    public void setPhoto_2(String photo_2) {
        this.photo_2 = photo_2;
    }

    public String getPhoto_3() {
        return photo_3;
    }

    public void setPhoto_3(String photo_3) {
        this.photo_3 = photo_3;
    }

    public String getPerson_type() {
        return person_type;
    }

    public void setPerson_type(String person_type) {
        this.person_type = person_type;
    }

    public String getFeature_comment() {
        return feature_comment;
    }

    public void setFeature_comment(String feature_comment) {
        this.feature_comment = feature_comment;
    }

    public String getFind_time() {
        return find_time;
    }

    public void setFind_time(String find_time) {
        this.find_time = find_time;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public String getCjyh() {
        return cjyh;
    }

    public void setCjyh(String cjyh) {
        this.cjyh = cjyh;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public RycjOtherModel clone() {
        try {
            return (RycjOtherModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }
    }
}
