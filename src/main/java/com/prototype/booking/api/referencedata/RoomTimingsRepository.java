package com.prototype.booking.api.referencedata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoomTimingsRepository extends JpaRepository<RoomTimings, String>, JpaSpecificationExecutor<RoomTimings> {
    Optional<RoomTimings> findByRoomCodeAndTimeId(String roomId, String timeId);
}
