package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.ExcelConfigMapper;
import com.jky.znys.team_kpi.entity.KPIExcelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ExcelConfigService {
    @Autowired
    private ExcelConfigMapper excelConfigMapper;

    public ArrayList<KPIExcelConfig> getConfigList(){
        return excelConfigMapper.getConfigList();
    }
}
