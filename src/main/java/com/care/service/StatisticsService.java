package com.care.service;

import com.care.controller.web.Vo.StatisticsData;

import java.util.List;

/**
 * Created by nujian on 16/5/8.
 */
public interface StatisticsService {

    List<StatisticsData> getStatisticsDataByType(Integer type);
}
