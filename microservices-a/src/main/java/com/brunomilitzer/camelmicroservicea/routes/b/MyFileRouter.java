package com.brunomilitzer.camelmicroservicea.routes.b;

import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFileRouter extends RouteBuilder {

    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {

        // Pipeline Pattern

        from("file:microservices-a/files/input")
            //.pipeline() // default pattern
            .routeId("Files-Input-Route")
            .transform().body(String.class)
            .choice()
                .when(simple("${file:ext} == 'xml'"))
                    .log("XML FILE")
                //.when(simple("${body} contains 'USD'"))
                .when(method(deciderBean))
                    .log("Not an XML FILE BUT contains USD")
                .otherwise()
                    .log("Not an XML FILE")
            .end()
            //.to("direct:log-file-values")
            .to("file:files/output");


        from("direct:log-file-values")
            .to("file:microservices-a/files/output")
            .log("${messageHistory} ${file:absolute.path}")
            .log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
            .log("${file:size} ${file:modified}")
            .log("${routeId} ${camelId} ${body}");
    }
}

@Component
class DeciderBean {

    private Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isThisConditionMet(@Headers Map<String, String> headers, @Body String body, @ExchangeProperties Map<String, String> exchangeProperties) {
        logger.info("{} {} {}", headers, body, exchangeProperties);
        return true;
    }
}