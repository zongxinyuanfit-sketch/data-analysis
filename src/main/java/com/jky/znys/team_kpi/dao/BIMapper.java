package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.BI;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface BIMapper {
    public void addData(BI item);

    public void upDateData(BI item);

    public int getCount(@Param("udate") String udate);

    public ArrayList<BI> getDataList(@Param("startTime") String startTime, @Param("endTime")String endTime);
}
