package be.howest.ti.hangman.websocket.config;

import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.websocket.lobby.LobbyMessage;
import be.howest.ti.hangman.websocket.lobby.LobbyMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String playerName = headerAccessor.getSessionAttributes().get("playerName").toString();
        Player player = new Player(playerName);
        log.info("Player with id {} connected", player.getId());
        LobbyMessage lobbyMessage = LobbyMessage.builder()
                .playerId(player.getId())
                .type(LobbyMessageType.JOIN)
                .build();
        messagingTemplate.convertAndSend("/lobby", lobbyMessage);
    }

                                               @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String playerIdString = headerAccessor.getSessionAttributes().get("playerId").toString();
        if (playerIdString != null) {
            UUID playerId = UUID.fromString(playerIdString);
            log.info("Player with id {} disconnected", playerId);
            LobbyMessage lobbyMessage = LobbyMessage.builder()
                    .playerId(playerId)
                    .type(LobbyMessageType.LEAVE)
                    .build();
//            TODO: SEE IF PLAYER IS IN GAME AND REMOVE HIM FROM GAME
//            TODO: IF PLAYER IS IN GAME, SEND MESSAGE TO OTHER PLAYER THAT HE LEFT
            messagingTemplate.convertAndSend("/lobby", lobbyMessage);
        }
    }

}
