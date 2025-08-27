package com.jky.znys.team_kpi.utils;

public class UrlCollector {
    private static UrlCollector c_instance;

    private UrlCollector() {
    }

    public static UrlCollector getInstance() {
        if (c_instance == null) {
            c_instance = new UrlCollector();
        }
        return c_instance;
    }

    public static int manualDone = 0;
    public static String webDate = "";
    public static String toastData = "";
    public static int isHoliday = 0;
    public static String umeng_miniapp="";
    public static String umeng_wx_pc_kswy="";
    public static String umeng_wx_pc_kswys="";
    public static String umeng_wx_pc_kpy="";
}
