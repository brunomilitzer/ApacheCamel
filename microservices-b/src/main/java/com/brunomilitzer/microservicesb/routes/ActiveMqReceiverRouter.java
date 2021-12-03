package com.brunomilitzer.microservicesb.routes;

import com.brunomilitzer.microservicesb.model.CurrencyExchange;
import java.math.BigDecimal;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

    /*@Autowired
    private MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;

    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;*/

    @Override
    public void configure() throws Exception {
        // JSON

        /*from("activemq: my-activemq-queue")
            .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
            .bean(myCurrencyExchangeProcessor)
            .bean(myCurrencyExchangeTransformer)
            .to("log:received-message-from-active-mq");*/

        /*from("activemq:my-activemq-xml-queue")
            .unmarshal()
            .jacksonxml(CurrencyExchange.class)
            .to("log:received-message-from-active-mq");*/

        from("activemq:split-queue")
            .log("log:received-message-from-active-mq");
    }
}

//@Component
class MyCurrencyExchangeProcessor {

    private Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);

    public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {

        logger.info("Some processing with currency exchange: " + currencyExchange.getConversionMultiple());

        return currencyExchange;
    }
}

//@Component
class MyCurrencyExchangeTransformer {

    private Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeTransformer.class);

    public CurrencyExchange transformMessage(CurrencyExchange currencyExchange) {

        currencyExchange.setConversionMultiple(currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));

        return currencyExchange;
    }
}