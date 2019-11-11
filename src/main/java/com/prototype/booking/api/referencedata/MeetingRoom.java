package com.prototype.booking.api.referencedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name ="MEETING_ROOM")
public class MeetingRoom {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private Integer capacity;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "roomId", cascade = CascadeType.ALL)
    private List<Timings> roomTimings;

}
