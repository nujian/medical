package com.care.customlization.flexjson;

import flexjson.transformer.AbstractTransformer;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by nujian on 2015/5/28.
 */
public class ApiReturnValueTransformer extends AbstractTransformer {
    @Override
    public void transform(Object object) {
        if (StringUtils.isNotBlank((String) object)) {
            getContext().write((String) object);
        } else {
            getContext().writeQuoted("");
        }
    }
}
