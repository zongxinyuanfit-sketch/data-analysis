package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIRequestParma;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface RequestParmaMapper {
    public ArrayList<KPIRequestParma> getDataList();
}
