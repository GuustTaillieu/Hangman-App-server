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
    private final Player host;
    private GameStatus status;
    private final Set<Player> players;
    private final List<WordToGuess> words;
    @JsonIgnore
    private int currentWordToGuessIndex = 0;
    private String currentWord;

    public Game(String name, Player host, Set<Player> players, List<WordToGuess> words) {
        this.name = name;
        this.host = host;
        if (players.isEmpty()) {
            throw new IllegalArgumentException("A game needs at least one player");
        }
        this.players = players;
        this.words = words;
        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }
    public Game(Set<Player> players, Player host, String name) {
        this(name, host, players, new ArrayList<>());
    }

    public Game(Player host, String name) {
        this(new HashSet<>(Collections.singletonList(host)), host, name);
    }

    public void addPlayer(Player player) {
        if (GameStatus.WAITING_FOR_PLAYERS.equals(status)) {
            players.add(player);
        } else {
            throw new HangmanException("Game is already started");
        }
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void setStatus(GameStatus gameStatus) {
        this.status = gameStatus;
    }

    @JsonIgnore
    public WordToGuess getCurrentWordToGuess() {
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
        return words.get(currentWordToGuessIndex);
    }

    public WordToGuess getCurrentWord() {
        try {
            return getCurrentWordToGuess();
        } catch (HangmanException e) {
            return null;
        }
    }

    public void incrementCurrentWordToGuessIndex() {
        if (currentWordToGuessIndex + 1 >= words.size()) {
            words.clear();
            status = GameStatus.FINISHED;
        } else {
            currentWordToGuessIndex++;
        }
    }

    public void addWordToGuess(Player player, String word) {
        if (GameStatus.WAITING_FOR_WORDS.equals(status)) {
            words.add(new WordToGuess(word, player, this));
        } else {
            throw new HangmanException("Game is already started or waiting for players");
        }
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

    public void setCurrentWordToGuessIndex(int nr) {
        this.currentWordToGuessIndex = nr;
    }
}
