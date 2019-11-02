package com.org.desi.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class RoomsInfo {
    private List<Rooms> rooms;

    @Data
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Rooms {
        private String roomId;
    }

}
