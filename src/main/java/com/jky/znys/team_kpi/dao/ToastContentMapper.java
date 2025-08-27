package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIToastContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface ToastContentMapper {
    public void addData(KPIToastContent item);

    public void upDateData(KPIToastContent item);

    public int getCount(@Param("udate") String udate);

    public ArrayList<KPIToastContent> getDataList(@Param("startTime") String startTime, @Param("endTime")String endTime);
}
