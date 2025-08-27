package com.jky.znys.team_kpi.dao;

import com.jky.znys.team_kpi.entity.KPIRadix;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface RadixMapper {
    public void addData(KPIRadix item);

    public ArrayList<KPIRadix> getDataList();
}
