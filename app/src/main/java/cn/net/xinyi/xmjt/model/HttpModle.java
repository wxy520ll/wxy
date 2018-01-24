package cn.net.xinyi.xmjt.model;

/**
 * Created by zhouhao on 2017/3/2.
 */

public class HttpModle {
    private String message;
    private String status;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
