package be.howest.ti.hangman.websocket.config;

import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.service.GameService;
import be.howest.ti.hangman.service.PlayerService;
import be.howest.ti.hangman.util.enums.GameStatus;
import be.howest.ti.hangman.websocket.message.LobbyMessage;
import be.howest.ti.hangman.util.enums.LobbyMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public WebSocketEventListener(
            SimpMessageSendingOperations messagingTemplate,
            PlayerService playerService,
            GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String playerName = headerAccessor.getFirstNativeHeader("username");
        UUID playerId = UUID.fromString(Objects.requireNonNull(headerAccessor.getFirstNativeHeader("userId")));
        Player player = new Player(Objects.requireNonNull(headerAccessor.getSessionId()), playerId, playerName);
        playerService.addPlayer(player);
        log.info("Player with id {} and name {} connected", playerId, playerName);
    }

   @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
            Player player = playerService.getPlayerBySessionId(sessionId);
            UUID playerId = player.getId();
            playerService.removePlayer(playerId);
            log.info("Player with id {} disconnected", playerId);
            Game game = player.getGame();
            if (game != null) {
                handleGameAfterPlayerDisconnect(game, player);
            }
        }
    }

    private void handleGameAfterPlayerDisconnect(Game game, Player player) {
        if (game.getStatus().equals(GameStatus.WAITING_FOR_PLAYERS)) {
            gameService.removePlayerFromGame(game, player);
            if (game.getHost().equals(player)) {
                log.info("Game with id {} removed", game.getId());
                messagingTemplate.convertAndSend("/lobby/activities", new LobbyMessage<>(LobbyMessageType.GAME_REMOVED, gameService.getLobbyGames()));
            }
            messagingTemplate.convertAndSend("/topic/game." + game.getId(), game);
        } else {
            gameService.removeGame(game);
            log.info("Game with id {} removed", game.getId());
            messagingTemplate.convertAndSend("/lobby/activities", new LobbyMessage<>(LobbyMessageType.GAME_REMOVED, gameService.getLobbyGames()));
        }
    }

}
