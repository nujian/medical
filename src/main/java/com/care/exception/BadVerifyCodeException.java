package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/23.
 */
public class BadVerifyCodeException extends CareException {

    private static final String memo = "验证码错误";

    public BadVerifyCodeException() {
        code = 409;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }

}
