package com.jky.znys.team_kpi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@SpringBootApplication
@MapperScan("com.jky.znys.team_kpi.dao")
@EnableScheduling
public class TeamKpiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TeamKpiApplication.class, args);
    }

    // 不重写打包war部署到tomcat接口会报404
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TeamKpiApplication.class);
    }
}
