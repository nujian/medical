package com.care.customlization.flexjson;

import flexjson.transformer.AbstractTransformer;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nujian on 2015/5/22.
 */
public class DateFormatTransformer extends AbstractTransformer {
    @Override
    public void transform(Object object) {
        if (StringUtils.isNotBlank((String) object)) {
            Calendar c = Calendar.getInstance();
            c.setTime((Date) object);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            getContext().write(String.valueOf(format.format(c.getTime())));
        } else {
            getContext().writeQuoted("");
        }
    }
}
