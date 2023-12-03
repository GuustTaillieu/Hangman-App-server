package be.howest.ti.hangman.websocket.game;

import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.service.GameService;
import be.howest.ti.hangman.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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

}
