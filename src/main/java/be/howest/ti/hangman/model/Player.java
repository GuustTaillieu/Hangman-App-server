package be.howest.ti.hangman.model;


import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {
    private final UUID id;
    private final String name;
    private int score;

    public Player(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.score = 0;
    }
}
