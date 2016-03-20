package com.care.service.impl;


import com.care.controller.result.ResultBean;
import com.care.domain.base.BaseModel;
import com.care.service.JSONdocService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nujian
 * Date: 14-7-3
 * Time: 下午9:21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class JSONdocServiceImpl implements JSONdocService {
    @Override
    public String generateSample(Class entityClass, boolean multiple, String[] includes, String[] excludes) {
        Object retval = null;
        List<BaseModel> list = BaseModel.findBaseModelEntries(0, 3, entityClass);
        if (list != null && list.size() > 0) {
            if (multiple) {
                retval = list;
            } else {
                retval = list.get(0);
            }
            return ResultBean.wrap(retval, includes, excludes).toJson();
        }
        return null;
    }
}
