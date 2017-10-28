package com.cooney.ship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RDSIAMConfiguration {

    private String rdsDatabaseUser;
    private String rdsRegionName;
    private String jdbcUrl;
    private String rdsInstanceHostName;
    private String driverClassName;
    private int rdsInstancePort;


    @Autowired
    public RDSIAMConfiguration(
            @Value("rds.auth.database.user") String rdsDatabaseUser,
            @Value("rds.auth.region.name") String rdsRegionName,
            @Value("rds.auth.instance.host") String rdsInstanceHostName,
            @Value("rds.auth.instance.port") int rdsInstancePort,
            @Value("spring.datasource.url") String jdbcUrl,
            @Value("spring.datasource.driver-class-name") String driverClassName) {

        this.rdsDatabaseUser = rdsDatabaseUser;
        this.rdsRegionName = rdsRegionName;
        this.jdbcUrl = jdbcUrl;
        this.rdsInstanceHostName = rdsInstanceHostName;
        this.rdsInstancePort = rdsInstancePort;
        this.driverClassName = driverClassName;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username(rdsDatabaseUser)
                .url(jdbcUrl)
                .password(rdsAuthTokenGenerator().generateAuthToken())
                .driverClassName(driverClassName)
                .build();
    }

    @Bean
    public AuthTokenGenerator rdsAuthTokenGenerator() {
        return new AuthTokenGenerator(
                rdsDatabaseUser,
                rdsRegionName,
                rdsInstanceHostName,
                rdsInstancePort
        );
    }

}
