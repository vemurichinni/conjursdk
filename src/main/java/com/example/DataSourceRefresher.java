package com.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataSourceRefresher {

    @Autowired
    private ConjurService conjurService;

    @Autowired
    private BasicDataSource dataSource;

    @Scheduled(fixedRate = 1800000)
    public void refreshDatabaseCredentials() {
        dataSource.setPassword(conjurService.getDatabasePassword());
        System.out.println("Database password refreshed.");
    }
}