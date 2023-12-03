package be.howest.ti.hangman.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Player {
    private final UUID id;
    @JsonIgnore
    private final String sessionId;
    private final String name;
    @Setter
    @JsonIgnore
    private Game game;
    @Setter
    private int score;

    public Player(String sessionId, UUID playerId, String name) {
        this.sessionId = sessionId;
        this.id = playerId;
        this.name = name;
        this.score = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
