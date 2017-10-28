package com.cooney.jisp

import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator
import spock.lang.Specification

class AuthTokenGeneratorSpec extends Specification {

    def "test that the auth token generator is able to build out an auth token"() {

        given: 'a mocked out RDS IAM auth token generator'
        def mockTokenGenerator = Mock(RdsIamAuthTokenGenerator)

        and: 'an auth token generator that will use these mocks'
        def tokenGenerator = new AuthTokenGenerator('chris', 'rds-region', 'rds-host', 1)
        tokenGenerator.setTokenGenerator(mockTokenGenerator)

        when: 'the token generator is invoked'
        String token = tokenGenerator.generateAuthToken()

        then: 'get auth token is invoked with the mock auth request'
        1 * mockTokenGenerator.getAuthToken(*_) >> { GetIamAuthTokenRequest tokenRequest ->
            assert tokenRequest.hostname == 'rds-host'
            assert tokenRequest.port == 1
            assert tokenRequest.userName == 'chris'

            return 'hello'
        }

        and: 'the output is what has been wired from the mock'
        token == 'hello'
    }
}
