package com.care.service.impl;

import com.care.domain.Price;
import com.care.domain.PriceChangeLog;
import com.care.domain.User;
import com.care.domain.enums.PriceChangeType;
import com.care.domain.enums.UserType;
import com.care.exception.EntityNotFoundException;
import com.care.exception.NoPermissionsException;
import com.care.service.PriceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by nujian on 16/2/24.
 */
@Service
public class PriceServiceImpl implements PriceService {

    public Price find(Integer priceId){
        return Price.entityManager().find(Price.class,priceId);
    }

    @Override
    @Transactional
    public Price add(User admin, BigDecimal cost, String memo) throws NoPermissionsException {
        if(!admin.getUserType().equals(UserType.ADMIN)){
            throw new NoPermissionsException();
        }
        Price price = new Price();
        price.setMemo(StringUtils.isBlank(memo)?"体检价格(人／次)":memo);
        price.setPrice(cost);
        price.setAdmin(admin);
        price.persist();
        logPirceChange(price, admin, "价格初始化","无","初始化成功");
        return price;
    }

    private void logPirceChange(Price price, User admin, String memo,String before,String after) {
        PriceChangeLog log = new PriceChangeLog();
        log.setPrice(price);
        log.setAdmin(admin);
        log.setChangBefore(before);
        log.setChangAfter(after);
        log.setType(PriceChangeType.INIT);
        log.setMemo(memo);
        log.persist();
    }

    @Override
    @Transactional
    public Price edit(User admin,Price sourse,Integer targetPriceId,String memo) throws NoPermissionsException, EntityNotFoundException {
        if(!admin.getUserType().equals(UserType.ADMIN)){
            throw new NoPermissionsException();
        }
        Price target = find(targetPriceId);
        if(target == null){
            throw new EntityNotFoundException();
        }
        boolean changed = false;
        String sourseMemo = sourse.getMemo();
        String beforeMemo = target.getMemo();
        if(StringUtils.isNotBlank(sourseMemo) && (StringUtils.equalsIgnoreCase(sourseMemo,target.getMemo()))){
            target.setMemo(sourseMemo);
            changed = true;
            logPirceChange(target,admin,memo,beforeMemo,sourseMemo);
        }
        BigDecimal cost = sourse.getPrice();
        BigDecimal beforCost = target.getPrice();
        if(cost != null){
            target.setPrice(cost);
            changed = true;
            logPirceChange(target, admin, memo, String.valueOf(beforCost), String.valueOf(cost));
        }
        if(changed){
            target.merge();
        }
        return target;
    }
}
