package com.prototype.booking.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private String bookingId;
}
