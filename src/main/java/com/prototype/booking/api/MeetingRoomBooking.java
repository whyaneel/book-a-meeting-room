package com.prototype.booking.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity(name="MEETING_ROOM_BOOKING")
public class MeetingRoomBooking {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid2")
    @JsonIgnore
    @Column(name="ID",length=50)
    private String id;

    @Embedded
    private UserInfo userInfo;

    @Embedded
    private RoomInfo roomInfo;

    @Embedded
    private MeetingRoomReference meetingRoomReference;

    @JsonIgnore
    @Column(name = "CREATED_DT", nullable = false)
    @JsonProperty("create_time")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime created;

    @JsonIgnore
    @Column(name = "UPDATED_DT", nullable = false)
    @JsonProperty("update_time")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        updated = created = LocalDateTime.now();
        Long identifier = ThreadLocalRandom.current().nextLong(100000000, 999999999);
        StringBuilder refNumber =
                new StringBuilder(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        refNumber.append(identifier);
        meetingRoomReference = MeetingRoomReference.builder().bookingId(refNumber.toString()).build();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }

    @Embeddable
    @Data
    public static class UserInfo {
        @Column
        private String empId;

        @Column
        private String email;
    }

    @Embeddable
    @Data
    public static class RoomInfo {

        @Column
        private String roomId;

        @Column
        private String timeId;

    }

    @Embeddable
    @Data
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class MeetingRoomReference {
        @Column(name="BOOKING_ID",length=50)
        private String bookingId;
    }


}
