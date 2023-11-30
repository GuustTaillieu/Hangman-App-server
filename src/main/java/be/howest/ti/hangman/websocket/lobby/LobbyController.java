package be.howest.ti.hangman.websocket.lobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

}
