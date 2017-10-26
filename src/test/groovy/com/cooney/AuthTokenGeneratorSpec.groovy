package com.cooney

import spock.lang.Specification

class AuthTokenGeneratorSpec extends Specification {

    def "test that the auth token generator is able to build out an auth token"() {
        given: "an auth token generator"
        def tokenGenerator = new AuthTokenGenerator()

        when: "the token generator is invoked"
        def token = tokenGenerator.generateAuthToken()

        then: "an auth token is generated"
        token == "i am a valid token"
    }

}
