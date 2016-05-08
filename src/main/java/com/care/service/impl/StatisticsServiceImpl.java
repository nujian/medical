package com.care.service.impl;

import com.care.controller.web.Vo.StatisticsData;
import com.care.domain.enums.StatisticsUnit;
import com.care.service.OrderService;
import com.care.service.StatisticsService;
import com.care.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by nujian on 16/5/8.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Override
    public List<StatisticsData> getStatisticsDataByType(Integer type) {
        List<StatisticsData> statisticsDatas = new ArrayList<StatisticsData>();
        switch (type){
            case 1:
                statisticsDatas.addAll(getUserStatisticsDatas());
                break;
            case 2:
                statisticsDatas.addAll(getOrderStatisticsDatas());
                break;
            case 3:
                statisticsDatas.addAll(getCashStatisticsDatas());
                break;
            default:
                break;
        }
        return statisticsDatas;
    }

    public List<StatisticsData> getUserStatisticsDatas() {
        List<StatisticsData> userStatisticsDatas = new ArrayList<StatisticsData>();
        userStatisticsDatas.add(new StatisticsData("今日新增用户", "统计今日新增用户", userService.getStatisticsNum4UserByUnit(StatisticsUnit.TODAY)));
        userStatisticsDatas.add(new StatisticsData("周内新增用户", "统计一周内新增用户", userService.getStatisticsNum4UserByUnit(StatisticsUnit.WEEK)));
        userStatisticsDatas.add(new StatisticsData("月内新增用户", "统计一月内新增用户", userService.getStatisticsNum4UserByUnit(StatisticsUnit.MONTH)));
        userStatisticsDatas.add(new StatisticsData("年内新增用户", "统计一年内新增用户", userService.getStatisticsNum4UserByUnit(StatisticsUnit.ALL)));
        return userStatisticsDatas;
    }

    public List<StatisticsData> getOrderStatisticsDatas() {
        List<StatisticsData> orderStatisticsDatas = new ArrayList<StatisticsData>();
        orderStatisticsDatas.add(new StatisticsData("今日新增订单", "统计今日新增订单", orderService.getStatisticsNum4UserByUnit(StatisticsUnit.TODAY)));
        orderStatisticsDatas.add(new StatisticsData("周内新增订单", "统计一周内新增订单", orderService.getStatisticsNum4UserByUnit(StatisticsUnit.WEEK)));
        orderStatisticsDatas.add(new StatisticsData("月内新增订单", "统计一月内新增订单", orderService.getStatisticsNum4UserByUnit(StatisticsUnit.MONTH)));
        orderStatisticsDatas.add(new StatisticsData("年内新增订单", "统计一年内新增订单", orderService.getStatisticsNum4UserByUnit(StatisticsUnit.ALL)));
        return orderStatisticsDatas;
    }

    public List<StatisticsData> getCashStatisticsDatas() {
        List<StatisticsData> cashStatisticsDatas = new ArrayList<StatisticsData>();
        cashStatisticsDatas.add(new StatisticsData("今日提现订单", "统计今日提现订单", userService.getStatisticsNum4CashByUnit(StatisticsUnit.TODAY)));
        cashStatisticsDatas.add(new StatisticsData("周内提现订单", "统计一周内提现订单", userService.getStatisticsNum4CashByUnit(StatisticsUnit.WEEK)));
        cashStatisticsDatas.add(new StatisticsData("月内提现订单", "统计一月内提现订单", userService.getStatisticsNum4CashByUnit(StatisticsUnit.MONTH)));
        cashStatisticsDatas.add(new StatisticsData("年内提现订单", "统计一年内提现订单", userService.getStatisticsNum4CashByUnit(StatisticsUnit.ALL)));
        return cashStatisticsDatas;
    }

}
