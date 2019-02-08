package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AnimalRouteTest {
    public static final String NICE_DOG = "nice dog", NASTY_CAT="nasty cat", SUPERNASTY_CAT="super nasty cat";

    @EndpointInject(uri = "{{dogEndpoint}}")
    protected MockEndpoint dogEndpoint;

    @EndpointInject(uri = "{{catEndpoint}}")
    protected MockEndpoint catEndpoint;

    @EndpointInject(uri = "{{animalSource}}")
    protected ProducerTemplate animalSource;

    @Test
    @DirtiesContext
    public void testDog() throws Exception {
        dogEndpoint.expectedMessageCount(1);
        catEndpoint.expectedMessageCount(0);

        animalSource.sendBodyAndHeader("test", AnimalRoute.CAMEL_FILE_NAME, NICE_DOG);

        dogEndpoint.assertIsSatisfied();
        catEndpoint.assertIsSatisfied();

        dogEndpoint.message(0).predicate(m -> {
            String header = m.getIn().getHeader(AnimalRoute.CAMEL_FILE_NAME).toString();
            return NICE_DOG.equals(header);
        });
    }

    @Test
    @DirtiesContext
    public void testCat() throws Exception {
        catEndpoint.expectedMessageCount(2);
        dogEndpoint.expectedMessageCount(0);

        animalSource.sendBodyAndHeader("test", AnimalRoute.CAMEL_FILE_NAME, NASTY_CAT);
        animalSource.sendBodyAndHeader("test", AnimalRoute.CAMEL_FILE_NAME, SUPERNASTY_CAT);

        dogEndpoint.assertIsSatisfied();
        catEndpoint.assertIsSatisfied();


        catEndpoint.message(0).predicate(m -> {
            String header = m.getIn().getHeader(AnimalRoute.CAMEL_FILE_NAME).toString();
            return NASTY_CAT.equals(header);
        });

        catEndpoint.message(1).predicate(m -> {
            String header = m.getIn().getHeader(AnimalRoute.CAMEL_FILE_NAME).toString();
            return SUPERNASTY_CAT.equals(header);
        });
    }
}