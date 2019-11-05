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
public class RoomsInfo {

    @Id
    @Column
    private String roomId;

    @Column
    private String roomName;

    @Column
    private Integer capacity;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "roomCode", cascade = CascadeType.ALL)
    //@OrderBy("description asc")
    private List<RoomTimings> roomTimings;

}
