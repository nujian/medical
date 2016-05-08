package com.care.controller.web.Vo;

/**
 * Created by nujian on 16/5/8.
 */
public class StatisticsData {

    private String name;

    private String memo;

    private Integer count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public StatisticsData() {
    }

    public StatisticsData(String name, String memo, Integer count) {
        this.name = name;
        this.memo = memo;
        this.count = count;
    }
}
