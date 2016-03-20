package com.care.customlization.flexjson;
import flexjson.transformer.AbstractTransformer;

/**
 * Created by nujian on 2015/5/28.
 */
public class JsonNullValueTransformer extends AbstractTransformer {
    @Override
    public Boolean isInline() {
        return true;
    }

    @Override
    public void transform(Object o) {
        // Do nothing, null objects are not serialized.
    }
}