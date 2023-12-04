package be.howest.ti.hangman.model;

import be.howest.ti.hangman.util.exceptions.HangmanException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
public class WordToGuess {

    private final String word;
    private final Player owner;
    private final Map<Player, Set<Character>> guesses;
    @JsonIgnore
    private final Game belongingGame;

    public WordToGuess(String word, Player owner, Game belongingGame) {
        this.word = word.toLowerCase();
        this.owner = owner;
        this.belongingGame = belongingGame;
        this.guesses = Map.of(owner, word.chars().mapToObj(c -> (char) c).collect(HashSet::new, HashSet::add, HashSet::addAll));
    }

    public int getWrongGuesses(Player player) {
        return guesses.get(player).stream().filter(c -> !word.contains(c.toString())).toArray().length;
    }

    public boolean isDoneWithWord(Player player) {
        return getWrongGuesses(player) > 10 || guessedWord(player);
    }

    private boolean guessedWord(Player player) {
        return word.chars().mapToObj(c -> (char) c).allMatch(guesses.get(player)::contains);
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

    public void guessLetter(Player player, char letter) {
        if (isDoneWithWord(player)) {
            throw new HangmanException("Player " + player.getName() + " is already done with this word");
        }
        guesses.get(player).add(letter);
    }
}
