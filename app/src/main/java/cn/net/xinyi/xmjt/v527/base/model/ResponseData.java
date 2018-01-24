package cn.net.xinyi.xmjt.v527.base.model;

/**
 * Created by Fracesuit on 2017/12/27.
 */

public class ResponseData<T> {
    T result;
    String message;
    String status;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
