package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.ManualMapper;
import com.jky.znys.team_kpi.entity.KPIManual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ManualService {
    @Autowired
    private ManualMapper manualMapper;

    public void addData(KPIManual item){
        manualMapper.addData(item);
    }

    public void upDateData(KPIManual item){
        manualMapper.upDateData(item);
    }

    public int getCountUdate(String udate){
        return manualMapper.getCountUdate(udate);
    }

    public ArrayList<KPIManual> getDataList(String startTime, String endTime){
        return manualMapper.getDataList(startTime,endTime);
    }
}
