package be.howest.ti.hangman.data;

import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.model.WordToGuess;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("inMemoryGameRepository")
public class InMemoryGameRepository implements GameRepository{

    private final Set<Game> games = new HashSet<>();

    @Override
    public Set<Game> getGames() {
        return Collections.unmodifiableSet(games);
    }

    @Override
    public Game createGame(Player player, String gameName) {
        Game game = new Game(player, gameName);
        player.setGame(game);
        games.add(game);
        return game;
    }

    @Override
    public Optional<Game> getGameById(UUID id) {
        return games.stream().filter(game -> game.getId().equals(id)).findFirst();
    }

    @Override
    public void addPlayerToGame(UUID gameId, Player player) {
        Optional<Game> game = getGameById(gameId);
        if (game.isPresent()) {
            game.get().addPlayer(player);
            player.setGame(game.get());
        } else {
            throw new HangmanException("Game with id " + gameId + " not found");
        }
    }

    @Override
    public WordToGuess getWordToGuess(Game game) {
        return game.getWordToGuess();
    }

    @Override
    public WordToGuess updateWordToGuessState(Game game, String newWordToGuessState) {
        game.getWordToGuess().setState(newWordToGuessState);
        return game.getWordToGuess();
    }

    @Override
    public void removeGame(UUID gameId) {
        Optional<Game> game = getGameById(gameId);
        if (game.isPresent()) {
            games.remove(game.get());
        } else {
            throw new HangmanException("Game with id " + gameId + " not found");
        }
    }
}
