package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.StatuesMapper;
import com.jky.znys.team_kpi.entity.KPIStatues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StatuesService {
    @Autowired
    private StatuesMapper statuesMapper;

    public ArrayList<KPIStatues> getStatueList(){
        return statuesMapper.getStatueList();
    }
}
