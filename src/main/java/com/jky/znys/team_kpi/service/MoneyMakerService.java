package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.MoneyMakerMapper;
import com.jky.znys.team_kpi.entity.KPITotalMoneyDay;
import com.jky.znys.team_kpi.entity.KPITotalMoneyMonth;
import com.jky.znys.team_kpi.entity.KPITotalMoneyWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MoneyMakerService {
    @Autowired
    private MoneyMakerMapper moneyMakerMapper;

    public ArrayList<KPITotalMoneyDay> getTotalMoneyDay(String start, String end){
        return moneyMakerMapper.getTotalMoneyDay(start,end);
    }
    public void updateTotalMoneyDay(String money,String udate){
        moneyMakerMapper.updateTotalMoneyDay(money,udate);
    }
    public void insertTotalMoneyDay(String money,String udate){
        moneyMakerMapper.insertTotalMoneyDay(money, udate);
    }
    public int getCountUdate(String udate){
        return moneyMakerMapper.getCountUdate(udate);
    }
    public KPITotalMoneyWeek getTotalMoneyWeek(String udate){
        return moneyMakerMapper.getTotalMoneyWeek(udate);
    }
    public void updateTotalMoneyWeek(KPITotalMoneyWeek item){
        moneyMakerMapper.updateTotalMoneyWeek(item);
    }
    public KPITotalMoneyMonth getTotalMoneyMonthById(int id){
        return moneyMakerMapper.getTotalMoneyMonthById(id);
    }
    public ArrayList<KPITotalMoneyMonth> getTotalMoneyMonth(){
        return moneyMakerMapper.getTotalMoneyMonth();
    }
    public void updateTotalMoneyMonth(KPITotalMoneyMonth item){
        moneyMakerMapper.updateTotalMoneyMonth(item);
    }
}
