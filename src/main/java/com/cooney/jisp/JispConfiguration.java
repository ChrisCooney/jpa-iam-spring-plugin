package com.cooney.jisp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JispConfiguration {

    private String rdsDatabaseUser;
    private String rdsRegionName;
    private String jdbcUrl;
    private String rdsInstanceHostName;
    private String driverClassName;
    private String rdsInstancePort;


    @Autowired
    public JispConfiguration(
            @Value("${rds.auth.db-user}") String rdsDatabaseUser,
            @Value("${rds.auth.region}") String rdsRegionName,
            @Value("${rds.auth.db-host}") String rdsInstanceHostName,
            @Value("${rds.auth.db-port}") String rdsInstancePort,
            @Value("${spring.datasource.url}") String jdbcUrl,
            @Value("${spring.datasource.driver-class-name}") String driverClassName) {

        this.rdsDatabaseUser = rdsDatabaseUser;
        this.rdsRegionName = rdsRegionName;
        this.jdbcUrl = jdbcUrl;
        this.rdsInstanceHostName = rdsInstanceHostName;
        this.rdsInstancePort = rdsInstancePort;
        this.driverClassName = driverClassName;
    }

    @Bean
    public DataSource dataSource() {
        String authToken = rdsAuthTokenGenerator().generateAuthToken();

        return DataSourceBuilder
                .create()
                .username(rdsDatabaseUser)
                .url(jdbcUrl)
                .password(authToken)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean
    public AuthTokenGenerator rdsAuthTokenGenerator() {
        return new AuthTokenGenerator(
                rdsDatabaseUser,
                rdsRegionName,
                rdsInstanceHostName,
                Integer.valueOf(rdsInstancePort)
        );
    }

}
