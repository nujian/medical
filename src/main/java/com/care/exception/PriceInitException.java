package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/24.
 */
public class PriceInitException extends CareException {

    private static final String memo = "价格初始化异常";

    public PriceInitException() {
        code = 401;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }
}
