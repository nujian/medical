package com.care.service;

import com.care.domain.Price;
import com.care.domain.User;
import com.care.exception.EntityNotFoundException;
import com.care.exception.NoPermissionsException;

import java.math.BigDecimal;

/**
 * Created by nujian on 16/2/24.
 */
public interface PriceService {

    Price add(User admin, BigDecimal cost, String memo) throws NoPermissionsException;

    Price edit(User admin, Price sourse, Integer targetPriceId, String memo) throws NoPermissionsException, EntityNotFoundException;
}
