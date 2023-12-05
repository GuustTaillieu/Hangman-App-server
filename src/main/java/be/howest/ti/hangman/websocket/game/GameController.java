package be.howest.ti.hangman.websocket.game;

import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.service.GameService;
import be.howest.ti.hangman.service.PlayerService;
import be.howest.ti.hangman.util.enums.GameMessageType;
import be.howest.ti.hangman.util.enums.GameStatus;
import be.howest.ti.hangman.util.enums.LobbyMessageType;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import be.howest.ti.hangman.websocket.lobby.LobbyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@Slf4j
public class GameController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public GameController(SimpMessagingTemplate messagingTemplate, PlayerService playerService, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @MessageMapping("/game/join")
    public void joinGame(@Header String gameId, @Header String playerId) {
        UUID gameUUID = UUID.fromString(gameId);
        UUID playerUUID = UUID.fromString(playerId);
        gameService.addPlayerToGame(gameUUID, playerUUID);
        Game game = gameService.getGameById(gameUUID);
        messagingTemplate.convertAndSend("/lobby/activities", new LobbyMessage<>(LobbyMessageType.GAME_JOINED, game));
        messagingTemplate.convertAndSend("/game/" + gameId, new GameMessage<>(GameMessageType.GAME_UPDATED, game));
    }

    @MessageMapping("/game/start")
    public void startGame(@Header String gameId, @Header String playerId) {
        Game game = gameService.getGameById(UUID.fromString(gameId));
        if (!game.getHost().getId().equals(UUID.fromString(playerId))) {
            throw new HangmanException("Only the owner can start the game");
        }
        if (game.getPlayers().size() < 2) {
            throw new HangmanException("Not enough players to start the game");
        }
        game.setStatus(GameStatus.WAITING_FOR_WORDS);
        messagingTemplate.convertAndSend("/topic/game." + gameId, new GameMessage<>(GameMessageType.GAME_UPDATED, game));
        log.info("Starting game {} by player {}", gameId, playerId);
    }

    @MessageMapping("/game/send-word")
    public void setWord(@Header String gameId, @Header String playerId, @Header String word) {
        Game game = gameService.getGameById(UUID.fromString(gameId));
        UUID playerUUID = UUID.fromString(playerId);
        gameService.addWordToGame(game, playerUUID, word);
        messagingTemplate.convertAndSend("/topic/game." + gameId, new GameMessage<>(GameMessageType.GAME_UPDATED, game));
        log.info("Setting word {} for game {} by player {}", word, gameId, playerId);
    }

    @MessageMapping("/game/guess-letter")
    public void guessLetter(@Header String gameId, @Header String playerId, @Header String letter) {
        Game game = gameService.getGameById(UUID.fromString(gameId));
        UUID playerUUID = UUID.fromString(playerId);
        Player player = playerService.getPlayerById(playerUUID);
        gameService.guessLetter(game, player, letter.toLowerCase().charAt(0));
        messagingTemplate.convertAndSend("/topic/game." + gameId, new GameMessage<>(GameMessageType.GAME_UPDATED, game));
        log.info("Guessing letter {} for game {} by player {}", letter, gameId, playerId);
    }

    @MessageMapping("/game/reset")
    public void resetGame(@Header String gameId) {
        Game game = gameService.getGameById(UUID.fromString(gameId));
        gameService.resetGame(game);
        messagingTemplate.convertAndSend("/topic/game." + gameId, new GameMessage<>(GameMessageType.GAME_UPDATED, game));
        log.info("Resetting game {}", gameId);
    }
}
