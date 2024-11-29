package com.luch;


import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Value
import spock.lang.Specification
import spock.lang.Stepwise
import javax.transaction.Transactional

@Integration
@Transactional
@Stepwise
class LoginFunctionalSpec extends Specification {

    String baseUrl;
    OkHttpTestClient okHttpTestClient = new OkHttpTestClient();
    @Value('${local.server.port}')
    Integer serverPort

    void 'test login - successful - jean'() {
        def responseContent;
        String username;
        String password;

        baseUrl = "http://localhost:${serverPort}";
        username = "jean";
        password = "abcd1234";

        when:
        responseContent = okHttpTestClient.initJsonToken(baseUrl, username, password);

        then:
        responseContent.responseCode == 200;
        username?.equalsIgnoreCase(responseContent.responseJson."username");
    }

    void 'test login - secure - jean'() {
        def responseContent;
        String username;
        String password;

        baseUrl = "http://localhost:${serverPort}";
        username = "jean";
        password = "abcd1234";

        when:
        responseContent = okHttpTestClient.initSession(baseUrl, username, password);
        responseContent = okHttpTestClient.get("/index/ok");

        then:
        responseContent.responseCode == 200;
        username?.equalsIgnoreCase(responseContent.responseJson."principal"."username");
    }

    void 'test login - successful - mylene'() {
        def responseContent;
        String username;
        String password;

        baseUrl = "http://localhost:${serverPort}";
        username = "mylene";
        password = "abcd1234";

        when:
        responseContent = okHttpTestClient.initJsonToken(baseUrl, username, password);

        then:
        responseContent.responseCode == 200;
        username?.equalsIgnoreCase(responseContent.responseJson."username");
    }

    void 'test login - bad password'() {
        def responseContent;
        String username;
        String password;
        boolean exceptionOccurred;

        baseUrl = "http://localhost:${serverPort}";
        username = "jean";
        password = "bogus";

        when:
        try {
            responseContent = okHttpTestClient.initJsonToken(baseUrl, username, password);
        } catch (Exception exception) {
            exceptionOccurred = true;
        }

        then:
        exceptionOccurred == true;
    }

}
