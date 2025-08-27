package com.jky.znys.team_kpi.entity;

public class KPIRadix {
    private int id;
    private String radix;
    private String categoryid;
    private String highscore;
    private String udate;

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

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getHighscore() {
        return highscore;
    }

    public void setHighscore(String highscore) {
        this.highscore = highscore;
    }

    public String getDate() {
        return udate;
    }

    public void setDate(String date) {
        this.udate = date;
    }
}
