package com.brunomilitzer.camelmicroservicea.routes.patterns;

import com.brunomilitzer.camelmicroservicea.model.CurrencyExchange;
import java.util.List;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EipPatternsRouter extends RouteBuilder {

    @Autowired
    private SplitterComponent splitter;

    @Override
    public void configure() throws Exception {
        // Pipeline
        // Content Based Routing - choice()
        // Multicast

        /*from("timer:multicast?period=10000")
            .multicast()
            .to("log:something1", "log:something2");*/

        /*from("file:microservices-a/files/csv")
            .unmarshal().csv()
            .split(body())
            .to("activemq:split-queue");*/

        // Message1, Message2, Message3
        /*from("file:microservices-a/files/csv")
            .convertBodyTo(String.class)
            //.split(body(), ",")
            .split(method(splitter))
            .to("activemq:split-queue");*/

        // Aggregate
        // Messages => Aggregate => Endpoint
        // to, 3
        /*from("file:microservices-a/files/aggregate-json")
            .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
            .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
            .completionSize(3)
            .completionTimeout(HIGHEST)
            .to("log:aggregation-size");*/

        // routing slip
        String routingSlip1 = "direct:endpoint1, direct:endpoint2";
        String routingSlip2 = "direct:endpoint1, direct:endpoint2, direct:endpoint3";

        from("timer:routingSlip1?period=10000")
            .transform().constant("My Message is Hardcoded")
            .routingSlip(simple(routingSlip2));

        from("direct:endpoint1")
            .to("log:directendpoint1");

        from("direct:endpoint2")
            .to("log:directendpoint2");

        from("direct:endpoint3")
            .to("log:directendpoint3");
    }
}

@Component
class SplitterComponent {
    public List<String> splitInput(String body) {
        return List.of("ABC", "DEF", "GHI");
    }
}