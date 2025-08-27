package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIManual;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface ManualMapper {
    public void addData(KPIManual item);
    public void upDateData(KPIManual item);
    public int getCountUdate(@Param("udate") String udate);
    public ArrayList<KPIManual> getDataList(@Param("startTime") String startTime, @Param("endTime")String endTime);
}
