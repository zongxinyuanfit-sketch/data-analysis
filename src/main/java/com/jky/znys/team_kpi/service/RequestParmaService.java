package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.RequestParmaMapper;
import com.jky.znys.team_kpi.entity.KPIRequestParma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RequestParmaService {
    @Autowired
    private RequestParmaMapper requestParmaMapper;

    public ArrayList<KPIRequestParma> getDataList(){
        return requestParmaMapper.getDataList();
    }
}
