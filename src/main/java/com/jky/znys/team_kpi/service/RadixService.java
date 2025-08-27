package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.RadixMapper;
import com.jky.znys.team_kpi.entity.KPIRadix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RadixService {
    @Autowired
    private RadixMapper radixMapper;

    public void addData(KPIRadix item){
        radixMapper.addData(item);
    }

    public ArrayList<KPIRadix> getDataList(){
        return radixMapper.getDataList();
    }
}
