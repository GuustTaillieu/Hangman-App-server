package be.howest.ti.hangman.websocket.lobby;

import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.service.GameService;
import be.howest.ti.hangman.service.PlayerService;
import be.howest.ti.hangman.util.enums.LobbyMessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
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

    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public LobbyController(SimpMessagingTemplate messagingTemplate, PlayerService playerService, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @MessageMapping("/lobby-join")
    @SendTo("/lobby/activities")
    public LobbyMessage<Set<Game>> joinLobby(@Payload String playerId) {
        log.info("Player with id {} joined the lobby", playerId);
        return new LobbyMessage<>(LobbyMessageType.LOBBY_JOINED, gameService.getLobbyGames());
    }

    @MessageMapping("/lobby/create-game")
    @SendTo("/lobby/activities")
//    Takes JSON with playerId and gameName as payload
    public LobbyMessage<Game> createGame(@Header String playerId, @Header String gameName) {
        log.info("Player with id {} created a game with name {}", playerId, gameName);
        UUID playerUUID = UUID.fromString(playerId);
        return new LobbyMessage<>(LobbyMessageType.GAME_CREATED, gameService.createGame(playerUUID, gameName));
    }

}
