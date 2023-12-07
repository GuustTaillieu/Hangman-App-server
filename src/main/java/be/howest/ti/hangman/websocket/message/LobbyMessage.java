package be.howest.ti.hangman.websocket.message;

import be.howest.ti.hangman.util.enums.LobbyMessageType;
import lombok.*;

@Builder
public record LobbyMessage<T>(LobbyMessageType type, T data) {

}
