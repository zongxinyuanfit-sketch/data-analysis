package com.jky.znys.team_kpi.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import com.jky.znys.team_kpi.entity.KPIExcelConfig;
import com.jky.znys.team_kpi.entity.KPIManual;
import com.jky.znys.team_kpi.entity.KPIUmeng;
import com.jky.znys.team_kpi.service.ExcelConfigService;
import com.jky.znys.team_kpi.service.ManualService;
import com.jky.znys.team_kpi.service.MoneyMakerService;
import com.jky.znys.team_kpi.service.UMengService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class ToastDingTalk {
    @Autowired
    private ExcelConfigService excelConfigService=(ExcelConfigService)SpringContextUtils.getBean(ExcelConfigService.class);
    @Autowired
    private ManualService manualService=(ManualService)SpringContextUtils.getBean(ManualService.class);
    @Autowired
    private UMengService uMengService=(UMengService)SpringContextUtils.getBean(UMengService.class);
//    @Autowired
//    private MoneyMakerService moneyMakerService=(MoneyMakerService)SpringContextUtils.getBean(MoneyMakerService.class);


    public ArrayList<String[]> makeToast(boolean is_toast) {
        ArrayList<KPIExcelConfig> get_excelconfig_result = excelConfigService.getConfigList();
        UrlCollector.toastData = "";
        ArrayList<String[]> ret_result = new ArrayList<>();
        try {
            if (UrlCollector.webDate.equals("")) {
                NetGPData.getInstance();
                UrlCollector.webDate = NetGPData.plusDay(-1, NetGPData.getNowDate());
            }
            String date_o = NetGPData.plusDay(-2, UrlCollector.webDate);
            String date_t = NetGPData.plusDay(-1, UrlCollector.webDate);
            String date_s = UrlCollector.webDate;

            ArrayList<String> dates = new ArrayList<>();
            dates.add(date_s);
            dates.add(date_t);
            dates.add(date_o);
            String[] arr_keys_total_income = {"free_ask_pay", "order_price", "jszx_allmoney", "kwapp_gift_day_buy", "hlwyy|money", "kwapp_zhiban_doctor_pay", "buy_yaopin|money", "chufang|money", "tijian_info|price", "kwapp_jpkc_pay", "ask_xuanshang_money", "ask_dashang_money", "coupon_money", "baidu_direct_money", "abc_pay", "baidu_reward", "xinglinzhongyi"};
            String[] arr_keys_free_zx = {"free_ask_pay", "ask_xuanshang_money", "ask_dashang_money", "free_ask", "free_ask_orders","ask_xuanshang_orders"};
            String[] arr_keys_pay_zx = {"appoint_all_jine", "bao_order_tel", "hlwyy|money", "kwapp_gift_day_buy", "baidu_direct_money", "abc_pay", "baidu_reward", "order_num_zs", "kwapp_zhiban_doctor_orders", "hlwyy|num", "kwapp_gift_day_orders"};
            String[] arr_keys_other_pay = {"buy_yaopin|money", "chufang|money", "tijian_info|price", "kwapp_jpkc_pay","xinglinzhongyi", "buy_yaopin|num", "chufang|num", "tijian_info|num", "kwapp_jpkc_pay_orders","xinglinzhongyi_danshu"};
            String[] arr_keys_jszx_pay = {"jszx_allmoney", "jszx_order_nums"};
            String[] arr_keys_uv = {"kwdoclistUV", "kepu_article_uv", "120uv", "medianum", "doctor_bind_count", "tuidan_sums", "order_doctor_count", "kwapp_twoweek_fugou", "baomaquan_info|looks","kwapp_ptw_mf_ff","kwapp_mfg_unum","fugou30_repeat_appoint","fugou30_repeat_jszx","chufang_buy_pass","ask_free_total","djl_info","kwapp_fgbl","zfl_info","hzbd_info","hzbd_pay_orders"};
            String[] arr_keys_21Oc={"kaohe_fwzje_gzh_appoint","kaohe_fwzje_gzh_srys","kaohe_fwzje_gzh_jszx","kaohe_fwzje_gzh_inquiry","kaohe_fwzje_xcx_appoint","kaohe_fwzje_xcx_srys","kaohe_fwzje_xcx_jszx","kaohe_fwzje_xcx_inquiry","kaohe_fwzje_xcxgzh_dsxs","kaohe_fwzje_all","hzbd_pay_orders","kaohe_yszs","kaohe_fwzje_xcxgzh","kaohe_fwzje_120net_appoint","kaohe_fwzje_pc_appoint"};
            String[] arr_keys_21No={"kaohe_swh_mfzx_all","kaohe_swh_appoint_app","kaohe_swh_srys_app","kaohe_swh_inquiry_app","kaohe_swh_jszx_app","kaohe_swh_appoint_kyy","kaohe_swh_srys_kyy","kaohe_swh_inquiry_kyy","kaohe_swh_jszx_kyy"};

            String[] arr_keys_caiwu={"zx_income","second_income","profit","shop_income","doctor_expend","shop_expend","gift_income","bddx_income","zycf_income","total_income","total_expend","iiyi_expend","zycf_expend","kecheng_income","shop_income","tijian_income","zycf_order_num"};
            String[] arr_keys_team={"total|total_profit","app|total_profit","hd|total_profit","other|total_profit","wx|total_profit","zfb|total_profit","app|total_income","hd|total_income","other|total_income","wx|total_income","zfb|total_income"};
            //total_income profit second_income
            String str_ztc_o = HttpTransfer.DTsendGet("https://ztc.120.net/api/kwcount?", "day=" + date_o);
            String str_ztc_t = HttpTransfer.DTsendGet("https://ztc.120.net/api/kwcount?", "day=" + date_t);
            String str_ztc_s = HttpTransfer.DTsendGet("https://ztc.120.net/api/kwcount?", "day=" + date_s);

            String str_kcaiwu_o=HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/department?", "dateline=" + date_o);
            String str_kcaiwu_t=HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/department?", "dateline=" + date_t);
            String str_kcaiwu_s=HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/department?", "dateline=" + date_s);

            String str_kteam_o=HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/kaohe_team?", "dateline=" + date_o);
            String str_kteam_t=HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/kaohe_team?", "dateline=" + date_t);
            String str_kteam_s=HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/kaohe_team?", "dateline=" + date_s);


            ArrayList<String> result_ztc_list = new ArrayList();
            ArrayList<String> result_kcaiwu_list = new ArrayList();
            ArrayList<String> result_team_list = new ArrayList();
            result_ztc_list.add(str_ztc_s);
            result_ztc_list.add(str_ztc_t);
            result_ztc_list.add(str_ztc_o);

            result_kcaiwu_list.add(str_kcaiwu_s);
            result_kcaiwu_list.add(str_kcaiwu_t);
            result_kcaiwu_list.add(str_kcaiwu_o);

            result_team_list.add(str_kteam_s);
            result_team_list.add(str_kteam_t);
            result_team_list.add(str_kteam_o);

            NetGPData.getInstance();
            ArrayList<ArrayList<String>> result_total_incomes = NetGPData.parseSourceData(result_ztc_list, arr_keys_total_income);

            ArrayList<ArrayList<String>> result_free_zxs = NetGPData.parseSourceData(result_ztc_list, arr_keys_free_zx);

            ArrayList<ArrayList<String>> result_pay_zxs = NetGPData.parseSourceData(result_ztc_list, arr_keys_pay_zx);

            ArrayList<ArrayList<String>> result_other_pays = NetGPData.parseSourceData(result_ztc_list, arr_keys_other_pay);

            ArrayList<ArrayList<String>> result_jszx_pay = NetGPData.parseSourceData(result_ztc_list, arr_keys_jszx_pay);

            ArrayList<ArrayList<String>> result_keys_21Oc = NetGPData.parseSourceData(result_ztc_list, arr_keys_21Oc);

            ArrayList<ArrayList<String>> result_keys_21No = NetGPData.parseSourceData(result_ztc_list, arr_keys_21No);

            ArrayList<ArrayList<String>> result_caiwu_22Ja = NetGPData.parseSourceData(result_kcaiwu_list, arr_keys_caiwu);

            ArrayList<ArrayList<String>> result_team_22Ja = NetGPData.parseSourceData(result_team_list, arr_keys_team);
            //1.总收入
            ret_result.add(income_total(result_total_incomes));
            //2.免费收入
            ret_result.add(income_free(result_free_zxs));
            //3.付费收入
            ret_result.add(income_pay(result_pay_zxs));
            //4.其他收入
            ret_result.add(income_other(result_other_pays, dates));
            //5.极速咨询收入
            ret_result.add(income_jszx(result_jszx_pay));

            ArrayList<KPIManual> manuallist = new ArrayList();
            manuallist = manualService.getDataList(date_o, date_s);

            //ArrayList<String> un_exist_date = new ArrayList();
            for (int i = 0; i < dates.size(); i++) {
                boolean is_exist = false;
                for (int k = 0; k < manuallist.size(); k++) {
                    if (manuallist.get(k).getUdate().equals(dates.get(i))) {
                        is_exist = true;
                        break;
                    }
                }
                if (!is_exist) {
                    KPIManual item = new KPIManual();
                    item.setLivewxkswys("0");
                    item.setLivewxwys("0");
                    item.setLivekwjk("0");
                    item.setMediareadnum("0");
                    item.setApplitelive("0");
                    item.setUdate(dates.get(i));
                    item.setVideopv("0");
                    item.setZfbreuser("0");
                    item.setZfblive("0");
                    item.setZfbmoney("0");
                    item.setNewkwjk("0");
                    item.setFugouwx("0");
                    manuallist.add(i, item);
                }
            }
            ArrayList<KPIUmeng> umenglist = new ArrayList();
            umenglist = uMengService.getDataList(date_o, date_s);


            //6.APP数据
            ret_result.add(makeAPPdata(umenglist));

            ArrayList<ArrayList<String>> wx_result = new ArrayList();

            ArrayList<String> wx_result_o = new ArrayList();
            String str_useInDecrease_kswys_o = NetGPData.getWXdata((get_excelconfig_result.get(0)).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidkswys(), date_o);
            String str_useInDecrease_wys_o = NetGPData.getWXdata((get_excelconfig_result.get(0)).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidwys(), date_o);

            wx_result_o.add(str_useInDecrease_kswys_o);
            wx_result_o.add(str_useInDecrease_wys_o);

            ArrayList<String> wx_result_t = new ArrayList();
            String str_useInDecrease_kswys_t = NetGPData.getWXdata((get_excelconfig_result.get(0)).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidkswys(), date_t);
            String str_useInDecrease_wys_t = NetGPData.getWXdata((get_excelconfig_result.get(0)).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidwys(), date_t);

            wx_result_t.add(str_useInDecrease_kswys_t);
            wx_result_t.add(str_useInDecrease_wys_t);

            ArrayList<String> wx_result_s = new ArrayList();
            String str_useInDecrease_kswys_s = NetGPData.getWXdata((get_excelconfig_result.get(0)).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidkswys(), date_s);
            String str_useInDecrease_wys_s = NetGPData.getWXdata((get_excelconfig_result.get(0)).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidwys(), date_s);

            wx_result_s.add(str_useInDecrease_kswys_s);
            wx_result_s.add(str_useInDecrease_wys_s);

            wx_result.add(wx_result_s);
            wx_result.add(wx_result_t);
            wx_result.add(wx_result_o);
            //7.微信数据
            ret_result.add(makeWXdata(wx_result, manuallist));
            //8.APP+微信总数据
            ret_result.add(makeAPPWXtotal(ret_result.get(5), ret_result.get(6)));

            NetGPData.getInstance();
            //9.UV数据
            ret_result.add(makeUVdata(NetGPData.parseSourceData(result_ztc_list, arr_keys_uv), manuallist, umenglist));

            //10.收入分析数据
            //保存总收入
//            {"zx_income","second_income","profit","shop_income","doctor_expend","shop_expend","gift_income","bddx_income","zycf_income","total_income","total_expend"};
            //  固定支出=工资+公摊+水电分摊
            double stable_expend = 8990.55;
            //  税费=(销售额/1.06*0.06)
            double tax_expend = (Double.valueOf(result_caiwu_22Ja.get(0).get(9)))/1.06*0.06;
            //  预计支出=爱爱医支出+中医支出
            double predicted_expend = Double.valueOf(result_caiwu_22Ja.get(0).get(11))+Double.valueOf(result_caiwu_22Ja.get(0).get(12));
            String caiwu_profit=str2double((Double.valueOf(result_caiwu_22Ja.get(0).get(9))-stable_expend-tax_expend-predicted_expend)+"");
            //String caiwu_profit=str2double((Double.valueOf(result_caiwu_22Ja.get(0).get(2))-9143-((Double.valueOf(result_caiwu_22Ja.get(0).get(9))+Double.valueOf(result_caiwu_22Ja.get(0).get(1)))/1.06*0.06))+"");
//            if(moneyMakerService.getCountUdate(UrlCollector.webDate)==0) {
//                moneyMakerService.insertTotalMoneyDay(caiwu_profit,UrlCollector.webDate);
//            }else {
//                // 更新数据
//                moneyMakerService.updateTotalMoneyDay(caiwu_profit,UrlCollector.webDate);
//            }

//            MoneyMaker moneyMaker = new MoneyMaker();
//            ret_result.add(moneyMaker.makeWeekResult());//10
//            ret_result.add(moneyMaker.makeMonthResult());//11
//            ret_result.add(moneyMaker.makeYearResult());//12

            //11支付宝数据
            ret_result.add(makeZfbData(manuallist,result_team_22Ja));//13

            //12.极速咨询收入
            ret_result.add(makeNewWXInCome(result_keys_21Oc));//14

            //13.极速咨询收入
            ret_result.add(makeAppKyyInCome(result_keys_21No));//15

            //14.特殊需要数据
            String[] e_arr =new String[1];
            e_arr[0] = result_team_22Ja.get(0).get(4);
            ret_result.add(e_arr);//16

            //15.保存财务数据
            String[] caiwu_arr = new String[3];
            caiwu_arr[0]=result_caiwu_22Ja.get(0).get(9);
            caiwu_arr[1]=result_caiwu_22Ja.get(1).get(9);
            caiwu_arr[2]=result_caiwu_22Ja.get(2).get(9);
            ret_result.add(caiwu_arr);//17
            //早上7：30分推送一次
            if(is_toast){
                makeDToast(UrlCollector.webDate, ret_result,umenglist,manuallist,result_caiwu_22Ja,result_team_22Ja);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret_result;
    }

    private String str2double(String str){
        String result = "";
        Double d= Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        result = df.format(d);
        return result;
    }

    public void makeDToast(String date, ArrayList<String[]> result_data, ArrayList<KPIUmeng> umeng_data, ArrayList<KPIManual> manual_data,ArrayList<ArrayList<String>> result_caiwu_22Ja,ArrayList<ArrayList<String>> result_team_22Ja) {
        ArrayList<KPIExcelConfig> get_excelconfig_result = excelConfigService.getConfigList();

        int wx_live = (Integer.parseInt(manual_data.get(0).getLivewxkswys())
                +Integer.parseInt(manual_data.get(0).getLivewxwys())
                +Integer.parseInt(manual_data.get(0).getLivekwjk())
                +Integer.parseInt(umeng_data.get(0).getLivexcxkswys())
                +Integer.parseInt(umeng_data.get(0).getLivexcxkswy())
                +Integer.parseInt(umeng_data.get(0).getLivexcxkpy()));
        int wx_new = (Integer.parseInt(result_data.get(6)[3])
                +Integer.parseInt(result_data.get(6)[4])
                +Integer.parseInt(manual_data.get(0).getNewkwjk())
                +Integer.parseInt(umeng_data.get(0).getNewxcxkswys())
                +Integer.parseInt(umeng_data.get(0).getNewxcxkswy())
                +Integer.parseInt(umeng_data.get(0).getNewxcxkpy()));

        int zfb_live = Integer.parseInt(manual_data.get(0).getZfblive());
        int zfb_new=Integer.parseInt(manual_data.get(0).getZfbreuser());

        int app_kyy_live=(Integer.parseInt(umeng_data.get(0).getLiveumengakswys())+Integer.parseInt(umeng_data.get(0).getLiveumengikswys())+Integer.parseInt(umeng_data.get(0).getLivekyy()));
        int app_kyy_new=(Integer.parseInt(umeng_data.get(0).getNewumengakswys())+Integer.parseInt(umeng_data.get(0).getNewumengikswys())+Integer.parseInt(umeng_data.get(0).getNewkyy()));
        //  固定支出=工资+公摊+水电分摊
        double stable_expend = 8990.55;
        //  税费=(销售额/1.06*0.06)
        double tax_expend = (Double.valueOf(result_caiwu_22Ja.get(0).get(9)))/1.06*0.06;
        //  预计支出=爱爱医支出+中医支出
        double predicted_expend = Double.valueOf(result_caiwu_22Ja.get(0).get(11))+Double.valueOf(result_caiwu_22Ja.get(0).get(12));
        String caiwu_profit=str2double((Double.valueOf(result_caiwu_22Ja.get(0).get(9))-stable_expend-tax_expend-predicted_expend)+"");
        //  其它收入
        String other_profit=str2double((Double.valueOf(result_caiwu_22Ja.get(0).get(13))+Double.valueOf(result_caiwu_22Ja.get(0).get(14))+Double.valueOf(result_caiwu_22Ja.get(0).get(14)))+"");
        String zfb_money=result_team_22Ja.get(0).get(10);
        String zfb_money_last=result_team_22Ja.get(1).get(10);
//        (str2double(result_data.get(0)[0])) + compareValueD((result_data.get(0))[0], (result_data.get(0))[1])
        String str_toast = "{\n    \"actionCard\": {\n        \"title\": \"智能医生-每日数据报表\", \n         \"text\": \"![screenshot](https://pub.120askimages.com/kswys/newbgnew.png) \\n #### 智能医生"
                + date + "总数据概览 "
                +"\\n\\n 总销售额 : "+(str2double(result_caiwu_22Ja.get(0).get(9))) + compareValueD(str2double(result_caiwu_22Ja.get(0).get(9)), str2double(result_caiwu_22Ja.get(1).get(9)))
                +"\\n\\n 预计支出（3月公摊数据）: "+str2double((stable_expend+tax_expend+predicted_expend)+"")
                +"\\n\\n 预计利润 : "+caiwu_profit
                //1
                + "\\n\\n 指定 : " +str2double((result_data.get(2))[0]) + compareValueD((result_data.get(2))[0], (result_data.get(2))[2])
                + " || 订单 : "
                + (result_data.get(2))[1]

                //2
                + "\\n\\n 极速 : " +str2double((result_data.get(4))[0]) + compareValueD((result_data.get(4))[0], (result_data.get(4))[2])+ " || 订单 : "+ (result_data.get(4))[1]

                //3
                + "\\n\\n 免费 : " +str2double((result_data.get(1))[0]) + compareValueD((result_data.get(1))[0], (result_data.get(1))[4])
                + " || 未悬赏数 : "
                + (result_data.get(1))[1]
                + " || 悬赏数 : "
                + (result_data.get(1))[2]

                //4
                + "\\n\\n 中药 : " +str2double((result_data.get(3))[0]) + compareValueD((result_data.get(3))[0], (result_data.get(3))[6])+ " || 订单 : "+ result_caiwu_22Ja.get(0).get(16).replace(".00","")

                //5
                + "\\n\\n 其它 : " +other_profit




//                +"\\n\\n 总收入 : "+(caiwu_profit)+" || "
//                + " || "+(wx_live+zfb_live+app_kyy_live)
//                + " || "+(wx_new+app_kyy_new+zfb_new)
//
//                //1
//                + "\\n\\n 指定咨询（不含诊室） : " +str2double((result_data.get(2))[0]) + compareValueD((result_data.get(2))[0], (result_data.get(2))[2])
//                + " || "
//                + (result_data.get(2))[1]
//
//                //2
//                + "\\n\\n 极速咨询 : " +str2double((result_data.get(4))[0]) + compareValueD((result_data.get(4))[0], (result_data.get(4))[2])+ " || "+ (result_data.get(4))[1]
//
//                //3
//                + "\\n\\n 免费咨询 : " +str2double((result_data.get(1))[0]) + compareValueD((result_data.get(1))[0], (result_data.get(1))[3])
//                + " || "
//                + (result_data.get(1))[1]
//                + " || "
//                + (result_data.get(1))[2]
//
//                //4
//                + "\\n\\n 新收入 : " +str2double((result_data.get(3))[0]) + compareValueD((result_data.get(3))[0], (result_data.get(3))[5])
//                + " || "
//                + (result_data.get(3))[2] + " / " + (result_data.get(3))[3] + " / " + (result_data.get(3))[4] + " / " + (result_data.get(3))[5]
//
//                //5
//                + "\\n\\n 微信 : "+(result_team_22Ja.get(0).get(4))+" || " +result_team_22Ja.get(0).get(9) + compareValueD(result_team_22Ja.get(0).get(9), result_team_22Ja.get(1).get(9))
//                + " || "
//                + wx_live
//
//                + " || "
//                + wx_new
//
//                //6
//                + "\\n\\n 支付宝 : "+(result_team_22Ja.get(0).get(5))+" || " +(zfb_money + compareValueD(zfb_money, zfb_money_last))
//                + " || " +zfb_live
//                + " || " +zfb_new
//
//                //7
//                + "\\n\\n App+快应用 : "+(result_team_22Ja.get(0).get(1))+" || " +result_team_22Ja.get(0).get(6) + compareValueD(result_team_22Ja.get(0).get(6), result_team_22Ja.get(1).get(6))
//                + " || " +app_kyy_live
//                + " || " +app_kyy_new
                //8
//                + "\\n\\n 医生诊室 : "+(result_team_22Ja.get(0).get(2))+" || " +result_team_22Ja.get(0).get(7) + compareValueD(result_team_22Ja.get(0).get(7), result_team_22Ja.get(1).get(7))
//                + " || " +result_data.get(13)[10]
//                + " || " +result_data.get(8)[4]


//                + "\", \n\"hideAvatar\": \"0\", \n\"btnOrientation\": \"0\", \n\"singleTitle\" : \"点击查看具体数据\",\n\"singleURL\" : \"" + (get_excelconfig_result.get(0)).getUrlreport() + "\"\n}, \n\"msgtype\": \"actionCard\"\n}";
//                +  "\"\n}, \n\"msgtype\": \"markdown\"\n}";
                + "\", \n\"hideAvatar\": \"0\", \n\"btnOrientation\": \"0\", \n\"singleTitle\" : \"\",\n\"singleURL\" : \"\"\n}, \n\"msgtype\": \"actionCard\"\n}";



        String str_toast_itman = "{\"msgtype\": \"text\",\n\"text\": {\n\"content\": \"" + date + "\n\n 智能医生预估毛利: "+(caiwu_profit)+"\n\n 智能医生流水：" + (str2double(result_caiwu_22Ja.get(0).get(9))) + compareValueD(str2double(result_caiwu_22Ja.get(0).get(9)), str2double(result_caiwu_22Ja.get(1).get(9))) + "\"\n}\n}";

//        String str_toast_yunying="{\n    \"actionCard\": {\n        \"title\": \"智能医生-流水明细\", \n         \"text\": \"![screenshot](https://pub.120askimages.com/kswys/newbgnew.png) \\n #### 智能医生"
//                + date + "总数据概览 \\n\\n 总收入 - " + result_team_22Ja.get(0).get(9)
//                + "\\n\\n 公众号-单次咨询服务金额 : " + str2double(result_data.get(13)[0])
//                + "\\n\\n 公众号-私人医生 : " + str2double(result_data.get(13)[1])
//                + "\\n\\n 公众号-急速咨询服务金额 : " + str2double(result_data.get(13)[2])
//                + "\\n\\n 公众号-视频门诊务金额 : " + str2double(result_data.get(13)[3])
//                + "\\n\\n 小程序-单次咨询总金额 : " + str2double(result_data.get(13)[4])
//                + "\\n\\n 小程序-私人医生总金额 : " + str2double(result_data.get(13)[5])
//                + "\\n\\n 小程序-极速咨询总金额 : " + str2double(result_data.get(13)[6])
//                + "\\n\\n 小程序-视频门诊总金额 : " + str2double(result_data.get(13)[7])
//                + "\\n\\n 小程序公众号-免费咨询打赏悬赏总额 : " + str2double(result_data.get(13)[8])
//                + "\"\n}, \n\"msgtype\": \"actionCard\"\n}";

        String str_toast_yulian = "{\n    \"actionCard\": {\n        \"title\": \"智能医生-每日数据报表\", \n         \"text\": \"![screenshot](https://pub.120askimages.com/kswys/newbgnew.png) \\n #### 智能医生"
                + date + "总数据概览 "
                +"\\n\\n 总收入 : "+(caiwu_profit)+" || "
                +(str2double(result_caiwu_22Ja.get(0).get(9))) + compareValueD(str2double(result_caiwu_22Ja.get(0).get(9)), str2double(result_caiwu_22Ja.get(1).get(9)))
                + " || "+(wx_live+zfb_live+app_kyy_live)
                + " || "+(wx_new+app_kyy_new+zfb_new)

                //1
                + "\\n\\n 指定咨询（不含诊室） : " +str2double((result_data.get(2))[0]) + compareValueD((result_data.get(2))[0], (result_data.get(2))[2])
                + " || "
                + (result_data.get(2))[1]

                //2
                + "\\n\\n 极速咨询 : " +str2double((result_data.get(4))[0]) + compareValueD((result_data.get(4))[0], (result_data.get(4))[2])+ " || "+ (result_data.get(4))[1]

                //3
                + "\\n\\n 免费咨询 : " +str2double((result_data.get(1))[0]) + compareValueD((result_data.get(1))[0], (result_data.get(1))[3])
                + " || "
                + (result_data.get(1))[1]
                + " || "
                + (result_data.get(1))[2]

                //4
                + "\\n\\n 新收入 : " +str2double((result_data.get(3))[0]) + compareValueD((result_data.get(3))[0], (result_data.get(3))[5])
                + " || "
                + (result_data.get(3))[2] + " / " + (result_data.get(3))[3] + " / " + (result_data.get(3))[4] + " / " + (result_data.get(3))[5]

                //5
                + "\\n\\n 微信 : "+(result_team_22Ja.get(0).get(4))+" || " +result_team_22Ja.get(0).get(9) + compareValueD(result_team_22Ja.get(0).get(9), result_team_22Ja.get(1).get(9))
                + " || "
                + wx_live

                + " || "
                + wx_new

                //6
                + "\\n\\n 支付宝 : "+(result_team_22Ja.get(0).get(5))+" || " +(zfb_money + compareValueD(zfb_money, zfb_money_last))
                + " || " +zfb_live
                + " || " +zfb_new

                //7
                + "\\n\\n App+快应用 : "+(result_team_22Ja.get(0).get(1))+" || " +result_team_22Ja.get(0).get(6) + compareValueD(result_team_22Ja.get(0).get(6), result_team_22Ja.get(1).get(6))
                + " || " +app_kyy_live
                + " || " +app_kyy_new
                //8
                + "\\n\\n 医生诊室 : "+(result_team_22Ja.get(0).get(2))+" || " +result_team_22Ja.get(0).get(7) + compareValueD(result_team_22Ja.get(0).get(7), result_team_22Ja.get(1).get(7))
                + " || " +result_data.get(10)[10]
                + " || " +result_data.get(8)[4]
                + "\"\n}, \n\"msgtype\": \"actionCard\"\n}";


        String str_toast_yunying_appkyy="{\n    \"actionCard\": {\n        \"title\": \"智能医生-考核明细\", \n         \"text\": \"![screenshot](https://pub.120askimages.com/kswys/newbgnew.png) \\n #### 智能医生"
                + date + "考核数据概览"
                + "\\n\\n APP快应用收入 : " + result_team_22Ja.get(0).get(6)
                + "\\n\\n 免费咨询 : " + str2double(result_data.get(11)[0])
                + "\\n\\n app指定咨询 : " + str2double(result_data.get(11)[1])
                + "\\n\\n app私人医生 : " + str2double(result_data.get(11)[2])
                + "\\n\\n app视频门诊 : " + str2double(result_data.get(11)[3])
                + "\\n\\n app极速咨询 : " + str2double(result_data.get(11)[4])
                + "\\n\\n 快应用指定咨询 : " + str2double(result_data.get(11)[5])
                + "\\n\\n 快应用私人医生 : " + str2double(result_data.get(11)[6])
                + "\\n\\n 快应用视频门诊 : " + str2double(result_data.get(11)[7])
                + "\\n\\n 快应用急速咨询 : " + str2double(result_data.get(11)[8])
                + "\\n\\n 日活 : ("+(Integer.parseInt(umeng_data.get(0).getLiveumengakswys())+Integer.parseInt(umeng_data.get(0).getLiveumengikswys())+Integer.parseInt(umeng_data.get(0).getLivekyy()))+") " + umeng_data.get(0).getLiveumengakswys()+"｜"+umeng_data.get(0).getLiveumengikswys()+"｜"+umeng_data.get(0).getLivekyy()
                + "\\n\\n 新增 : ("+(Integer.parseInt(umeng_data.get(0).getNewumengakswys())+Integer.parseInt(umeng_data.get(0).getNewumengikswys())+Integer.parseInt(umeng_data.get(0).getNewkyy()))+") " + umeng_data.get(0).getNewumengakswys()+"｜"+umeng_data.get(0).getNewumengikswys()+"｜"+umeng_data.get(0).getNewkyy()
                + "\\n\\n 咨询外收入 : " + str2double(result_data.get(3)[0])
                + "\"\n}, \n\"msgtype\": \"actionCard\"\n}";
//        String temp_zx_income=(Double.parseDouble(result_caiwu_22Ja.get(0).get(0))+Double.parseDouble(result_caiwu_22Ja.get(0).get(6))+Double.parseDouble(result_caiwu_22Ja.get(0).get(7))+Double.parseDouble(result_caiwu_22Ja.get(0).get(8)))+"";
//        String str_toast_caiwu="{\n    \"actionCard\": {\n        \"title\": \"财务报表\", \n         \"text\": \"![screenshot](https://pub.120askimages.com/kswys/newbgnew.png) \\n"
//                + date + "预估利润 "
//                +"\\n\\n 预估利润 : " + str2double(result_caiwu_22Ja.get(0).get(2))
//                +"\\n\\n 预估毛利 : " + caiwu_profit
//                + "\\n\\n 咨询收入 : " + str2double(result_caiwu_22Ja.get(0).get(9))
//                + "\\n\\n 药品收入 : " + str2double(result_caiwu_22Ja.get(0).get(1))
//                + "\\n\\n 广告收入 : 0"
//                + "\\n\\n 其他收入 : 0"
//                + "\\n\\n 医生分摊 : " + str2double(result_caiwu_22Ja.get(0).get(10))
//                + "\\n\\n 办公管理 : " + str2double(result_caiwu_22Ja.get(0).get(5))
//                + "\"\n}, \n\"msgtype\": \"actionCard\"\n}";

        try {
            /**
             * local
             */

//            String result_test = HttpTransfer.postJson("https://oapi.dingtalk.com/robot/send?access_token=6530b1223c098687570ce597df78aaf0882a2798609f0048e3214d43ede40af9",str_toast);

            /**
             * online
             */
//            String result_yunying_appkyy=HttpTransfer.postJson("https://oapi.dingtalk.com/robot/send?access_token=309f6450d46d351f7c3dbf2cc76b424436527d4a34e9a160ee8d843cc163a06f",str_toast_yunying_appkyy);
//            String result = HttpTransfer.postJson("https://oapi.dingtalk.com/robot/send?access_token=7d917e74bf7d9dcb1877485a195f20718ba163c7ed003c33973a93a3023aee6b", str_toast);
//            String result_it = HttpTransfer.postJson("https://oapi.dingtalk.com/robot/send?access_token=7fd8ad440cdd8590245cf791afcab463e0215eb47dd4884b8b1b727908927d3b", str_toast_itman);
//            String result_yunying=HttpTransfer.postJson("https://oapi.dingtalk.com/robot/send?access_token=bd87f77b06cc7ee0b86f2dff86eea8ef40744c3c439fbfeeb6128fb896e91221",str_toast_yulian);
//            String result_ufo = HttpTransfer.postJson("https://oapi.dingtalk.com/robot/send?access_token=012944487d24948c18e603fb64890c16884a59be7473366ae7940ff5c1d3cc7d",str_toast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String compareValue(String num1, String num2) {
        String result = "";
        int new1 = Integer.parseInt(num1);
        int new2 = Integer.parseInt(num2);
        if (new1 > new2) {
            result = "↑";
        }
        if (new1 < new2) {
            result = "↓";
        }
        if (new1 == new2) {
            result = "";
        }
        return result;
    }

    private String compareValueD(String num1, String num2) {
        String result = "";
        double money1 = Math.round(Double.valueOf(num1).doubleValue());
        double money2 = Math.round(Double.valueOf(num2).doubleValue());
        if (money1 > money2) {
            result = "↑";
        }
        if (money1 < money2) {
            result = "↓";
        }
        if (money1 == money2) {
            result = "";
        }
        return result;
    }

    public void makeDToast_text(String phoneNum, String message) {
        String str_toast = "{\n     \"msgtype\": \"text\",\n     \"text\": {\n         \"content\": \"@" + phoneNum + "  通知信息：" + message + "\"\n     },\n     \"at\": {\n         \"atMobiles\": [\n             \"" + phoneNum + "\"\n         ], \n         \"isAtAll\": false\n     }\n }";
        try {
            String result = HttpTransfer.postJson("https://oapi.dingtalk.com/robot/send?access_token=6530b1223c098687570ce597df78aaf0882a2798609f0048e3214d43ede40af9", str_toast);
            if (JSONObject.fromObject(result).getString("errmsg").equals("ok")) {
                System.out.println("DingTalk toast  send done");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nakeDToast_wx(){

    }

    private String[] makeAPPdata(ArrayList<KPIUmeng> umenglist) {
        String[] ret = new String[umenglist.size() * 4];
        for (int i = 0; i < umenglist.size(); i++) {
            ret[(4 * i)] = umenglist.get(i).getLiveumengakswys();
            ret[(4 * i + 1)] = umenglist.get(i).getLiveumengikswys();
            ret[(4 * i + 2)] = umenglist.get(i).getNewumengakswys();
            ret[(4 * i + 3)] = umenglist.get(i).getNewumengikswys();
        }
        return ret;
    }

    private String[] makeZfbData(ArrayList<KPIManual> manuallist,ArrayList<ArrayList<String>> result_team_22Ja){
        String[] ret = new String[manuallist.size()*3];
        for (int i = 0; i < manuallist.size(); i++) {
            ret[(3 * i)] = result_team_22Ja.get(i).get(10);
            ret[(3 * i + 1)] = manuallist.get(i).getZfblive();
            ret[(3 * i + 2)] = manuallist.get(i).getZfbreuser();
        }
        return ret;
    }

    private String[] makeNewWXInCome(ArrayList<ArrayList<String>> result){
        String[] ret = new String[result.size()*15];
        for (int i = 0; i < result.size(); i++) {
            ret[(15 * i)] = result.get(i).get(0);
            ret[(15 * i + 1)] = result.get(i).get(1);
            ret[(15 * i + 2)] = result.get(i).get(2);
            ret[(15 * i + 3)] = result.get(i).get(3);
            ret[(15 * i + 4)] = result.get(i).get(4);
            ret[(15 * i + 5)] = result.get(i).get(5);
            ret[(15 * i + 6)] = result.get(i).get(6);
            ret[(15 * i + 7)] = result.get(i).get(7);
            ret[(15 * i + 8)] = result.get(i).get(8);
            ret[(15 * i + 9)] = result.get(i).get(9);
            ret[(15 * i + 10)] = result.get(i).get(10);
            ret[(15 * i + 11)] = result.get(i).get(11);
            ret[(15 * i + 12)] = result.get(i).get(12);
            ret[(15 * i + 13)] = result.get(i).get(13);
            ret[(15 * i + 14)] = result.get(i).get(14);
        }
        return ret;
    }

    private String[] makeAppKyyInCome(ArrayList<ArrayList<String>> result){
        String[] ret = new String[result.size()*9];
        for (int i = 0; i < result.size(); i++) {
            ret[(9 * i)] = result.get(i).get(0);
            ret[(9 * i + 1)] = result.get(i).get(1);
            ret[(9 * i + 2)] = result.get(i).get(2);
            ret[(9 * i + 3)] = result.get(i).get(3);
            ret[(9 * i + 4)] = result.get(i).get(4);
            ret[(9 * i + 5)] = result.get(i).get(5);
            ret[(9 * i + 6)] = result.get(i).get(6);
            ret[(9 * i + 7)] = result.get(i).get(7);
            ret[(9 * i + 8)] = result.get(i).get(8);
        }
        return ret;
    }

    private String[] makeWXdata(ArrayList<ArrayList<String>> wx_result, ArrayList<KPIManual> wx_live) {
        String[] ret = new String[wx_result.size() * 5];
        for (int i = 0; i < wx_result.size(); i++) {
            ret[(5 * i)] = wx_live.get(i).getLivewxkswys();
            ret[(5 * i + 1)] =  wx_live.get(i).getLivewxwys();
            ret[(5 * i + 2)] = wx_live.get(i).getLivekwjk();
            int new_kswys = NetGPData.getNumWX("list", "new_user", (String) ((ArrayList) wx_result.get(i)).get(0));
            int new_wys = NetGPData.getNumWX("list", "new_user", (String) ((ArrayList) wx_result.get(i)).get(1));
            ret[(5 * i + 3)] = (new_kswys + "");
            ret[(5 * i + 4)] = (new_wys + "");
        }
        return ret;
    }

    private String[] makeAPPWXtotal(String[] app_data, String[] wx_data) {
        String[] ret = new String[app_data.length / 2];
        for (int i = 0; i < 3; i++) {
            ret[(2 * i)] = (Integer.parseInt(app_data[(4 * i)])
                    + Integer.parseInt(app_data[(4 * i + 1)])
                    + Integer.parseInt(wx_data[(5 * i)].length()==0?"0":wx_data[(5 * i)])
                    + Integer.parseInt(wx_data[(5 * i + 1)].length()==0?"0":wx_data[(5 * i+1)])
                    + Integer.parseInt(wx_data[(5 * i + 2)].length()==0?"0":wx_data[(5 * i+2)]) + "");

            ret[(2 * i + 1)] = (Integer.parseInt(app_data[(4 * i + 2)])
                    + Integer.parseInt(app_data[(4 * i + 3)])
                    + Integer.parseInt(wx_data[(5 * i + 3)].length()==0?"0":wx_data[(5 * i+3)])
                    + Integer.parseInt(wx_data[(5 * i + 4)].length()==0?"0":wx_data[(5 * i+4)]) + "");
        }
        return ret;
    }

    private String[] makeUVdata(ArrayList<ArrayList<String>> uv_result, ArrayList<KPIManual> manual_list, ArrayList<KPIUmeng> umeng_list) {
        String[] ret = new String[uv_result.size() * 11];
        for (int i = 0; i < uv_result.size(); i++) {
            // 友盟UV总数据 0
            ret[(i * 11)] = (Integer.parseInt(umeng_list.get(i).getUvwebm()) + Integer.parseInt(umeng_list.get(i).getUvwebpc())) + "";
            // 小程序快应用新增 废弃
            ret[(i * 11 + 1)] = (Integer.parseInt(umeng_list.get(i).getNewkyy())
                    + Integer.parseInt(umeng_list.get(i).getNewxcxkswys())
                    + Integer.parseInt(umeng_list.get(i).getNewxcxkswy())
                    + Integer.parseInt(umeng_list.get(i).getNewxcxkpy())
                    + Integer.parseInt(umeng_list.get(i).getNewxcxzfb()))
                    +"";
            // 医生推荐订单 2
            ret[(i * 11 + 2)] = uv_result.get(i).get(4);
            //指定咨询退款率 废弃
            ret[(i * 11 + 3)] = uv_result.get(i).get(5);
            //医生奖励数 4
            ret[(i * 11 + 4)] = uv_result.get(i).get(6);
            //小程序+快应用日活 废弃
            ret[(i * 11 + 5)] = (Integer.parseInt(umeng_list.get(i).getLivekyy())
                    +Integer.parseInt(umeng_list.get(i).getLivexcxkswys())
                    +Integer.parseInt(umeng_list.get(i).getLivexcxkswy())
                    +Integer.parseInt(umeng_list.get(i).getLivexcxkpy())
                    +Integer.parseInt(umeng_list.get(i).getLivexcxzfb()))
                    + "";
            //极速咨询复购 废弃
            ret[(i * 11 + 6)] = (Integer.parseInt(uv_result.get(i).get(11))+Integer.parseInt(uv_result.get(i).get(12)))+"";
            //处方数 7
            ret[(i * 11 + 7)] =uv_result.get(i).get(13);
            //免费咨询数 废弃
            ret[(i * 11 + 8)] =uv_result.get(i).get(14);
            //hzbd_pay_orders
            ret[(i * 11 + 9)] =uv_result.get(i).get(19);
            //hzbd 10
            ret[(i * 11 + 10)] =uv_result.get(i).get(18);

        }
        return ret;
    }


    private String[] income_total(ArrayList<ArrayList<String>> result) {
        String[] arr_result = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            BigDecimal total = new BigDecimal(0.00);
            for (int j = 0; j < result.get(i).size(); j++) {
                if (j == 12) {
                    total = total.subtract(new BigDecimal(Double.valueOf((result.get(i)).get(12))));
                } else {
                    double item = Double.valueOf((result.get(i)).get(j));
                    total = total.add(new BigDecimal(item));
                }
            }
            arr_result[i] = (total.doubleValue() + "");
        }
        return arr_result;
    }

    private String[] income_free(ArrayList<ArrayList<String>> result) {
        String[] arr_result = new String[result.size() * 4];
        for (int i = 0; i < result.size(); i++) {
            BigDecimal total = new BigDecimal(0.00);
            total = total.add(new BigDecimal(Double.valueOf(result.get(i).get(0))))
                    .add(new BigDecimal(Double.valueOf(result.get(i).get(1))))
                    .add(new BigDecimal(Double.valueOf(result.get(i).get(2))));
            arr_result[(4 * i)] = total.doubleValue() + "";
            arr_result[(4 * i + 1)] = ((String) ((ArrayList) result.get(i)).get(3));
            arr_result[(4 * i + 2)] = ((String) ((ArrayList) result.get(i)).get(4));
            arr_result[(4 * i + 3)] = ((String) ((ArrayList) result.get(i)).get(5));
        }
        return arr_result;
    }

    private String[] income_pay(ArrayList<ArrayList<String>> result) {
        String[] arr_result = new String[result.size() * 2];
        for (int i = 0; i < result.size(); i++) {
            BigDecimal total_money = new BigDecimal(0.00);
            int total_num = 0;
            for (int j = 0; j < 7; j++) {
                total_money = total_money.add(new BigDecimal(Double.valueOf(result.get(i).get(j))));
            }
            for (int j = 7; j < 11; j++) {
                total_num += Integer.parseInt(result.get(i).get(j));
            }
            arr_result[(2 * i)] = (total_money.doubleValue() + "");
            arr_result[(2 * i + 1)] = (total_num + "");
        }
        return arr_result;
    }

    private String[] income_other(ArrayList<ArrayList<String>> result, ArrayList<String> dates) {
        String[] arr_result = new String[result.size() * 6];
        for (int i = 0; i < result.size(); i++) {
            BigDecimal total_money = new BigDecimal(0.00);

            total_money = total_money.add(new BigDecimal(Double.valueOf(result.get(i).get(0))))
                    .add(new BigDecimal(Double.valueOf(result.get(i).get(1))))
                    .add(new BigDecimal(Double.valueOf(result.get(i).get(2))))
                    .add(new BigDecimal(Double.valueOf(result.get(i).get(3))))
                    .add(new BigDecimal(Double.valueOf(result.get(i).get(4))));

            arr_result[(6 * i)] = (total_money.doubleValue() + "");
            arr_result[(6 * i + 1)] = result.get(i).get(5);
            arr_result[(6 * i + 2)] = result.get(i).get(6);
            arr_result[(6 * i + 3)] = result.get(i).get(7);
            arr_result[(6 * i + 4)] = result.get(i).get(8);
            arr_result[(6 * i + 5)] = result.get(i).get(9);
        }
        return arr_result;
    }

    private String[] income_jszx(ArrayList<ArrayList<String>> result) {
        String[] arr_result = new String[result.size() * 2];
        for (int i = 0; i < result.size(); i++) {
            arr_result[(2 * i)] = result.get(i).get(0);
            arr_result[(2 * i + 1)] = result.get(i).get(1);
        }
        return arr_result;
    }
}
