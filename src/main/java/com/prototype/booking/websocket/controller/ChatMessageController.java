package com.prototype.booking.websocket.controller;

import com.prototype.booking.model.ChatMessage;
import com.prototype.booking.utils.Constants;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {
  @MessageMapping("/message/{uuid}")
  @SendTo(Constants.BROKER_DESTINATION_PREFIX + "/{uuid}")
  public ChatMessage send(@Payload ChatMessage message) {
    //TODO service layer logic need to be used for processing
    return message;
  }

  @MessageMapping("/init/{uuid}")
  @SendTo(Constants.BROKER_DESTINATION_PREFIX + "/{uuid}")
  public ChatMessage init(@Payload ChatMessage message, @DestinationVariable String uuid, SimpMessageHeaderAccessor headerAccessor) {
    headerAccessor.getSessionAttributes().put("uuid", uuid);
    return message;
  }
}
