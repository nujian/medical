package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/23.
 */
public class BadWalletPasswordException extends CareException {

    private static final String memo = "钱包密码错误";

    public BadWalletPasswordException() {
        code = 414;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }

}
