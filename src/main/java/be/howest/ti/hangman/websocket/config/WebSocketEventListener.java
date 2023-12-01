package be.howest.ti.hangman.websocket.config;

import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.websocket.lobby.LobbyMessage;
import be.howest.ti.hangman.websocket.lobby.LobbyMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String playerName = headerAccessor.getFirstNativeHeader("username");
        UUID playerId = UUID.fromString(Objects.requireNonNull(headerAccessor.getFirstNativeHeader("userId")));
        Player player = new Player(Objects.requireNonNull(headerAccessor.getSessionId()), playerId, playerName);
        log.info("Player with id {} connected", playerId);
    }

   @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
//            TODO: GET PLAYER ID FROM SESSION ID
            UUID playerId = UUID.randomUUID();
            log.info("Player with id {} disconnected", playerId);
            LobbyMessage lobbyMessage = LobbyMessage.builder()
                    .playerId(playerId)
                    .type(LobbyMessageType.LEAVE)
                    .build();
//            TODO: SEE IF PLAYER IS IN GAME AND REMOVE HIM FROM GAME
//            TODO: IF PLAYER IS IN GAME, SEND MESSAGE TO OTHER PLAYER THAT HE LEFT
            messagingTemplate.convertAndSend("/lobby/activities", lobbyMessage);
        }
    }

}
