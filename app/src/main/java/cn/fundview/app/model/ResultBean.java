package cn.fundview.app.model;

import java.io.Serializable;

/**
 * 返回值
 * result 1 成功  2 失败
 * message 成功/失败原因
 * uid accountName
 */
public class ResultBean implements Serializable {

    private static final long serialVersionUID = -8990007856943348516L;
    private String result;
    private String message;
    private int status;//状态值

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
