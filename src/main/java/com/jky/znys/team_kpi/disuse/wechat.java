package com.jky.znys.team_kpi.disuse;

import com.jky.znys.team_kpi.entity.KPIExcelConfig;
import com.jky.znys.team_kpi.entity.KPIManual;
import com.jky.znys.team_kpi.service.ExcelConfigService;
import com.jky.znys.team_kpi.service.ManualService;
import com.jky.znys.team_kpi.utils.NetGPData;
import com.jky.znys.team_kpi.utils.SpringContextUtils;
import com.jky.znys.team_kpi.utils.UrlCollector;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;

public class wechat {
    @Autowired
    private ExcelConfigService excelConfigService=(ExcelConfigService) SpringContextUtils.getBean(ExcelConfigService.class);
    @Autowired
    private ManualService manualService=(ManualService)SpringContextUtils.getBean(ManualService.class);

    public ArrayList<String> wechat_data(String webDate) {
        String date = webDate;
        ArrayList<KPIExcelConfig> get_excelconfig_result = excelConfigService.getConfigList();
        ArrayList<String> wx_result = new ArrayList<String>();

        String str_useInDecrease_kswys = NetGPData.getWXdata(get_excelconfig_result.get(0).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidkswys(), date);
        String str_useInDecrease_wys = NetGPData.getWXdata(get_excelconfig_result.get(0).getUrlgettoken(), (get_excelconfig_result.get(0)).getUrluseindecrease(), (get_excelconfig_result.get(0)).getAppidwys(), date);
        wx_result.add(str_useInDecrease_kswys);
        wx_result.add(str_useInDecrease_wys);
        return wx_result;
    }

    public ArrayList<KPIManual> get_manual(String webDate){
        String date = webDate;
        ArrayList<KPIManual> manuallist = new ArrayList();
        try {
            manuallist = manualService.getDataList(date, date);
            if(manuallist.size()==0){
                manuallist.add(get_temp(date));
            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            manuallist.add(get_temp(date));
        }
        return manuallist;
    }
    private KPIManual get_temp(String webDate){
        KPIManual item = new KPIManual();
        item.setLivewxkswys("0");
        item.setLivewxwys("0");
        item.setLivekwjk("0");
        item.setMediareadnum("0");
        item.setApplitelive("0");
        item.setUdate(webDate);
        item.setVideopv("0");
        return item;
    }
}
