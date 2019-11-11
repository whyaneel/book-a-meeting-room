package com.prototype.booking.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
  // TODO this will be holding meeting room, timinings info etc.
  private String content;
  private String sender;
}
