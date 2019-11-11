package com.prototype.booking.api.integration;

import com.prototype.booking.api.MeetingRoomBooking;
import com.prototype.booking.api.referencedata.TimingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.integration.http.dsl.Http;

import javax.persistence.EntityManager;

@Configuration
@Slf4j
public class RoomServiceFlow {

    @Autowired
    TimingsRepository timingsRepository;

    @Autowired
    RoomServiceGateway.RoomService roomServiceGateway;

    @Bean
    public IntegrationFlow roomsInfoFlow(EntityManager entityManager) {
        return IntegrationFlows.from(Http.inboundGateway("/rooms")
                .requestMapping(m -> m.methods(HttpMethod.GET))
                .errorChannel("globalErrorChannel.input"))
                .wireTap("loggingFlow.input")
                .log(LoggingHandler.Level.INFO, this.getClass().getName(), m -> "Retrieving Total Rooms Information")
                .handle((p,h) -> roomServiceGateway.getRoomsInfo("all")) //Spring Integration currently has no concept of a message without a payload
                .get();
    }

    @Bean
    public IntegrationFlow bookRoomFlow(EntityManager entityManager) {
        return IntegrationFlows.from(Http.inboundGateway("/booking")
                .requestPayloadType(MeetingRoomBooking.class)
                .requestMapping(m -> m.methods(HttpMethod.POST))
                .errorChannel("globalErrorChannel.input"))
                .wireTap("loggingFlow.input")
                .log(LoggingHandler.Level.INFO, this.getClass().getName(), m -> "Start - Booking Meeting Room")
                .<MeetingRoomBooking>handle((p, h) -> {
                    MeetingRoomBooking app = roomServiceGateway.bookRoom(p);
                    log.info("End - Booking Meeting Room, Booking Id: {}", app.getMeetingRoomReference().getBookingId());
                    return app;
                })
                .transform(MeetingRoomBooking.class, p -> p.getMeetingRoomReference())
                .get();
    }



    @Bean
    public IntegrationFlow loggingFlow() {
        return f -> f.handle(message -> {
            log.info("===========================incoming request details================================================");
            log.info("URI         : {}", message.getHeaders().get(HttpHeaders.REQUEST_URL));
            log.info("Method      : {}", message.getHeaders().get(HttpHeaders.REQUEST_METHOD));
            log.info("Request body: {}", message.getPayload());
            log.info("===================================================================================================");
        });
    }
}
