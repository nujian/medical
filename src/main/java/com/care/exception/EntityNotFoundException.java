package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/22.
 */
public class EntityNotFoundException extends CareException {

    private static final String memo = "对象不存在";

    public EntityNotFoundException() {
        code = 404;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }

}
