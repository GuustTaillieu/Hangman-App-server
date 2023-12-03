package be.howest.ti.hangman.model;

import be.howest.ti.hangman.util.exceptions.HangmanException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
public class WordToGuess {

    private final String word;
    private String state;
    private final Player owner;
    @JsonIgnore
    private final Game belongingGame;
    @Setter
    private boolean guessed = false;
    private Set<Character> guessedLetters;

    public WordToGuess(String word, Player owner, Game belongingGame) {
        this.word = word;
        this.state = "_".repeat(word.length());
        this.owner = owner;
        this.belongingGame = belongingGame;
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

    public void setState(String newState) {
        this.state = newState;
    }
    public boolean addGuessedLetter(char guessedLetter) {
        if (guessedLetters.contains(guessedLetter)) {
            return false;
        }
        this.guessedLetters.add(guessedLetter);
        return true;
    }
}
