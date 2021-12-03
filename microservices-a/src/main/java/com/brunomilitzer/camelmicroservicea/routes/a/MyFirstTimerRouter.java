package com.brunomilitzer.camelmicroservicea.routes.a;

import java.time.LocalDateTime;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;

    @Override
    public void configure() throws Exception {
        // timer
        // transformation
        // log
        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        from("timer:first-timer") // null
            .log("${body}")
            .transform().constant("My Constant Message")
            //.transform().constant("Time now is " + LocalDateTime.now())
            .log("${body}")
            //.bean("getCurrentTimeBean")
            .bean(getCurrentTimeBean, "getCurrentTime")
            .log("${body}")
            .bean(loggingComponent)
            .process(new SimpleLoggingProcessor())
            .to("log:first-time");
    }
}

//@Component
class GetCurrentTimeBean {
    public String getCurrentTime() {
        return "Time now is " + LocalDateTime.now();
    }
}

//@Component
class SimpleLoggingProcessingComponent {

    private final Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    public void process(String message) {
        logger.info("SimpleLoggingProcessingComponent {}", message);
    }
}

class SimpleLoggingProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLoggingProcessor {}", exchange);
    }
}