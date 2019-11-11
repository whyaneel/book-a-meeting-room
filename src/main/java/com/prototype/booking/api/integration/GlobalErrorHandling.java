package com.prototype.booking.api.integration;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
@Slf4j
public class GlobalErrorHandling {

    //REST API Error Handling
    @Bean
    public IntegrationFlow globalErrorChannel(){
        return f -> f.<MessagingException>handle((p, h) -> {
            log.error("APi Gateway Error : {}", p);
            return MessageBuilder
                    .withPayload(Strings.EMPTY)
                    .setHeaderIfAbsent(HttpHeaders.STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        });
    }

    //Service Layer Error Handling TODO: return proper error messages to client
    @Bean
    public IntegrationFlow error() {
        return IntegrationFlows.from("roomServiceError")
                .handle((p,h) -> {
                    log.error("Room Info service Error : ", p);
                    return MessageBuilder
                            .withPayload(Strings.EMPTY)
                            .setHeaderIfAbsent(HttpHeaders.STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                })
                .get();
    }
}
