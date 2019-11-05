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
  public void onConnect(SessionConnectedEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    log.info("WebSocket Session [{}] Connected", headerAccessor.getSessionId());
  }

  @EventListener
  public void onDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    log.info("WebSocket Session [{}] Disconnected", headerAccessor.getSessionId());
    if (headerAccessor.getSessionAttributes().get("uuid") != null) {
      log.info("User [{}] Left", headerAccessor.getSessionAttributes().get("uuid"));
    }
  }
}
