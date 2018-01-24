package cn.net.xinyi.xmjt.model;

// FIXME generate failure  field _$Data268
// FIXME generate failure  field _$Data241

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajun.wang on 2017/10/10.
 *
 * 脚镣设备信息
 */

public class DevicesModel implements Serializable{


    /**
     * error : 0
     * data : [{"id":4,"description":"","simId":"13401944389","type":2,"devId":"32867d","unitId":1},{"id":3,"description":"","simId":"15951962423","type":2,"devId":"3282ce","unitId":1},{"id":2,"description":"","simId":"13404148936","type":2,"devId":"3285fa","unitId":1},{"id":1,"description":"","simId":"14751760231","type":1,"devId":"322013","unitId":1}]
     * errorMsg : 操作成功
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

    public static class DataBean {
        /**
         * id : 4
         * description :
         * simId : 13401944389
         * type : 2
         * devId : 32867d
         * unitId : 1
         */

        private int id;
        private String description;
        private String simId;
        private int type;
        private String devId;
        private int unitId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSimId() {
            return simId;
        }

        public void setSimId(String simId) {
            this.simId = simId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public int getUnitId() {
            return unitId;
        }

        public void setUnitId(int unitId) {
            this.unitId = unitId;
        }
    }
}
