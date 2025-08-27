package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.DescribesMapper;
import com.jky.znys.team_kpi.entity.KPIDescribes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DescribesService {
    @Autowired
    private DescribesMapper describesMapper;

    public ArrayList<KPIDescribes> getDescribeList(){
        return describesMapper.getDescribeList();
    }
}
