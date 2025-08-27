package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.BIMapper;
import com.jky.znys.team_kpi.entity.BI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BIService {
    @Autowired
    private BIMapper biMapper;

    public void addData(BI item){
        biMapper.addData(item);
    }

    public void upDateData(BI item){
        biMapper.upDateData(item);
    }

    public int getCount(String udate){
        return biMapper.getCount(udate);
    }

    public ArrayList<BI> getDataList(String startTime, String endTime){
        return biMapper.getDataList(startTime,endTime);
    }
}
