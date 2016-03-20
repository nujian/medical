package com.care.controller.uiModels;

import com.care.controller.uiModels.base.BaseVoModel;
import com.care.domain.User;
import com.care.domain.UserAddress;

/**
 * Created by nujian on 16/2/17.
 */
public class IndexVo extends BaseVoModel{

    private User user;

    private UserAddress defaultAddress;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAddress getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(UserAddress defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
