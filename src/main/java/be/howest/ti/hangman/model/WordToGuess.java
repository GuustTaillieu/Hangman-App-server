package be.howest.ti.hangman.model;

import be.howest.ti.hangman.util.exceptions.HangmanException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class WordToGuess {

    private static final int MAX_WRONG_GUESSES = 9;

    private final String word;
    private final Player owner;
    private final Map<UUID, List<Character>> guesses;
    @JsonIgnore
    private final Game belongingGame;

    public WordToGuess(String word, Player owner, Game belongingGame) {
        this.word = word.toLowerCase();
        this.owner = owner;
        this.belongingGame = belongingGame;
        this.guesses = new HashMap<>();
        guesses.put(owner.getId(), word.chars().mapToObj(c -> (char) c).toList());
    }

    @JsonIgnore
    public int getWrongGuesses(Player player) {
        if (!guesses.containsKey(player.getId())) {
            guesses.put(player.getId(), new ArrayList<>());
        }
        return guesses.get(player.getId()).stream().filter(c -> !word.contains(c.toString())).toArray().length;
    }

    public boolean isDoneWithWord(Player player) {
        return getWrongGuesses(player) >= MAX_WRONG_GUESSES || guessedWord(player);
    }

    private boolean guessedWord(Player player) {
        return word.chars().mapToObj(c -> (char) c).allMatch(guesses.get(player.getId())::contains);
    }

    public void guessLetter(Player player, char letter) {
        if (isDoneWithWord(player)) {
            throw new HangmanException("Player " + player.getName() + " is already done with this word");
        }
        if (!guesses.containsKey(player.getId())) {
            guesses.put(player.getId(), new ArrayList<>());
        }
        guesses.get(player.getId()).add(letter);
        if (player.getWrongGuesses() >= MAX_WRONG_GUESSES) {
            player.setScore(player.getScore() - 1);
        }
        if (guessedWord(player)) {
            player.setScore(player.getScore() + 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordToGuess that)) return false;
        return Objects.equals(word, that.word) && Objects.equals(owner, that.owner) && Objects.equals(belongingGame, that.belongingGame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, owner, belongingGame);
    }
}
