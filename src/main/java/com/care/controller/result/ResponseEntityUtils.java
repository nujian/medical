package com.care.controller.result;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by nujian on 15/11/13.
 * Controller 返回数据工具类
 */
public class ResponseEntityUtils {

    /**
     *
     * @param responseContent
     * @return
     */
    public static ResponseEntity<String> wrapResponseEntity(String responseContent){

        return wrapResponseEntity(responseContent,null);
    }

    /**
     *
     * @param responseContent
     * @param headers
     * @return
     */
    public static ResponseEntity<String> wrapResponseEntity(String responseContent,HttpHeaders headers){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=utf-8");
        if(headers != null){
            for(String key:headers.keySet()){
                if(StringUtils.equalsIgnoreCase("Content-Type",key)){
                    continue;
                }
                httpHeaders.add(key, String.valueOf(headers.get(key)));
            }
        }

        return new ResponseEntity<String>(responseContent,httpHeaders, HttpStatus.OK);
    }

}
