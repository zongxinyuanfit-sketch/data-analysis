package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIExcelConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ExcelConfigMapper {
    public ArrayList<KPIExcelConfig> getConfigList();
}
