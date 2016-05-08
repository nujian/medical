package com.care.exception;

import com.care.Constants;
import com.care.exception.base.CareException;

/**
 * Created by nujian on 16/5/8.
 */
public class NurseStatusErrorException extends CareException{

    private static final String memo = "该护士已被停单";

    public NurseStatusErrorException() {
        code = 501;
        Constants.RESPONSE_CODE_THREAD_LOCAL.set(code);
        this.setMemo(memo);
    }
}
