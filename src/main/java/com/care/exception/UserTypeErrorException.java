package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/24.
 */
public class UserTypeErrorException extends CareException {
    private static final String memo = "用户类型有误";

    public UserTypeErrorException() {
//        code = 501;
        code = 413;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }
}
