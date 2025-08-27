package com.jky.znys.team_kpi.entity;

public class KPITotalMoneyMonth {
    private int id;
    private String radix;
    private String total;
    private String average;
    private String percent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRadix() {
        return radix;
    }

    public void setRadix(String radix) {
        this.radix = radix;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
