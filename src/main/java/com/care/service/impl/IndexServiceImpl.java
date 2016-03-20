package com.care.service.impl;

import com.care.controller.uiModels.IndexVo;
import com.care.domain.User;
import com.care.domain.UserAddress;
import com.care.service.IndexService;
import org.springframework.stereotype.Service;

/**
 * Created by nujian on 16/3/3.
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Override
    public IndexVo wrapIndex(User current) {
        IndexVo index = new IndexVo();
        index.setUser(current);
        index.setDefaultAddress(UserAddress.getDisplayUserAddress(current));
        return index;
    }
}
