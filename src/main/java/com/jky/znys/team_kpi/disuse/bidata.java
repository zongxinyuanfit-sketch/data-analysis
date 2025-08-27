package com.jky.znys.team_kpi.disuse;

import com.jky.znys.team_kpi.entity.KPIManual;
import com.jky.znys.team_kpi.entity.KPIRequestParma;
import com.jky.znys.team_kpi.entity.KPIUmeng;
import com.jky.znys.team_kpi.service.MoneyMakerService;
import com.jky.znys.team_kpi.service.RequestParmaService;
import com.jky.znys.team_kpi.service.UMengService;
import com.jky.znys.team_kpi.utils.*;
import com.jky.znys.team_kpi.entity.BI;
import com.jky.znys.team_kpi.service.BIService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;

public class bidata {
    @Autowired
    private UMengService uMengService = (UMengService) SpringContextUtils.getBean(UMengService.class);
    @Autowired
    private RequestParmaService requestParmaService = (RequestParmaService) SpringContextUtils.getBean(RequestParmaService.class);
    @Autowired
    private BIService biService = (BIService) SpringContextUtils.getBean(BIService.class);
    @Autowired
    private MoneyMakerService moneyMakerService = (MoneyMakerService) SpringContextUtils.getBean(MoneyMakerService.class);

    ToastDingTalk tool_toast = new ToastDingTalk();
//    public void update_bi_data_manual() throws ParseException {
//        if (UrlCollector.webDate.equals("")) {
//            NetGPData.getInstance();
//            UrlCollector.webDate = NetGPData.plusDay(-1, NetGPData.getNowDate());
//        }
//        BI bi_data = biService.getDataList(UrlCollector.webDate,UrlCollector.webDate).get(0);
//        String result = bi_data.getContent();
//        wechat wechat_t = new wechat();
//        KPIManual manual_data = wechat_t.get_manual().get(0);
//        JSONArray result_array = JSONArray.fromObject(result);
//        result_array.getJSONArray(3).getJSONObject(0).put("new",manual_data.getLcnewwys());
//        result_array.getJSONArray(3).getJSONObject(0).put("live",manual_data.getLclivewys());
//        bi_data.setContent(result_array.toString());
//        biService.upDateData(bi_data);
//    }

    public String bi_data(String start, String end, String btntype) throws ParseException {
        // 从pushinfo过来有日期
        // 从外部直接进来无日期 此时会生成前一天日期，然后日期会进一步减1天
        String result = "";
        String msg = "";
        String code = "";
        String webDate = "";

        webDate = NetGPData.plusDay(-1, NetGPData.getNowDate());

        // 只拿前一天数据
        if (btntype.equals("0")) {
            try {
                BI data_bi = new BI();
                if (biService.getCount(webDate) > 0) {
                    ArrayList<BI> data_one = biService.getDataList(webDate, webDate);
                    result = JSONArray.fromObject(data_one).toString();
                    msg = "local data";
                    code = "200";
                } else {
                    ArrayList<String[]> ret_result = new ArrayList<String[]>();
                    //1 umeng  如果你七点之前打开它，它就会出错
                    ArrayList<KPIUmeng> umeng_data = uMengService.getDataList(webDate, webDate);
                    //2 ztc
                    String ztc_data = HttpTransfer.DTsendGet("https://ztc.120.net/api/kwcount?", "day=" + webDate);
                    //3 wechat manual
                    wechat wechat_t = new wechat();
                    ArrayList<String> wechat_data = wechat_t.wechat_data(webDate);
                    ArrayList<KPIManual> manual_data = wechat_t.get_manual(webDate);


                    //1 新增
                    ret_result.add(new_parser(umeng_data.get(0), wechat_data));
                    //2 活跃
                    ret_result.add(live_parser(umeng_data.get(0), manual_data.get(0)));
                    //3 收入
                    ret_result.add(money_parser(ztc_data));
                    //4 留存
                    ret_result.add(re_live_parser(manual_data.get(0)));
                    //5 复购率
                    ret_result.add(re_buy_parser(ztc_data));
                    //6 咨询量
                    ret_result.add(ask_parser(ztc_data));
                    //7 退款明细
                    ret_result.add(money_back_parser(ztc_data));
                    //8 总收入
                    String[] total_money = new String[1];
                    total_money[0] = moneyMakerService.getTotalMoneyDay(webDate,webDate).get(0).getMoney();
                    ret_result.add(total_money);

                    data_bi.setContent(JSONArray.fromObject(ret_result).toString());
                    data_bi.setUdate(webDate);
                    biService.addData(data_bi);

                    ArrayList<BI> data_one = biService.getDataList(webDate, webDate);
                    result = JSONArray.fromObject(data_one).toString();
                    msg = "online data";
                    code = "200";
                }

            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                result = "";
                msg = "make data error";
                code = "500";
            }
        }
        // 获取一段日期内数据
        else {
            try {
                int dayCount = NetGPData.daysBetween(start, end)+1;
                ArrayList<BI> data_bi = biService.getDataList(start, end);
                if (dayCount == data_bi.size()) {
                    result = JSONArray.fromObject(data_bi).toString();
                    msg = "local data of one month";
                    code = "200";
                } else {
                    result = "";
                    msg = "no such more data";
                    code = "404";
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                result = "";
                msg = "make data error";
                code = "501";
            }
        }
        return make_result_json(msg,code,result);
    }

    public String[] new_parser(KPIUmeng item, ArrayList<String> wechat_data) throws UnsupportedEncodingException, ParseException {
        //        {value: a, name: '快速问医生服务号'},
        //        {value: b, name: '问医生服务号'},
        //        {value: c, name: '快应用'},
        //        {value: d, name: '支付宝小程序'},
        //        {value: e, name: 'IOS'},
        //        {value: f, name: '安卓'},
        //        {value: g, name: '快速问医生小程序'},
        //        {value: h, name: '快速问医小程序'},
        String[] result = new String[8];
        // 获取友盟数据链接及其参数
        ArrayList<KPIRequestParma> t_rp = requestParmaService.getDataList();

        if (UrlCollector.umeng_miniapp.equals("")) {
            UrlCollector.umeng_miniapp = NetGPData.getDataBycookie(t_rp.get(1).getCookies(), t_rp.get(1).getDataurl(), 2, t_rp.get(1).getReferer(), t_rp.get(1).getParmas(), t_rp.get(1).getWxtoken(), 1);
        }
        if (UrlCollector.umeng_wx_pc_kswy.equals("")) {
            UrlCollector.umeng_wx_pc_kswy = NetGPData.getDataBycookie(t_rp.get(4).getCookies(), t_rp.get(4).getDataurl(), 2, t_rp.get(1).getReferer(), parser_payload(t_rp.get(4).getParmas()), t_rp.get(4).getWxtoken(), 1);
        }
        if (UrlCollector.umeng_wx_pc_kswys.equals("")) {
            UrlCollector.umeng_wx_pc_kswys = NetGPData.getDataBycookie(t_rp.get(5).getCookies(), t_rp.get(5).getDataurl(), 2, t_rp.get(1).getReferer(), parser_payload(t_rp.get(5).getParmas()), t_rp.get(5).getWxtoken(), 1);
        }

        JSONArray umeng_array = JSONObject.fromObject(UrlCollector.umeng_miniapp).getJSONObject("data").getJSONArray("data");
        //微信爬虫部分数据，要被减掉
        JSONArray umeng_pc_kswy = JSONObject.fromObject(UrlCollector.umeng_wx_pc_kswy).getJSONObject("data").getJSONArray("data");
        JSONArray umeng_pc_kswys = JSONObject.fromObject(UrlCollector.umeng_wx_pc_kswys).getJSONObject("data").getJSONArray("data");

        result[0] = NetGPData.getNumWX("list", "new_user", wechat_data.get(0)) + "";
        result[1] = NetGPData.getNumWX("list", "new_user", wechat_data.get(1)) + "";
        result[2] = item.getNewkyy();
        result[3] = umeng_array.getJSONObject(3).getString("preNewUser");
        result[4] = item.getNewumengikswys();
        result[5] = item.getNewumengakswys();
        result[6] = (umeng_array.getJSONObject(1).getInt("preNewUser") - parser_wx_pc(umeng_pc_kswy)[1]) + "";
        result[7] = (umeng_array.getJSONObject(2).getInt("preNewUser") - parser_wx_pc(umeng_pc_kswys)[1]) + "";

        return result;
    }

    public String[] live_parser(KPIUmeng uitem, KPIManual mitem) {
        //        {value: live_data[0], name: '快速问医生服务号'},
        //        {value: live_data[1], name: '问医生服务号'},
        //        {value: live_data[2], name: '快应用'},
        //        {value: live_data[3], name: '支付宝小程序'},
        //        {value: live_data[4], name: 'IOS'},
        //        {value: live_data[5], name: '安卓'},
        //        {value: live_data[6], name: '快速问医生小程序'},
        //        {value: live_data[7], name: '快速问医小程序'},
        String[] result = new String[8];

        JSONArray umeng_array = JSONObject.fromObject(UrlCollector.umeng_miniapp).getJSONObject("data").getJSONArray("data");
        //微信爬虫部分数据，要被减掉
        JSONArray umeng_pc_kswy = JSONObject.fromObject(UrlCollector.umeng_wx_pc_kswy).getJSONObject("data").getJSONArray("data");
        JSONArray umeng_pc_kswys = JSONObject.fromObject(UrlCollector.umeng_wx_pc_kswys).getJSONObject("data").getJSONArray("data");

        result[0] = mitem.getLivewxkswys();
        result[1] = mitem.getLivewxwys();
        result[2] = uitem.getLivekyy();
        result[3] = umeng_array.getJSONObject(3).getString("preActiveUser");
        result[4] = uitem.getLiveumengikswys();
        result[5] = uitem.getLiveumengakswys();
        result[6] = (umeng_array.getJSONObject(1).getInt("preActiveUser") - parser_wx_pc(umeng_pc_kswy)[0]) + "";
        result[7] = (umeng_array.getJSONObject(2).getInt("preActiveUser") - parser_wx_pc(umeng_pc_kswys)[0]) + "";

        return result;
    }

    public String[] money_parser(String ztc_data) {
        //        {value: money_data[0], name: '免费咨询'},
        //        {value: money_data[1], name: '指定咨询'},
        //        {value: money_data[2], name: '私人医生'},
        //        {value: money_data[3], name: '极速咨询'},
        //        {value: money_data[4], name: '视频门诊'},
        //        {value: money_data[5], name: '百度咨询'}
        //        {value: money_data[6], name: '医生推荐'}
        String[] result = new String[7];
        JSONObject j_ztc = JSONObject.fromObject(ztc_data).getJSONObject("data");
        result[0] = j_ztc.getString("free_ask_pay");
        result[1] = j_ztc.getString("order_price");
        result[2] = j_ztc.getString("bao_order_tel");
        result[3] = j_ztc.getString("jszx_allmoney");
        result[4] = j_ztc.getJSONObject("hlwyy").getString("money");
        result[5] = j_ztc.getString("baidu_direct_money");
        result[6] = j_ztc.getString("hzbd_info");

        return result;
    }

    public String[] re_live_parser(KPIManual mitem) throws UnsupportedEncodingException, ParseException {
        //        ['问医生服务号', re_live_data[0][0], re_live_data[0][1]],
        //        ['快应用', re_live_data[1][0], re_live_data[1][1]],
        //        ['支付宝小程序', re_live_data[2][0], re_live_data[2][1]],
        //        ['IOS', re_live_data[3][0], re_live_data[3][1]],
        //        ['安卓', re_live_data[4][0], re_live_data[4][1]],
        //        ['快速问医生小程序', re_live_data[5][0], re_live_data[5][1]],
        //        ['快速问医小程序', re_live_data[6][0], re_live_data[6][1]]
        String[] result = new String[6];
        try {
            ArrayList<KPIRequestParma> t_rp = requestParmaService.getDataList();
            String[] result_temp = new String[12];
            result_temp[0] = NetGPData.getDataBycookie(t_rp.get(6).getCookies(), t_rp.get(6).getDataurl(), 2, "", parser_payload(t_rp.get(6).getParmas()), t_rp.get(6).getWxtoken(), 0);
            result_temp[1] = NetGPData.getDataBycookie(t_rp.get(7).getCookies(), t_rp.get(7).getDataurl(), 2, "", parser_payload(t_rp.get(7).getParmas()), t_rp.get(7).getWxtoken(), 0);
            result_temp[2] = NetGPData.getDataBycookie(t_rp.get(8).getCookies(), t_rp.get(8).getDataurl(), 2, "", parser_payload(t_rp.get(8).getParmas()), t_rp.get(8).getWxtoken(), 0);
            result_temp[3] = NetGPData.getDataBycookie(t_rp.get(9).getCookies(), t_rp.get(9).getDataurl(), 2, "", parser_payload(t_rp.get(9).getParmas()), t_rp.get(9).getWxtoken(), 0);
            result_temp[4] = NetGPData.getDataBycookie(t_rp.get(10).getCookies(), t_rp.get(10).getDataurl(), 2, "", parser_payload(t_rp.get(10).getParmas()), t_rp.get(10).getWxtoken(), 0);
            result_temp[5] = NetGPData.getDataBycookie(t_rp.get(11).getCookies(), t_rp.get(11).getDataurl(), 2, "", parser_payload(t_rp.get(11).getParmas()), t_rp.get(11).getWxtoken(), 0);
            result_temp[6] = NetGPData.getDataBycookie(t_rp.get(12).getCookies(), t_rp.get(12).getDataurl(), 2, t_rp.get(12).getReferer(), parser_payload(t_rp.get(12).getParmas()), t_rp.get(12).getWxtoken(), 1);
            result_temp[7] = NetGPData.getDataBycookie(t_rp.get(13).getCookies(), t_rp.get(13).getDataurl(), 2, t_rp.get(13).getReferer(), parser_payload(t_rp.get(13).getParmas()), t_rp.get(13).getWxtoken(), 1);
            result_temp[8] = NetGPData.getDataBycookie(t_rp.get(14).getCookies(), t_rp.get(14).getDataurl(), 2, t_rp.get(14).getReferer(), parser_payload(t_rp.get(14).getParmas()), t_rp.get(14).getWxtoken(), 1);
            result_temp[9] = NetGPData.getDataBycookie(t_rp.get(15).getCookies(), t_rp.get(15).getDataurl(), 2, t_rp.get(15).getReferer(), parser_payload(t_rp.get(15).getParmas()), t_rp.get(15).getWxtoken(), 1);
            result_temp[10] = NetGPData.getDataBycookie(t_rp.get(16).getCookies(), t_rp.get(16).getDataurl(), 2, t_rp.get(16).getReferer(), parser_payload(t_rp.get(16).getParmas()), t_rp.get(16).getWxtoken(), 1);
            result_temp[11] = NetGPData.getDataBycookie(t_rp.get(17).getCookies(), t_rp.get(17).getDataurl(), 2, t_rp.get(17).getReferer(), parser_payload(t_rp.get(17).getParmas()), t_rp.get(17).getWxtoken(), 1);


            result[0] = parser_return(result_temp[0], result_temp[1]);
            result[1] = parser_return(result_temp[2], result_temp[3]);
            result[2] = parser_return(result_temp[4], result_temp[5]);
            result[3] = parser_return(result_temp[6], result_temp[7]);
            result[4] = parser_return(result_temp[8], result_temp[9]);
            result[5] = parser_return(result_temp[10], result_temp[11]);
        }catch (Exception e){
            tool_toast.makeDToast_text("15015962016", "获取友盟留存数据异常，请检查cookie是否过期。");
            for(int i=0;i<6;i++){
                JSONObject item = new JSONObject();
                item.put("new","0");
                item.put("live","0");
                result[i] = item.toString();
            }
        }
        return result;
    }

    public String[] re_buy_parser(String ztc_data) {
        //        {value: re_buy_data[0], name: '指定咨询'},
        //        {value: re_buy_data[1], name: '私人医生'},
        //        {value: re_buy_data[2], name: '极速咨询'},
        //        {value: re_buy_data[3], name: '视频门诊'},
        //        {value: re_buy_data[4], name: '药品'},
        //        {value: re_buy_data[5], name: '打赏'}
        String[] result = new String[4];
        JSONObject j_ztc = JSONObject.fromObject(ztc_data).getJSONObject("data");
        result[0] = j_ztc.getString("fugou30_appoint");
        result[1] = j_ztc.getString("fugou30_srys");
        result[2] = j_ztc.getString("fugou30_jszx");
        result[3] = j_ztc.getString("fugou30_pay");

        return result;
    }

    public String[] ask_parser(String ztc_data) {
        //        '免费咨询', '指定咨询', '私人医生', '极速咨询', '视频门诊', '百度咨询'
        String[] result = new String[6];
        JSONObject j_ztc = JSONObject.fromObject(ztc_data).getJSONObject("data");
        result[0] = j_ztc.getString("ask_free_total");
        result[1] = j_ztc.getString("order_num_zs");
        result[2] = "0";
        result[3] = j_ztc.getString("jszx_order_nums");
        result[4] = j_ztc.getJSONObject("hlwyy").getString("num");
        result[5] = j_ztc.getString("baidu_zxl");

        return result;
    }

    public String[] money_back_parser(String ztc_data) {
        //        {value: money_back_data[0], name: '退款数>' + a},
        //        {value: money_back_data[1], name: '退款率>' + b},
        //        {value: money_back_data[2], name: '冻结率>' + c},
        //        {value: money_back_data[3], name: '退款金额>' + d},
        String[] result = new String[4];
        JSONObject j_ztc = JSONObject.fromObject(ztc_data).getJSONObject("data");
        result[0] = NetGPData.getPercent(j_ztc.getInt("today_tuikuan_sum"),j_ztc.getInt("today_success_sum"));
        result[1] = j_ztc.getString("order_frozen_count");
        result[2] = j_ztc.getString("djl_info");
        result[3] = j_ztc.getString("order_back_amount");
        return result;
    }

    public int[] parser_wx_pc(JSONArray items) {
        int[] result = new int[2];
        //微信爬虫部分，要被减掉
        for (int i = 0; i < items.size(); i++) {
            if (items.getJSONObject(i).getString("id").equals("wx_1129")) {
                result[0] = items.getJSONObject(i).getInt("activeUser");
                result[1] = items.getJSONObject(i).getInt("newUser");
                break;
            }
        }
        return result;
    }

    public String parser_return(String new_return, String live_return) {
        JSONObject result = new JSONObject();
        if(new_return.length()>10&&new_return.length()>10) {
            JSONObject j_new_return = JSONObject.fromObject(new_return);
            JSONObject j_live_return = JSONObject.fromObject(live_return);
            if (j_live_return.getString("code").equals("200") && j_new_return.getString("code").equals("200")) {
                JSONArray new_array = j_new_return.getJSONObject("data").getJSONArray("items");
                JSONArray live_array = j_live_return.getJSONObject("data").getJSONArray("items");
                for (int i = 0; i < new_array.size(); i++) {
                    if (new_array.getJSONObject(i).getString("key").equals("retentionValueRate")) {
                        JSONArray item = new_array.getJSONObject(i).getJSONArray("data");
                        result.put("new", item.getString(item.size() - 2));
                    }
                }
                for (int i = 0; i < live_array.size(); i++) {
                    if (live_array.getJSONObject(i).getString("key").equals("retentionValueRate")) {
                        JSONArray item = live_array.getJSONObject(i).getJSONArray("data");
                        result.put("live", item.getString(item.size() - 2));
                    }
                }
            } else {
                result.put("new", "0");
                result.put("live", "0");
            }
        }else {
            result.put("new", new_return);
            result.put("live", live_return);
        }
        return result.toString();
    }

    public String parser_payload(String payload) throws ParseException {
        String result = "";
        String webDate = NetGPData.plusDay(-1, NetGPData.getNowDate());
        JSONObject j_payload = JSONObject.fromObject(payload);
        j_payload.put("fromDate", NetGPData.plusDay(-7, webDate));
        j_payload.put("toDate", webDate);
        result = j_payload.toString();
        return result;
    }

    public String make_result_json(String msg, String code, String c_result) {
        JSONObject result = new JSONObject();
        result.put("msg", msg);
        result.put("code", code);
        result.put("result", c_result);
        return result.toString();
    }

}
