package cn.net.xinyi.xmjt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajun.wang on 2017/10/13.
 */

public class UnitModel implements Serializable{


    /**
     * error : 0
     * errorMsg : caozu
     * data : [{"id":"1","name":"","province":"","city":"","district":"","parentId":""}]
     */

    private String error;
    private String errorMsg;
    private List<DataBean> data=new ArrayList<>();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * name :
         * province :
         * city :
         * district :
         * parentId :
         */

        private String id;
        private String name;
        private String province;
        private String city;
        private String district;
        private String parentId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }
}
