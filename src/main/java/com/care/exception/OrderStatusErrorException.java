package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/23.
 */
public class OrderStatusErrorException extends CareException {

    private static final String memo = "订单状态不匹配";

    public OrderStatusErrorException() {
        code = 412;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }
}
