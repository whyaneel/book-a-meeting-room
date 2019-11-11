package com.prototype.booking.api.referencedata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIMINGS")
@Data
public class Timings {

    @Id
    @Column
    private String timeId;

    @Column
    private String timeSlot;

    @JsonIgnore
    @Column
    private String roomId;

    @Column
    private Boolean isAvailable;
}
