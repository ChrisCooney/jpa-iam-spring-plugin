package com.cooney;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenGenerator {

    private String rdsDatabaseUser;
    private String rdsRegionName;
    private String rdsInstanceHostName;
    private int rdsInstancePort;

    private RdsIamAuthTokenGenerator tokenGenerator;

    @Autowired
    public AuthTokenGenerator(
            @Value("rds.database.user") String rdsDatabaseUser,
            @Value("rds.region.name") String rdsRegionName,
            @Value("rds.instance.host") String rdsInstanceHostName,
            @Value("rds.instance.port") int rdsInstancePort
    ) {
        this.rdsDatabaseUser = rdsDatabaseUser;
        this.rdsRegionName = rdsRegionName;
        this.rdsInstanceHostName = rdsInstanceHostName;
        this.rdsInstancePort = rdsInstancePort;

        initTokenGenerator();
    }

    private void initTokenGenerator() {
        this.tokenGenerator = RdsIamAuthTokenGenerator.builder()
                .credentials(new DefaultAWSCredentialsProviderChain())
                .region(rdsRegionName)
                .build();
    }

    public String generateAuthToken() {
        return tokenGenerator.getAuthToken(GetIamAuthTokenRequest
                .builder()
                .hostname(rdsInstanceHostName)
                .port(rdsInstancePort)
                .userName(rdsDatabaseUser)
                .build());
    }

    public void setTokenGenerator(RdsIamAuthTokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }
}
