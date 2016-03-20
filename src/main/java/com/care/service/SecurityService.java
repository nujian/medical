package com.care.service;

import com.care.domain.User;
import com.care.exception.base.CareException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nujian on 16/2/22.
 */
public interface SecurityService {

    User getCurrentLoginUser(HttpServletRequest request) throws CareException;

}
