package com.care.service.impl;

import com.care.Constants;
import com.care.domain.User;
import com.care.domain.UserLoginLog;
import com.care.exception.BadTokenException;
import com.care.exception.base.CareException;
import com.care.service.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nujian on 16/2/22.
 */
@Service
public class SecurityServiceImpl implements SecurityService {
    @Override
    public User getCurrentLoginUser(HttpServletRequest request) throws CareException {
        UserLoginLog log = null;
        String token = request.getHeader(Constants.USER_LOGIN_TOKEN);
        if(StringUtils.isBlank(token) || ((log = UserLoginLog.getActiveLogByToken(token)) == null)){
            throw new BadTokenException();
        }
        log = UserLoginLog.getActiveLogByToken(token);
        return log.getUser();
    }
}
