package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPICategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface CategoryMapper {
    public ArrayList<KPICategory> getCategoryList();
}
