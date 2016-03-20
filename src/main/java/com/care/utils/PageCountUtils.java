package com.care.utils;

/**
 * Created by nujian on 16/2/23.
 */
public class PageCountUtils {

    public static Integer processPage(Integer page){
        if(page == null){
            page = 1;
        }
        return page;
    }
    public static Integer processCount(Integer count){
        if(count == null){
            count = 10;
        }
        return count;
    }
}
