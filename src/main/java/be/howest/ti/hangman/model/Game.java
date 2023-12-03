package be.howest.ti.hangman.model;

import be.howest.ti.hangman.util.enums.GameStatus;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.*;

@Getter
public class Game {

    private final UUID id = UUID.randomUUID();
    private final String name;
    private GameStatus status;
    private final Set<Player> players;
    private final List<WordToGuess> words;

    public Game(String name, Set<Player> players, List<WordToGuess> words) {
        this.name = name;
        if (players.isEmpty()) {
            throw new IllegalArgumentException("A game needs at least one player");
        }
        this.players = players;
        this.words = words;
        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }
    public Game(Set<Player> players, String name) {
        this(name, players, new ArrayList<>());
    }

    public Game(Player player, String name) {
        this(Set.of(player), name);
    }

    public void addPlayer(Player player) {
        if (GameStatus.WAITING_FOR_PLAYERS.equals(status)) {
            players.add(player);
        } else {
            throw new HangmanException("Game is already started");
        }
        players.add(player);
        setGameStatus(GameStatus.WAITING_FOR_WORDS);
    }

    private void setGameStatus(GameStatus gameStatus) {
        this.status = gameStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game game)) return false;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @JsonIgnore
    public WordToGuess getWordToGuess() {
        if (GameStatus.WAITING_FOR_WORDS.equals(status)) {
            throw new HangmanException("Game is waiting for words");
        }
        if (GameStatus.WAITING_FOR_PLAYERS.equals(status)) {
            throw new HangmanException("Game is waiting for players");
        }
        if (GameStatus.FINISHED.equals(status)) {
            throw new HangmanException("Game is finished");
        }
        if (words.isEmpty()) {
            throw new HangmanException("No words to guess");
        }
        return words.stream()
                .filter(word -> !word.isGuessed())
                .findFirst()
                .orElseThrow(() -> new HangmanException("No words to guess"));
    }

    public void removePlayer(Player player) {
        players.remove(player);
        setGameStatus(GameStatus.WAITING_FOR_PLAYERS);
    }
}
