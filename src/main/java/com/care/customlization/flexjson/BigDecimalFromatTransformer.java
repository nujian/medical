package com.care.customlization.flexjson;

import flexjson.transformer.AbstractTransformer;

/**
 * Created by nujian on 2015/5/28.
 */
public class BigDecimalFromatTransformer extends AbstractTransformer {
    @Override
    public void transform(Object object) {
        if(object!=null){
            getContext().write(String.valueOf(object));
        }else{
            getContext().write("");
        }
    }
}
