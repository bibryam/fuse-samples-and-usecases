###How to set a custom context for REST DSL with 'servlet' on Spring Boot

camel.component.servlet.mapping.context-path=/foo/v1/*

    restConfiguration()
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Greeting REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "/foo/v1/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);

http://localhost:8080/webjars/swagger-ui/index.html?url=/foo/v1/api-doc&validatorUrl=#/greetings/greeting-api

curl -X GET "http://localhost:8080/foo/v1/greetings/fff" -H  "accept: application/json"



###How to add Hawtio
     
    --- a/fuse-72-clean-sample/pom.xml
    +++ b/fuse-72-clean-sample/pom.xml
    @@ -83,6 +83,10 @@
           <artifactId>camel-test</artifactId>
           <scope>test</scope>
         </dependency>
    +    <dependency>
    +      <groupId>io.hawt</groupId>
    +      <artifactId>hawtio-springboot</artifactId>
    +    </dependency>

    
    diff --git a/fuse-72-clean-sample/src/main/resources/application.properties b/fuse-72-clean-sample/src/main/resources/application.properties
    index 57ab22d..fcb5714 100755
    --- a/fuse-72-clean-sample/src/main/resources/application.properties
    +++ b/fuse-72-clean-sample/src/main/resources/application.properties
    @@ -1,9 +1,16 @@
     server.port=8080
    
     # disable all management enpoints except health
    -endpoints.enabled = false
    +endpoints.enabled = true
     endpoints.health.enabled = true
     management.health.defaults.enabled=false
    
     camel.health.enabled=false
     camel.health.indicator.enabled=true
    +
    +
    +management.port              = 8081
    +endpoints.jolokia.sensitive  = false
    +endpoints.hawtio.sensitive   = false
    +hawtio.authenticationEnabled = false
    +Hawtio.offline               = false
    \ No newline at end of file
