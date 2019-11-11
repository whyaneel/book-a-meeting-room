package com.prototype.booking.api.integration;

import com.prototype.booking.api.MeetingRoomBooking;
import com.prototype.booking.api.referencedata.MeetingRoom;
import com.prototype.booking.api.referencedata.Timings;
import com.prototype.booking.api.referencedata.TimingsRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@Slf4j
public class RoomServiceGateway {

    @Autowired
    TimingsRepository timingsRepository;

    @MessagingGateway(name = "roomService", errorChannel = "roomServiceError")
    public interface RoomService {

        @Gateway(requestChannel = "book.input")
        MeetingRoomBooking bookRoom(MeetingRoomBooking meetingRoomBooking);

        @Gateway(requestChannel = "retrieve.input")
        List<MeetingRoom> getRoomsInfo(String p);
    }


    @Bean
    public IntegrationFlow book(EntityManager entityManager) {
        return f -> f.log(LoggingHandler.Level.INFO, this.getClass().getName(),
                m -> "Start - Saving the User and Room Info")
                .handle(Jpa.updatingGateway(entityManager).entityClass(MeetingRoomBooking.class)
                    .persistMode(PersistMode.MERGE), e -> e.transactional())
                .<MeetingRoomBooking>handle((p, h) -> {
                    Optional<Timings> timings = timingsRepository.findByRoomIdAndTimeId(p.getRoomInfo().getRoomId(), p.getRoomInfo().getTimeId());
                    if(timings.isPresent()){
                        var roomTimings = timings.get();
                        roomTimings.setIsAvailable(false);
                        timingsRepository.save(roomTimings);
                    }
                    return p;
                })
                .<MeetingRoomBooking>handle((p,h) -> p)
                .log(LoggingHandler.Level.INFO, this.getClass().getName(),
                        m -> "End - Saving the User and Room Info " + m.getPayload())
                .bridge();
    }

    @Bean
    public IntegrationFlow retrieve(EntityManager entityManager) {
        return f -> f
                .handle(Jpa.retrievingGateway(entityManager).entityClass(MeetingRoom.class))
                .bridge();
    }


    //TODO : Room Release Service
    @Bean
    public IntegrationFlow release(EntityManager entityManager) {
        return f -> f
                .bridge();
    }


}
