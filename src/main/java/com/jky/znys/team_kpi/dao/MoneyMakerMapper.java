package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPITotalMoneyDay;
import com.jky.znys.team_kpi.entity.KPITotalMoneyMonth;
import com.jky.znys.team_kpi.entity.KPITotalMoneyWeek;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface MoneyMakerMapper {
    public ArrayList<KPITotalMoneyDay> getTotalMoneyDay(@Param("start") String start, @Param("end") String end);
    public void insertTotalMoneyDay(@Param("money") String money,@Param("udate") String udate);
    public void updateTotalMoneyDay(@Param("money") String money,@Param("udate") String udate);
    public int getCountUdate(@Param("udate") String udate);
    public KPITotalMoneyWeek getTotalMoneyWeek(@Param("udate") String udate);
    public void updateTotalMoneyWeek(KPITotalMoneyWeek item);
    public KPITotalMoneyMonth getTotalMoneyMonthById(@Param("id") int id);
    public ArrayList<KPITotalMoneyMonth> getTotalMoneyMonth();
    public void updateTotalMoneyMonth(KPITotalMoneyMonth item);
}
