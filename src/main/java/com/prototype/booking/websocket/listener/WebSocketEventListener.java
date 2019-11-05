package com.prototype.booking.websocket.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {
  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    log.info("WebSocket Session Connected");
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    log.info("WebSocket Session Disconnected");
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String uuid = (String) headerAccessor.getSessionAttributes().get("uuid");
    if (uuid != null) {
      log.info("{} Disconnected : ", uuid);
    }
  }
}
