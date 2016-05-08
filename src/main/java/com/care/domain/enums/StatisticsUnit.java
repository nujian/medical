package com.care.domain.enums;

import com.care.utils.DateUtils;

import java.util.Date;

/**
 * Created by nujian on 16/5/8.
 */
public enum StatisticsUnit {

    TODAY,WEEK,MONTH,ALL;

    public Date getQueryDate(){
        final Integer weekdays = 7;
        final Integer monthdays = 31;
        final Integer year = 365;
        Date date = new Date();
        switch (this){
            case TODAY:
                break;
            case WEEK:
                date = DateUtils.preDate(date,weekdays);
                break;
            case MONTH:
                date = DateUtils.preDate(date,monthdays);
                break;
            case ALL:
                date = DateUtils.preDate(date,year);
                break;
            default:
                break;
        }
        return date;
    }
}
