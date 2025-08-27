package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.CategoryMapper;
import com.jky.znys.team_kpi.entity.KPICategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public ArrayList<KPICategory> getCategoryList(){
        return categoryMapper.getCategoryList();
    }
}
