package com.prototype.booking.api.referencedata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TimingsRepository extends JpaRepository<Timings, String>, JpaSpecificationExecutor<Timings> {
    Optional<Timings> findByRoomCodeAndTimeId(String roomId, String timeId);
}
