package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIStatues;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StatuesMapper {
    public ArrayList<KPIStatues> getStatueList();
}
