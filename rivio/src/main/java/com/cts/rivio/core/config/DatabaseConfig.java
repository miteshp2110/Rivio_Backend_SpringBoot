package com.cts.rivio.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    // ==========================================
    // 1. PRIMARY DATA SOURCE (For normal APIs)
    // ==========================================

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        // Because we changed 'url' to 'jdbcUrl' in properties, HikariCP will accept this perfectly!
        return DataSourceBuilder.create().build();
    }

    // ==========================================
    // 2. SECONDARY DATA SOURCE (For the Chatbot)
    // ==========================================

    @Value("${chatbot.datasource.url}")
    private String chatbotUrl;
    @Value("${chatbot.datasource.username}")
    private String chatbotUsername;
    @Value("${chatbot.datasource.password}")
    private String chatbotPassword;

    @Bean(name = "readOnlyDataSource")
    public DataSource readOnlyDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // DriverManagerDataSource uses setUrl(), so chatbot.datasource.url works fine here
        dataSource.setUrl(chatbotUrl);
        dataSource.setUsername(chatbotUsername);
        dataSource.setPassword(chatbotPassword);
        return dataSource;
    }

    @Bean(name = "readOnlyJdbcTemplate")
    public JdbcTemplate readOnlyJdbcTemplate(@Qualifier("readOnlyDataSource") DataSource readOnlyDataSource) {
        return new JdbcTemplate(readOnlyDataSource);
    }
}