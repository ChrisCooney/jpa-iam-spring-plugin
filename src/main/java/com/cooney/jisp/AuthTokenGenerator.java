package com.cooney.jisp;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;

public class AuthTokenGenerator {

    private String rdsDatabaseUser;
    private String rdsRegionName;
    private String rdsInstanceHostName;
    private int rdsInstancePort;

    private RdsIamAuthTokenGenerator tokenGenerator;

    public AuthTokenGenerator(
            String rdsDatabaseUser,
            String rdsRegionName,
            String rdsInstanceHostName,
            int rdsInstancePort
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
