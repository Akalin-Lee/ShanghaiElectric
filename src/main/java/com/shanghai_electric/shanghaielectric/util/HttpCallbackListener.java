package com.shanghai_electric.shanghaielectric.util;

/**
 * Created by Akalin on 2017/2/28.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
