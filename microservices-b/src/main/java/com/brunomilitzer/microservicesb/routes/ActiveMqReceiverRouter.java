package com.brunomilitzer.microservicesb.routes;

import com.brunomilitzer.microservicesb.model.CurrencyExchange;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.crypto.CryptoDataFormat;
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

        /*from("activemq:split-queue")
            .log("log:received-message-from-active-mq");*/

        from("activemq:my-activemq-queue")
            .unmarshal(createEncryptor())
            .log("log:received-message-from-active-mq");
    }

    private CryptoDataFormat createEncryptor() throws KeyStoreException, IOException, NoSuchAlgorithmException,
        CertificateException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        ClassLoader classLoader = getClass().getClassLoader();
        keyStore.load(classLoader.getResourceAsStream("myDesKey.jceks"), "someKeystorePassword".toCharArray());
        Key sharedKey = keyStore.getKey("myDesKey", "someKeyPassword".toCharArray());

        CryptoDataFormat sharedKeyCrypto = new CryptoDataFormat("DES", sharedKey);
        return sharedKeyCrypto;
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