package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import org.apache.camel.test.spring.EnableRouteCoverage;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableRouteCoverage
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingsShouldReturnFallbackMessage() throws Exception {
        Assert.assertEquals( "Hello, jacopo", this.restTemplate.getForObject("http://localhost:" + port + "/camel/greetings/jacopo", Greetings.class).getGreetings());
    }

    @Test
    public void healthShouldReturnOkMessage() throws Exception {
        Assert.assertEquals( "{\"status\":\"UP\"}", this.restTemplate.getForObject("http://localhost:" + port + "/health", String.class));
    }
}