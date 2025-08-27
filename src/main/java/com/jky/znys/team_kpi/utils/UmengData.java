package com.jky.znys.team_kpi.utils;

import com.jky.znys.team_kpi.entity.KPIRequestParma;
import com.jky.znys.team_kpi.entity.KPIUmeng;
import com.jky.znys.team_kpi.service.RequestParmaService;
import com.jky.znys.team_kpi.service.UMengService;
import org.springframework.beans.factory.annotation.Autowired;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Component
public class UmengData {
    @Autowired
    private RequestParmaService requestParmaService;
    @Autowired
    private UMengService uMengService;

    ToastDingTalk tool_toast = new ToastDingTalk();

    @Scheduled(cron = "0 47 9 ? * *")
    public void MakeUmengdata() {
        System.out.print("MakeUmengdataIng");
        KPIUmeng t_um_data = new KPIUmeng();
        String webDate ="";
        try {
            webDate = NetGPData.plusDay(-1, NetGPData.getNowDate());
            // 获取收入数据，来自大山哥
            String str_income_s = HttpTransfer.DTsendGet("https://ztc.120.net/api/kwcount?", "day=" + webDate);
            // 获取友盟数据链接及其参数
            ArrayList<KPIRequestParma> t_rp = requestParmaService.getDataList();
            JSONObject temp_parma_pachong_kswy = JSONObject.fromObject(t_rp.get(4).getParmas());
            JSONObject temp_parma_pachong_kswys = JSONObject.fromObject(t_rp.get(5).getParmas());
            JSONObject temp_parma_pachong_kpy = JSONObject.fromObject(t_rp.get(6).getParmas());
            temp_parma_pachong_kswy.put("fromDate", webDate);
            temp_parma_pachong_kswy.put("toDate", webDate);
            temp_parma_pachong_kswys.put("fromDate", webDate);
            temp_parma_pachong_kswys.put("toDate", webDate);
            temp_parma_pachong_kpy.put("fromDate", webDate);
            temp_parma_pachong_kpy.put("toDate", webDate);
            // 获取umeng部分数据 app、小程序、web 三端的新增与活跃 微信爬虫数据
            String result_live_new_app = NetGPData.getDataBycookie(t_rp.get(0).getCookies(), t_rp.get(0).getDataurl(), 1, "", "",t_rp.get(0).getWxtoken(),0);
            System.out.println("result_live_new_app-----------------------"+result_live_new_app);
            String result_live_new_mp = NetGPData.getDataBycookie(t_rp.get(1).getCookies(), t_rp.get(1).getDataurl(), 2, t_rp.get(1).getReferer(), t_rp.get(1).getParmas(),t_rp.get(1).getWxtoken(),1);
            UrlCollector.umeng_miniapp =result_live_new_mp;
            String result_pachong_kswy = NetGPData.getDataBycookie(t_rp.get(4).getCookies(), t_rp.get(4).getDataurl(), 2, t_rp.get(1).getReferer(), temp_parma_pachong_kswy.toString(),t_rp.get(4).getWxtoken(),1);
            String result_pachong_kswys = NetGPData.getDataBycookie(t_rp.get(5).getCookies(), t_rp.get(5).getDataurl(), 2, t_rp.get(1).getReferer(), temp_parma_pachong_kswys.toString(),t_rp.get(5).getWxtoken(),1);
            String result_pachong_kpy = NetGPData.getDataBycookie(t_rp.get(6).getCookies(), t_rp.get(6).getDataurl(), 2, t_rp.get(1).getReferer(), temp_parma_pachong_kpy.toString(),t_rp.get(6).getWxtoken(),1);
            UrlCollector.umeng_wx_pc_kswy = result_pachong_kswy;
            UrlCollector.umeng_wx_pc_kswys = result_pachong_kswys;
            UrlCollector.umeng_wx_pc_kpy = result_pachong_kpy;

            String baidu_payload = "indicators=pv_count,visitor_count,ip_count,bounce_ratio,avg_visit_time&method=homepage/getSitesInfoList";
            String result_baidu_m=NetGPData.getDataBycookie(t_rp.get(2).getCookies(), t_rp.get(2).getDataurl(), 3, t_rp.get(2).getReferer(), baidu_payload,t_rp.get(2).getWxtoken(),1);
            t_um_data = parse_data_success(str_income_s, result_live_new_app, result_live_new_mp, result_pachong_kswy, result_pachong_kswys,result_pachong_kpy,result_baidu_m);
        } catch (Exception e) {
            tool_toast.makeDToast_text("15015962016", "获取友盟数据异常，请检查cookie是否过期。");
            t_um_data = parse_data_fail();
        }
        // 保存数据或者更新数据
        t_um_data.setDate(webDate);
        if (uMengService.getCount(t_um_data.getDate()) > 0) {
            uMengService.upDateData(t_um_data);
        } else {
            uMengService.addData(t_um_data);
        }
        UrlCollector.webDate = "";
    }

    private KPIUmeng parse_data_success(String str_income_s, String result_live_new_app, String result_live_new_mp, String result_pachong_kswy, String result_pachong_kswys,String result_pachong_kpy,String result_baidu_m) {
        KPIUmeng t_um_data = new KPIUmeng();
        ///////////////////////////////////////////////////////////////////////////////////////
        // str转jsonObject
        JSONObject f_make_result_app = JSONObject.fromObject(result_live_new_app);
        JSONObject f_make_result_mp = JSONObject.fromObject(result_live_new_mp);
        JSONObject f_make_result_pachong_kswy = JSONObject.fromObject(result_pachong_kswy);
        JSONObject f_make_result_pachong_kswys = JSONObject.fromObject(result_pachong_kswys);
        JSONObject f_make_result_pachong_kpy = JSONObject.fromObject(result_pachong_kpy);

        // 小程序部分
        JSONArray s_make_result_mp = f_make_result_mp.getJSONObject("data").getJSONArray("data");

        //快速问医生 5fa10e5a45b2b751a922adfe
        //康普云 5fdaac2bdd2891533921a6ea
        //快速问医 5e0ebc7e8bbd62283d1a4ee3
        //支付宝 5ee486ce978eea081640df4e

        //微信爬虫部分，要被减掉
        JSONArray temp_arr_pachong_kswy = f_make_result_pachong_kswy.getJSONObject("data").getJSONArray("data");
        JSONArray temp_arr_pachong_kswys = f_make_result_pachong_kswys.getJSONObject("data").getJSONArray("data");
        JSONArray temp_arr_pachong_kpy = f_make_result_pachong_kpy.getJSONObject("data").getJSONArray("data");
        //活跃 小程序活跃 //新增 小程序新增
        for(int i=0;i<s_make_result_mp.size();i++){
            if(s_make_result_mp.getJSONObject(i).getString("dataSourceId").equals("5fa10e5a45b2b751a922adfe")){
                t_um_data.setNewxcxkswys((s_make_result_mp.getJSONObject(i).getInt("preNewUser")- getValueByKey(temp_arr_pachong_kswys,"newUser"))+"");
                t_um_data.setLivexcxkswys((s_make_result_mp.getJSONObject(i).getInt("preActiveUser")- getValueByKey(temp_arr_pachong_kswys,"activeUser"))+"");
                t_um_data.setPcxcxkswys(getValueByKey(temp_arr_pachong_kswys,"newUser")+"");
            }
            if(s_make_result_mp.getJSONObject(i).getString("dataSourceId").equals("5e0ebc7e8bbd62283d1a4ee3")){
                t_um_data.setNewxcxkswy((s_make_result_mp.getJSONObject(i).getInt("preNewUser")-getValueByKey(temp_arr_pachong_kswy,"newUser"))+"");
                t_um_data.setLivexcxkswy((s_make_result_mp.getJSONObject(i).getInt("preActiveUser")-getValueByKey(temp_arr_pachong_kswy,"activeUser"))+"");
                t_um_data.setPcxcxkswy(getValueByKey(temp_arr_pachong_kswy,"newUser")+"");
            }
            if(s_make_result_mp.getJSONObject(i).getString("dataSourceId").equals("5fdaac2bdd2891533921a6ea")){
                t_um_data.setNewxcxkpy((s_make_result_mp.getJSONObject(i).getInt("preNewUser")-getValueByKey(temp_arr_pachong_kpy,"newUser"))+"");
                t_um_data.setLivexcxkpy((s_make_result_mp.getJSONObject(i).getInt("preActiveUser")-getValueByKey(temp_arr_pachong_kpy,"activeUser"))+"");
                t_um_data.setPcxcxkpy(getValueByKey(temp_arr_pachong_kpy,"newUser")+"");
            }
            if(s_make_result_mp.getJSONObject(i).getString("dataSourceId").equals("5ee486ce978eea081640df4e")){
                t_um_data.setNewxcxzfb(s_make_result_mp.getJSONObject(i).getString("preNewUser"));
                t_um_data.setLivexcxzfb(s_make_result_mp.getJSONObject(i).getString("preActiveUser"));
            }

        }

        // 赋值web-uv
        JSONArray baidu_arr = ((JSONObject.fromObject(result_baidu_m)).getJSONObject("data")).getJSONArray("items");
        baidu_arr.forEach((item)->{
            if(JSONObject.fromObject(item).get("id").equals(17839604)){
                t_um_data.setUvwebm((((JSONObject.fromObject(item)).getJSONArray("items")).getJSONArray(1)).getString(2));
            }
            if(JSONObject.fromObject(item).get("id").equals(17839592)){
                t_um_data.setUvwebpc((((JSONObject.fromObject(item)).getJSONArray("items")).getJSONArray(1)).getString(2));
            }
        });





        // 赋值app
        JSONArray s_make_result_app = f_make_result_app.getJSONObject("data").getJSONArray("list");
        for(int i =0;i<s_make_result_app.size();i++){
            // Android版快速问医生
            if(s_make_result_app.getJSONObject(i).getString("appkey").equals("4f94e4f252701507f0000004")){
                t_um_data.setLiveumengakswys(s_make_result_app.getJSONObject(i).getString("activeUser"));
                t_um_data.setNewumengakswys(s_make_result_app.getJSONObject(i).getString("newUser"));
            }
            // iPhone版快速问医生
            if(s_make_result_app.getJSONObject(i).getString("appkey").equals("4f974693527015313d00002e")){
                t_um_data.setLiveumengikswys(s_make_result_app.getJSONObject(i).getString("activeUser"));
                t_um_data.setNewumengikswys(s_make_result_app.getJSONObject(i).getString("newUser"));
            }
            // 快速问医生_快应用
            if(s_make_result_app.getJSONObject(i).getString("appkey").equals("5d10234a4ca3576ac30008ae")){
                t_um_data.setLivekyy(s_make_result_app.getJSONObject(i).getString("activeUser"));
                t_um_data.setNewkyy(s_make_result_app.getJSONObject(i).getString("newUser"));
            }
        }

        t_um_data.setDcrecommend(JSONObject.fromObject(str_income_s).getJSONObject("data").getString("doctor_bind_count"));
        t_um_data.setTuidanlv(JSONObject.fromObject(str_income_s).getJSONObject("data").getString("tuidan_sums"));
        ///////////////////////////////////////////////////////////////////////////////////////
        return t_um_data;
    }



    private int getValueByKey(JSONArray arr,String key){
        int result=0;
        for(int i=0;i<arr.size();i++){
            if(arr.getJSONObject(i).getString("id").equals("wx_1129")){
                result= arr.getJSONObject(i).getInt(key);
                break;
            }
        }
        return  result;
    }

    private KPIUmeng parse_data_fail() {
        KPIUmeng t_um_data = new KPIUmeng();
        t_um_data.setLiveumengakswys("0");
        t_um_data.setLiveumengikswys("0");
        t_um_data.setNewumengakswys("0");
        t_um_data.setNewumengikswys("0");
        t_um_data.setLivekyy("0");
        t_um_data.setNewkyy("0");
        t_um_data.setDcrecommend("0");
        t_um_data.setTuidanlv("0");
        t_um_data.setUvwebm("0");
        t_um_data.setUvwebpc("0");
        t_um_data.setNewxcxkswys("0");
        t_um_data.setNewxcxkswy("0");
        t_um_data.setNewxcxkpy("0");
        t_um_data.setNewxcxzfb("0");
        t_um_data.setLivexcxkswys("0");
        t_um_data.setLivexcxkswy("0");
        t_um_data.setLivexcxkpy("0");
        t_um_data.setLivexcxzfb("0");
        t_um_data.setPcxcxkpy("0");
        t_um_data.setPcxcxkswy("0");
        t_um_data.setPcxcxkswys("0");
        return t_um_data;
    }
}
