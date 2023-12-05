package be.howest.ti.hangman.data;

import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.model.WordToGuess;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GameRepository {

    Set<Game> getGames();
    Game createGame(Player player, String gameName);
    Optional<Game> getGameById(UUID id);
    void addPlayerToGame(UUID gameId, Player player);
    WordToGuess getWordToGuess(Game game);
    void removeGame(Game game);
}
