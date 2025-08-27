package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.ToastContentMapper;
import com.jky.znys.team_kpi.entity.KPIToastContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ToastContentService {
    @Autowired
    private ToastContentMapper toastContentMapper;

    public void addData(KPIToastContent item){
        toastContentMapper.addData(item);
    }

    public void upDateData(KPIToastContent item){
        toastContentMapper.upDateData(item);
    }

    public int getCount(String udate){
         return toastContentMapper.getCount(udate);
    }

    public ArrayList<KPIToastContent> getDataList(String startTime, String endTime){
        return toastContentMapper.getDataList(startTime,endTime);
    }
}
