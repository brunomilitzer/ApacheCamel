package com.brunomilitzer.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestApiConsumerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration().host("localhost").port(8000);

        from("timer:rest-api-consumer?period=10000")
            .setHeader("from", () -> "USD")
            .setHeader("to", () -> "BR")
            .to("rest:get:/currency-exchange/from/{from}/to/{to}")
            .log("${body}");
    }
}
