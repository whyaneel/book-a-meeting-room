package com.prototype.booking.api;

import com.prototype.booking.api.referencedata.RoomTimings;
import com.prototype.booking.api.referencedata.RoomTimingsRepository;
import com.prototype.booking.api.referencedata.RoomsInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;

import javax.persistence.EntityManager;
import java.util.Optional;

@Configuration
@Slf4j
public class BookMeetingRoomFlow {

    @Autowired
    RoomTimingsRepository timingsRepository;

    @Bean
    public IntegrationFlow roomsInfoFlow(EntityManager entityManager) {
        return IntegrationFlows.from(Http.inboundGateway("/rooms")
                .requestMapping(m -> m.methods(HttpMethod.GET))
                .errorChannel("globalErrorChannel.input"))
                .wireTap("loggingFlow.input")
                .log(LoggingHandler.Level.INFO, this.getClass().getName(), m -> "Retrieving Total Rooms Information")
                .handle(Jpa.retrievingGateway(entityManager).entityClass(RoomsInfo.class))
                .transform(Transformers.toJson())
                .get();
    }

    @Bean
    public IntegrationFlow bookRoomFlow(EntityManager entityManager) {
        return IntegrationFlows.from(Http.inboundGateway("/bot/booking")
                .requestPayloadType(BookRoomApp.class)
                .requestMapping(m -> m.methods(HttpMethod.POST))
                .errorChannel("globalErrorChannel.input"))
                .wireTap("loggingFlow.input")
                .log(LoggingHandler.Level.INFO, this.getClass().getName(), m -> "Start - Booking Meeting Room")
                .handle(Jpa.updatingGateway(entityManager).entityClass(BookRoomApp.class)
                        .persistMode(PersistMode.MERGE), e -> e.transactional())
                .enrichHeaders(h -> h.headerExpression("bookingId", "payload.bookingId"))
                .<BookRoomApp>handle((p,h) -> {
                    Optional<RoomTimings> timings = timingsRepository.findByRoomCodeAndTimeId(p.getRoomInfo().getRoomId(), p.getRoomInfo().getTimeId());
                    if(timings.isPresent()){
                        RoomTimings roomTimings = timings.get();
                        roomTimings.setIsAvailable(false);
                        timingsRepository.save(roomTimings);
                    }
                    log.info("End - Booking Meeting Room, Booking Id: {}", h.get("bookingId"));
                    return MessageBuilder.withPayload(BookResponse.builder().bookingId(p.getBookingId()).build()).build();
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
