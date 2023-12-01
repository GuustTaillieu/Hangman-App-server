package be.howest.ti.hangman.websocket.lobby;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LobbyMessage {

    private UUID playerId;
    private LobbyMessageType type;
}
