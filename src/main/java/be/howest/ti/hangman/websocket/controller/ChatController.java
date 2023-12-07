package be.howest.ti.hangman.websocket.controller;

import be.howest.ti.hangman.model.Chat;
import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.service.ChatService;
import be.howest.ti.hangman.service.GameService;
import be.howest.ti.hangman.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/game/send-chat")
    public void sendMessage(@Header String gameId, @Header String playerId, @Header String message) {
        log.info("Sending message {} from player {} in game {}", message, playerId, gameId);
        UUID gameUUID = UUID.fromString(gameId);
        UUID playerUUID = UUID.fromString(playerId);
        Chat chat = chatService.addChat(gameUUID, playerUUID, message);
        messagingTemplate.convertAndSend("/topic/game." + gameId + ".chats", chat);
    }

}
