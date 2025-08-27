package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIDescribes;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface DescribesMapper {
    public ArrayList<KPIDescribes> getDescribeList();
}
