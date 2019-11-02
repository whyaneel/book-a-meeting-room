package com.org.desi.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.integration.http.dsl.Http;

import java.util.Arrays;
import java.util.List;

import com.org.desi.api.RoomsInfo.*;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
@Slf4j
public class BookMeetingRoom {

    @Bean
    public IntegrationFlow roomsInfoFlow() {
        return IntegrationFlows.from(Http.inboundGateway("/rooms")
                .requestMapping(m -> m.methods(HttpMethod.GET))
                .errorChannel("globalErrorChannel.input"))
                .log(LoggingHandler.Level.INFO, this.getClass().getName(), m -> "Start -  Retrieving Total Rooms Information")
                .handle((p,h) -> {
                    List<Rooms> rooms = Arrays.asList(
                            Rooms.builder().roomId("01").build(),
                            Rooms.builder().roomId("02").build(),
                            Rooms.builder().roomId("03").build()
                    );
                    RoomsInfo roomsInfo = RoomsInfo.builder().rooms(rooms).build();
                    log.info("End - Retrieving Total Rooms Information");
                    return MessageBuilder.withPayload(roomsInfo).copyHeadersIfAbsent(h).build();
                })
                .get();
    }

    @Bean
    public IntegrationFlow globalErrorChannel(){
        return f -> f.<MessagingException>handle((p,h) -> {
            return MessageBuilder
                    .withPayload(Strings.EMPTY)
                    .setHeaderIfAbsent(HttpHeaders.STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            });
    }
}
