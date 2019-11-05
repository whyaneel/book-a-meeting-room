package com.prototype.booking.api.referencedata;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIMINGS")
@Data
public class RoomTimings {

    @Id
    @Column
    private String timeId;

    @Column
    private String timeSlot;

    @Column
    private String roomCode;

    @Column
    private Boolean isAvailable;
}
