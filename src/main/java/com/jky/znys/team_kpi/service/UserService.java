package com.jky.znys.team_kpi.service;

import com.jky.znys.team_kpi.dao.UserMapper;
import com.jky.znys.team_kpi.entity.KPIUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public ArrayList<KPIUser> getUserList(){
        return userMapper.getUserList();
    }
}
