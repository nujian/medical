package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/22.
 */
public class BadTokenException extends CareException {

    private static final String memo = "token错误或token已经失效";

    public BadTokenException() {
        code = 403;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }

}
