package com.sparta.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSourceProperties masterataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource masterDataSource() {
        DataSourceProperties property = masterataSourceProperties();
        HikariDataSource dataSource = DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .driverClassName(property.getDriverClassName())
            //.url(property.getUrl())
            .username(property.getUsername())
            .password(property.getPassword())
            .build();
        dataSource.setJdbcUrl(property.getUrl());
        return dataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slaves")
    public Map<String, DataSourceProperties> slaveDataSourceProperties(){
        return new HashMap<>();
    }

    @Bean
    public Map<String, DataSource> slaveDataSource() {
        Map<String, DataSourceProperties> dataSourceProperties = slaveDataSourceProperties();
        Map<String, DataSource> slaveDataSources = new HashMap<>();
        for(String key : dataSourceProperties.keySet()){
            DataSourceProperties properties = dataSourceProperties.get(key);
            System.out.println(properties.getUrl());
            HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(properties.getDriverClassName())
                //.url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
            dataSource.setJdbcUrl(properties.getUrl());
            slaveDataSources.put(key, dataSource);
        }
        return slaveDataSources;
    }

    @Bean
    public DataSource routingDataSource( DataSource masterDataSource, Map<String, DataSource> slaveDataSources) {
        Map<Object, Object> dataSources = new HashMap<>(slaveDataSources);
        dataSources.put("source", masterDataSource);

        List<String> replicaList = new ArrayList<>(slaveDataSources.keySet());

        RoutingDataSource routingDataSource = new RoutingDataSource(replicaList);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        routingDataSource.setTargetDataSources(dataSources);

        return routingDataSource;
    }

    @Primary
    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource(masterDataSource(), slaveDataSource()));
    }
}
