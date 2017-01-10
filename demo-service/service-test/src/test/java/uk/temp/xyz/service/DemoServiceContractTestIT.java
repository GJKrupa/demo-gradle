package uk.temp.xyz.service;

import org.junit.Test;

import static io.restassured.RestAssured.when;


public class DemoServiceContractTestIT{

    @Test
    public void speak_returns_200_with_text_response() {
        String host = System.getProperty("docker-host");
        String port = System.getProperty("docker-port");
        when().
                get("http://"+host+":"+port+"/demo-service/say/hello").
                then().
                statusCode(200);
    }




}