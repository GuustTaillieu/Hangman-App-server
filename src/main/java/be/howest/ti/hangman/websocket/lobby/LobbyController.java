package be.howest.ti.hangman.websocket.lobby;

import be.howest.ti.hangman.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.UUID;

@Controller
@Slf4j
public class LobbyController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/lobby/join")
    @SendTo("/lobby/activities")
    public Set<Player> joinLobby(@Payload String playerId) {
        log.info("Player with id {} joined the lobby", playerId);
        UUID uuid = UUID.fromString(playerId);
        return Set.of(new Player("sessionId", uuid, "name"));
    }

}
