package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIUmeng;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface UMengMapper {

    public void addData(KPIUmeng item);

    public void upDateData(KPIUmeng item);

    public int getCount(@Param("udate") String udate);

    public ArrayList<KPIUmeng> getDataList(@Param("startTime") String startTime, @Param("endTime")String endTime);
}
