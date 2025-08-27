package com.jky.znys.team_kpi.controller;

import com.jky.znys.team_kpi.entity.KPIManual;
import com.jky.znys.team_kpi.entity.KPIToastContent;
import com.jky.znys.team_kpi.entity.KPIUmeng;
import com.jky.znys.team_kpi.service.ManualService;
import com.jky.znys.team_kpi.service.ToastContentService;
import com.jky.znys.team_kpi.service.UMengService;
import com.jky.znys.team_kpi.utils.ExportExcel;
import com.jky.znys.team_kpi.utils.NetGPData;
import com.jky.znys.team_kpi.utils.ToastDingTalk;
import com.jky.znys.team_kpi.utils.UrlCollector;
import com.jky.znys.team_kpi.disuse.bidata;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;

@RestController
public class GController {
    @Autowired
    private ManualService manualService;
    @Autowired
    private ToastContentService toastContentService;

    @Autowired
    private UMengService uMengService;

    @RequestMapping("/index")
    public String index() {
        return "indxe";
    }

    @RequestMapping("/AddData")
    @ResponseBody
    public JSONObject AddData(@RequestBody KPIManual item) throws ParseException {
        if(manualService.getCountUdate(item.getUdate())==0) {
            manualService.addData(item);
        }else {
            // 更新数据
            manualService.upDateData(item);
        }

        UrlCollector.webDate = item.getUdate();
        ToastDingTalk t_toast = new ToastDingTalk();
        String ret = JSONArray.fromObject(t_toast.makeToast(false)).toString();
        UrlCollector.toastData = ret;
        KPIToastContent kpiToastContent = new KPIToastContent();
        kpiToastContent.setDate(UrlCollector.webDate);
        kpiToastContent.setContent(ret);
        if(toastContentService.getCount(kpiToastContent.getDate())>0){
            toastContentService.upDateData(kpiToastContent);
        }else {
            toastContentService.addData(kpiToastContent);
        }
        UrlCollector.webDate = "";
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "添加KPI数据成功");
        return json;
    }

    @RequestMapping("/AddUMData")
    @ResponseBody
    public JSONObject AddUMData(@RequestBody KPIUmeng item) throws ParseException {
        if(uMengService.getCount(item.getUdate())==0) {
            uMengService.addData(item);
        }else {
            // 更新数据
            uMengService.upDateData(item);
        }
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "添加友盟数据成功");
        return json;
    }

    @RequestMapping("/GetExcel")
    @ResponseBody
    public String GetExcel(@RequestBody String jsonData) {
        JSONObject jsonOB = JSONObject.fromObject(jsonData);
        ExportExcel t_excel = new ExportExcel();
        return t_excel.getMultidata(jsonOB.getString("starttime"), jsonOB.getString("endtime"), jsonOB.getInt("usetime"));
    }

    @RequestMapping("/GetYLExcel")
    @ResponseBody
    public String GetYLExcel(@RequestBody String jsonData) {
        JSONObject jsonOB = JSONObject.fromObject(jsonData);
        ExportExcel t_excel = new ExportExcel();
        return t_excel.getYLMultidata(jsonOB.getString("starttime"), jsonOB.getString("endtime"), jsonOB.getInt("usetime"));
    }

    @RequestMapping("/GetReport")
    @ResponseBody
    public String GetReport(@RequestBody String jsonData) {
        JSONObject jsonOB = JSONObject.fromObject(jsonData);
        ArrayList<KPIToastContent> t_contents = new ArrayList<KPIToastContent>();
        String temp_date = jsonOB.getString("date");
        String date = "";
        try {
            date = NetGPData.plusDay(-1, NetGPData.getNowDate());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            if (temp_date != null) {
                date = temp_date;
                if (temp_date.split("-")[2].equals("00")) {
                    date = NetGPData.plusDay(-1, NetGPData.getNowDate());
                }
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            t_contents = toastContentService.getDataList(date, date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t_contents.get(t_contents.size() - 1).getContent();
    }

    @RequestMapping("/GetBI")
    @ResponseBody
    public String GetBI(@RequestBody String jsonData) throws UnsupportedEncodingException, ParseException {
        JSONObject jsonOB = JSONObject.fromObject(jsonData);
        String start = jsonOB.getString("start");
        String end = jsonOB.getString("end");
        String btntype = jsonOB.getString("b_type");
        bidata bidata_t = new bidata();
        return bidata_t.bi_data(start,end,btntype);
    }
}
