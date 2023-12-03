package be.howest.ti.hangman.model;


import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Player {
    private final UUID id;
    private final String sessionId;
    private final String name;
    private int score;

    public Player(String sessionId, UUID playerId, String name) {
        this.sessionId = sessionId;
        this.id = playerId;
        this.name = name;
        this.score = 0;
    }
}
