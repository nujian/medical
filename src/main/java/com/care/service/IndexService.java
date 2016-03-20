package com.care.service;

import com.care.controller.uiModels.IndexVo;
import com.care.domain.User;

/**
 * Created by nujian on 16/3/3.
 */
public interface IndexService {

    IndexVo wrapIndex(User current);
}
