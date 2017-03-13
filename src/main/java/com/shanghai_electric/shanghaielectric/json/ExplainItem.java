package com.shanghai_electric.shanghaielectric.json;

/**
 * Created by Akalin on 2017/3/12.
 */

public class ExplainItem{
    int number;
    String trouble_reason;
    String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTrouble_reason() {
        return trouble_reason;
    }

    public void setTrouble_reason(String trouble_reason) {
        this.trouble_reason = trouble_reason;
    }

}
