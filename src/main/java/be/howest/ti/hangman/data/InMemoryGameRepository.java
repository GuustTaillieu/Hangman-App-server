package be.howest.ti.hangman.data;

import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.model.WordToGuess;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("inMemoryGameRepository")
@Slf4j
public class InMemoryGameRepository implements GameRepository{

    private final Set<Game> games = new HashSet<>();

    @Override
    public Set<Game> getGames() {
        return Collections.unmodifiableSet(games);
    }

    @Override
    public Game createGame(Player host, String gameName) {
        Game game = new Game(host, gameName);
        host.setGame(game);
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
        return game.getCurrentWordToGuess();
    }

    @Override
    public void removeGame(Game game) {
        games.remove(game);
    }
}
