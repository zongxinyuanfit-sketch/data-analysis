package com.jky.znys.team_kpi.entity;

public class KPIUser{
    private int uid;
    private String name;
    private String radix;
    private int describeid;
    private int statuesid;
    private int markid;
    private String uremark;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRadix() {
        return radix;
    }

    public void setRadix(String radix) {
        this.radix = radix;
    }

    public int getDescribeid() {
        return describeid;
    }

    public void setDescribeid(int describeid) {
        this.describeid = describeid;
    }

    public int getStatuesid() {
        return statuesid;
    }

    public void setStatuesid(int statuesid) {
        this.statuesid = statuesid;
    }

    public int getMarkid() {
        return markid;
    }

    public void setMarkid(int markid) {
        this.markid = markid;
    }

    public String getUremark() {
        return uremark;
    }

    public void setUremark(String uremark) {
        this.uremark = uremark;
    }

}
