package com.cooney.jisp

import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator
import spock.lang.Specification
import spock.lang.Unroll

import java.security.InvalidParameterException

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
        1 * mockTokenGenerator.getAuthToken(_) >> { GetIamAuthTokenRequest tokenRequest ->
            assert tokenRequest.hostname == 'rds-host'
            assert tokenRequest.port == 1
            assert tokenRequest.userName == 'chris'

            return 'hello'
        }

        and: 'the output is what has been wired from the mock'
        token == 'hello'
    }

    @Unroll
    def "test that the auth token generator validates values: name = #name, region = #region, host = #host, port = #port"() {
        when: "a token generator is created with the input values"
        new AuthTokenGenerator(name, region, host, port)

        then: "the appropriate exception is thrown"
        thrown(exception)

        where:

        name | region | host | port || exception
        'a'  | 'a'    | 'a'  | -1   || InvalidParameterException
        'a'  | 'a'    | null | 1    || InvalidParameterException
        'a'  | 'a'    | null | -1   || InvalidParameterException
        'a'  | null   | 'a'  | 1    || InvalidParameterException
        'a'  | null   | 'a'  | -1   || InvalidParameterException
        'a'  | null   | null | 1    || InvalidParameterException
        'a'  | null   | null | -1   || InvalidParameterException
        null | 'a'    | 'a'  | 1    || InvalidParameterException
        null | 'a'    | 'a'  | -1   || InvalidParameterException
        null | 'a'    | null | 1    || InvalidParameterException
        null | 'a'    | null | -1   || InvalidParameterException
        null | null   | 'a'  | 1    || InvalidParameterException
        null | null   | 'a'  | -1   || InvalidParameterException
        null | null   | null | 1    || InvalidParameterException
        null | null   | null | -1   || InvalidParameterException
    }
}
