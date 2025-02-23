package com.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    private ConjurService conjurService;

    @Autowired
    private BasicDataSource dataSource;

    @GetMapping("/refresh-password")
    public String refreshPassword() {
        dataSource.setPassword(conjurService.getDatabasePassword());
        return "Database password refreshed!";
    }
}