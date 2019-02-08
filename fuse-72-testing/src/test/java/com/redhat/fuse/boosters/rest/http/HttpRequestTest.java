package com.redhat.fuse.boosters.rest.http;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.camel.test.spring.CamelSpringBootRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

//@RunWith(SpringRunner.class)
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @MockBean
    private GreetingsService greetingsService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingsShouldReturnFallbackMessage() throws Exception {

        given(this.greetingsService.getGreetings(anyString())).willReturn(new Greetings("Hello from Mock"));

        Assert.assertEquals( "Hello from Mock", this.restTemplate.getForObject("http://localhost:" + port + "/camel/greetings/jacopo", Greetings.class).getGreetings());
    }

    @Test
    public void healthShouldReturnOkMessage() throws Exception {

        Assert.assertEquals( "{\"status\":\"UP\"}", this.restTemplate.getForObject("http://localhost:" + port + "/health", String.class));
    }
}