package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/29.
 */
public class LackOfBalanceException extends CareException {

    private static final String memo = "余额不足";

    public LackOfBalanceException() {
        code = 415;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }
}
