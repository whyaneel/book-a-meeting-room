package com.prototype.booking.api;

import com.prototype.booking.api.referencedata.RoomsInfo;
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
public class BookMeetingRoom {

    @Autowired
    TimingsRepository timingsRepository;

    @MessagingGateway(name = "roomServiceGateway", errorChannel = "roomServiceError")
    public interface RoomServiceGateway {

        @Gateway(requestChannel = "book.input")
        BookRoomApp bookRoom(BookRoomApp bookRoomApp);

        @Gateway(requestChannel = "retrieve.input")
        List<RoomsInfo> getRoomsInfo(String p);
    }


    @Bean
    public IntegrationFlow book(EntityManager entityManager) {
        return f -> f.log(LoggingHandler.Level.INFO, this.getClass().getName(),
                m -> "Start - Saving the User and Room Info")
                .handle(Jpa.updatingGateway(entityManager).entityClass(BookRoomApp.class)
                    .persistMode(PersistMode.MERGE), e -> e.transactional())
                .<BookRoomApp>handle((p,h) -> {
                    Optional<Timings> timings = timingsRepository.findByRoomCodeAndTimeId(p.getRoomInfo().getRoomId(), p.getRoomInfo().getTimeId());
                    if(timings.isPresent()){
                        var roomTimings = timings.get();
                        roomTimings.setIsAvailable(false);
                        timingsRepository.save(roomTimings);
                    }
                    return p;
                }).log(LoggingHandler.Level.INFO, this.getClass().getName(),
                        m -> "End - Saving the User and Room Info with Booking ID " + m.getPayload())
                .bridge();
    }

    @Bean
    public IntegrationFlow retrieve(EntityManager entityManager) {
        return f -> f
                .handle(Jpa.retrievingGateway(entityManager).entityClass(RoomsInfo.class))
                .bridge();
    }


    //TODO : Room Release Service
    @Bean
    public IntegrationFlow release(EntityManager entityManager) {
        return f -> f
                .bridge();
    }


}
