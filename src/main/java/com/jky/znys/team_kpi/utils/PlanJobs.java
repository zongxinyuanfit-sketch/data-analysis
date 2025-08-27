package com.jky.znys.team_kpi.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jky.znys.team_kpi.entity.KPIToastContent;
import com.jky.znys.team_kpi.service.ToastContentService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PlanJobs {
    @Autowired
    private ToastContentService toastContentService;
    /**
     * 定时推送消息到钉钉
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void PushToast() {
        try {
            if (isHour() == 7) {
                PushInfo();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void PushInfo() throws UnsupportedEncodingException, ParseException {
        UrlCollector.webDate = NetGPData.plusDay(-1, NetGPData.getNowDate());
        // bidata bidata_t = new bidata(); 废弃
        ToastDingTalk t_toast = new ToastDingTalk();
        // 每日推送数据
        String ret = JSONArray.fromObject(t_toast.makeToast(true)).toString();
        UrlCollector.toastData = ret;
        KPIToastContent t_tc = new KPIToastContent();
        // 日期来源为ToastDingTalk
        t_tc.setDate(UrlCollector.webDate);
        t_tc.setContent(ret);
        if(toastContentService.getCount(t_tc.getDate())>0){
            toastContentService.upDateData(t_tc);
        }else {
            toastContentService.addData(t_tc);
        }
        // bi看板数据 废弃
        // bidata_t.bi_data("","","0");
    }

    //判断具体时间是7点还是9点
    private int isHour() {
        int result = 0;
        try {
            Date temp_date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String temp_time = sdf.format(temp_date);
            if (temp_time.equals("07:30:00")) {
                result = 7;
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        return result;
    }
}
