package org.example.expert.config;

import org.example.expert.test.TestDataGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TestDataConfig {

    @Bean
    public TestDataGenerator testDataGenerator(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate){
        return new TestDataGenerator(jdbcTemplate, transactionTemplate);
    }
}
