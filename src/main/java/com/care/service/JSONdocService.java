package com.care.service;

/**
 * Created with IntelliJ IDEA.
 * User: ZhangYi
 * Date: 14-7-3
 * Time: 下午9:18
 * To change this template use File | Settings | File Templates.
 */
public interface JSONdocService {

    String generateSample(Class entityClass, boolean multiple, String[] includes, String[] excludes);

}
