package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/2/24.
 */
public class NoPermissionsException extends CareException {

    private static final String memo = "权限不足";

    public NoPermissionsException() {
        code = 411;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }
}
