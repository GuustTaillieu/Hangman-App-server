package be.howest.ti.hangman.websocket.game;

import be.howest.ti.hangman.util.enums.GameMessageType;
import be.howest.ti.hangman.util.enums.LobbyMessageType;
import lombok.Builder;

@Builder
public record GameMessage<T>(GameMessageType type, T data) {

}
