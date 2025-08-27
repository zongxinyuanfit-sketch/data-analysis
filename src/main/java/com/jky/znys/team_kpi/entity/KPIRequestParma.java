package com.jky.znys.team_kpi.entity;

public class KPIRequestParma {
    private int id;
    private String name;
    private String password;
    private String loginurl;
    private String dataurl;
    private String cookies;
    private String referer;
    private String useragent;
    private String parmas;
    private String method;
    private String wxtoken;
    private String fixdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginurl() {
        return loginurl;
    }

    public void setLoginurl(String loginurl) {
        this.loginurl = loginurl;
    }

    public String getDataurl() {
        return dataurl;
    }

    public void setDataurl(String dataurl) {
        this.dataurl = dataurl;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getParmas() {
        return parmas;
    }

    public void setParmas(String parmas) {
        this.parmas = parmas;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getWxtoken() {
        return wxtoken;
    }

    public void setWxtoken(String wxtoken) {
        this.wxtoken = wxtoken;
    }

    public String getFixdate() {
        return fixdate;
    }

    public void setFixdate(String fixdate) {
        this.fixdate = fixdate;
    }
}
