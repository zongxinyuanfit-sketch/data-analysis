package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.UMengMapper;
import com.jky.znys.team_kpi.entity.KPIUmeng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UMengService {
    @Autowired
    private UMengMapper uMengMapper;

    public void addData(KPIUmeng item){
        uMengMapper.addData(item);
    }

    public void upDateData(KPIUmeng item){
        uMengMapper.upDateData(item);
    }

    public int getCount(String udate){
        return uMengMapper.getCount(udate);
    }

    public ArrayList<KPIUmeng> getDataList(String startTime, String endTime){
        return uMengMapper.getDataList(startTime,endTime);
    }
}
