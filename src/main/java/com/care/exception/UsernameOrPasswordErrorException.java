package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/18.
 */
public class UsernameOrPasswordErrorException extends CareException{

    private static final String memo = "用户名或密码错误";

    public UsernameOrPasswordErrorException() {
        code = 401;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }

}
