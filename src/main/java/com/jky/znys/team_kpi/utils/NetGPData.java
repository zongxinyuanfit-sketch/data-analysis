package com.jky.znys.team_kpi.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jky.znys.team_kpi.entity.KPIRadix;
import com.jky.znys.team_kpi.entity.KPIUser;
import com.jky.znys.team_kpi.service.RadixService;
import com.jky.znys.team_kpi.service.ToastContentService;
import com.jky.znys.team_kpi.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 单例工具类
 *
 * @author mac
 */
public class NetGPData {
    private static NetGPData c_instance;

    private NetGPData() {
    }

    public static NetGPData getInstance() {
        if (c_instance == null) {
            c_instance = new NetGPData();
        }
        return c_instance;
    }

    @Autowired
    private static RadixService radixService = (RadixService) SpringContextUtils.getBean(RadixService.class);

    @Autowired
    private static UserService userService = (UserService) SpringContextUtils.getBean(UserService.class);

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getWXdata(String url_getToken, String url_data, String appid_kswys, String start_date) {
        // 获取access_token
        String ret = "";
        JSONObject jsonObject_wx = JSONObject.fromObject(HttpTransfer.sendGet(url_getToken, "wechat_id=" + appid_kswys));
        String access_token = jsonObject_wx.getString("access_token");
        // 获取微信具体数据
        String str_date = "{\"begin_date\":\"" + start_date + "\",\"end_date\":\"" + start_date + "\"}";
        // 获取用户增减数据
        ret = HttpTransfer.postJson(url_data + "access_token=" + access_token, str_date);
        return ret;
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String plusDay(int num, String newDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currdate = format.parse(newDate);
        //System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        //System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }

    /**
     * 指定日期加上天数后的月份
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String plusMonth(int num, String newDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currdate = format.parse(newDate);
        //System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.MONTH, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        //System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }

    /**
     * 获取当前月第一天
     *
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDay_str = sdf.format(calendar.getTime());
        return firstDay_str;
    }

    /**
     * 获取当前月最后一天
     *
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = 0;
        //2月的平年瑞年天数
        if (month == 2) {
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDay_str = sdf.format(calendar.getTime());
        return lastDay_str;
    }

    /**
     * 根据日期判断本月有多少天
     *
     * @author 半知半行
     */
    public static int getMonthDays(int year, int month) {
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static KPIRadix getNowRadix(String date, String mark) {
        //getMonth
        KPIRadix nowradix = new KPIRadix();
        ArrayList<KPIRadix> t_result = radixService.getDataList();
        for (int i = 0; i < t_result.size(); i++) {
            //System.out.println(t_result.get(i).getDate()+"------"+getWeek6(date));
            if (t_result.get(i).getCategoryid().equals(mark) && t_result.get(i).getDate() != null && t_result.get(i).getDate().equals(getMonth(date))) {
                nowradix = t_result.get(i);
                break;
            }
        }
        return nowradix;
    }

//    public static void setNowRadix(String date, String mark, String radix, String highscore, int id) {
//        KPIRadix t_radix = new KPIRadix();
//        t_radix.setCategoryid(mark);
//        t_radix.setDate(date);
//        t_radix.setHighscore(highscore);
//        t_radix.setRadix(radix);
//        t_radix.setId(id);
//
//        radixService.addData(t_radix);
//
//        KPIUser t_user = new KPIUser();
//        ArrayList<KPIUser> users = new ArrayList<KPIUser>();
//        users = userService.getUserList();
//        for (int i = 0; i < users.size(); i++) {
//            if (users.get(i).getMarkid() == Integer.parseInt(mark)) {
//                t_user = users.get(i);
//                t_user.setRadix(radix);
//                //userMapper.adddata(t_user, Integer.parseInt(mark));
//            }
//        }
//    }

    /**
     * 获取百分比
     *
     * @param num1
     * @param num2
     * @return
     */
    public static String getPercent(int num1, int num2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static String getNowDate() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

//    public static int getWeekNum(Date dt) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dt);
//        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        if (w < 0)
//            w = 0;
//        return w;
//    }

    public static String getMonth(String date) {
        String ret_month = "";
        DateFormat sdf_source = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat sdf_dest = new SimpleDateFormat("yyyy-MM");
        try {
            Date dt = sdf_source.parse(date);
            ret_month = sdf_dest.format(dt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret_month;
    }


    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static ArrayList<ArrayList<String>> parseSourceData(ArrayList<String> d_source, String[] d_items) {
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        try {
            for (int i = 0; i < d_source.size(); i++) {
                String jb_source_item = d_source.get(i);
                JSONObject temp = JSONObject.fromObject(jb_source_item).getJSONObject("data");
                if(temp.size()==0){
                    temp=JSONObject.fromObject(jb_source_item);
                }
                ArrayList<String> arr_result = new ArrayList<String>();
                for (int j = 0; j < d_items.length; j++) {
                    String result = "0";
                    String key = d_items[j];
                    ArrayList<String> keys = new ArrayList<>(Arrays.asList(key.contains("|") ? key.split("\\|") : key.split("\\ ")));
                    result = temp.has(keys.get(0)) ? temp.getString(keys.get(0)) : "0";
                    if (result.contains("{")) {
                        result = JSONObject.fromObject(result).getString(keys.get(1));
                    }
                    result = result.equals("null") ? "0" : result;
                    //result = result.contains(".") ? Double.valueOf(result)+"":result.split("\\.")[0];
                    arr_result.add(result);
                }
                ret.add(arr_result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            for (int i = 0; i < d_source.size(); i++) {
                ArrayList<String> arr_result = new ArrayList<String>();
                for (int j = 0; j < d_items.length; j++) {
                    String result = "0";
                    arr_result.add(result);
                }
                ret.add(arr_result);
            }
        }
        return ret;
    }

    public static int str2double2int(String result) {
        int num = 0;
        num = Integer.parseInt(Math.round(Double.valueOf(result)) + "");
        return num;
    }

    public static int strPercent2double2int(String result) {
        String local_result = result;
        if (local_result.contains("%")) {
            local_result = result.replace("%", "");
        }
        return str2double2int(local_result);
    }

    public static int getNumWX(String fkey, String skey, String source) {
        JSONObject jsonObject_wx = JSONObject.fromObject(source);
        int num = 0;
        JSONArray jsonArray_wx_wys = jsonObject_wx.getJSONArray(fkey);
        for (int i = 0; i < jsonArray_wx_wys.size(); i++) {
            JSONObject jo = jsonArray_wx_wys.getJSONObject(i);
            num += jo.getInt(skey);
        }
        return num;
    }

    /**
     * 获取网络接口数据
     *
     * @param cookie
     * @param url
     * @param pull_method
     * @param referer
     * @param payload
     * @param x_srf_token
     * @param x_srf_token_mark
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getDataBycookie(String cookie, String url, int pull_method, String referer, String payload, String x_srf_token, int x_srf_token_mark) throws UnsupportedEncodingException {
        String ret = "";
        HttpClient httpClient = new HttpClient();
        List<Header> headers = new ArrayList<Header>();
        // 设置http头
        headers.add(new Header("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36"));
        headers.add(new Header("Cookie", cookie));
        headers.add(new Header("referer", referer));
        if (x_srf_token_mark == 0) {
            headers.add(new Header("x-xsrf-token", x_srf_token));
        }
        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        int statusCode;
        // method 1:get  2:post
        if (pull_method == 1) {
            GetMethod method = new GetMethod(url);
            try {
                statusCode = httpClient.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK) {
                    System.out.println("Method failed code=" + statusCode + ": " + method.getStatusLine());
                } else {
                    ret = new String(method.getResponseBody(), "utf-8");
                    System.out.println(new String(method.getResponseBody(), "utf-8"));
                    // System.out.println(method.getResponseBodyAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                method.releaseConnection();
            }
        } else if (pull_method == 3) {
            PostMethod method = new PostMethod(url);
            method.setRequestEntity(new StringRequestEntity(payload, "application/x-www-form-urlencoded", "utf-8"));
            try {
                statusCode = httpClient.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK) {
                    System.out.println("Method failed code=" + statusCode + ": " + method.getStatusLine());
                } else {
                    ret = new String(method.getResponseBody(), "utf-8");
                    System.out.println(new String(method.getResponseBody(), "utf-8"));
                    // System.out.println(method.getResponseBodyAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                method.releaseConnection();
            }
        } else {
            PostMethod method = new PostMethod(url);
            method.setRequestEntity(new StringRequestEntity(payload, "application/json", "utf-8"));
            try {
                statusCode = httpClient.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK) {
                    System.out.println("Method failed code=" + statusCode + ": " + method.getStatusLine());
                } else {
                    ret = new String(method.getResponseBody(), "utf-8");
                    System.out.println(new String(method.getResponseBody(), "utf-8"));
                    // System.out.println(method.getResponseBodyAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                method.releaseConnection();
            }
        }
        return ret;
    }
}
