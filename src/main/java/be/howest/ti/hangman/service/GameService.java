package be.howest.ti.hangman.service;

import be.howest.ti.hangman.data.GameRepository;
import be.howest.ti.hangman.data.PlayerRepository;
import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.model.WordToGuess;
import be.howest.ti.hangman.util.enums.GameStatus;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(
            @Qualifier("inMemoryGameRepository") GameRepository gameRepository,
            @Qualifier("inMemoryPlayerRepository") PlayerRepository playerRepository
    ) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Set<Game> getGames() {
        return gameRepository.getGames().stream()
                .filter(game -> game.getPlayers().size() < 2)
                .collect(Collectors.toSet());
    }

    public Set<Game> getLobbyGames() {
        return gameRepository.getGames().stream()
                .filter(game -> game.getStatus().equals(GameStatus.WAITING_FOR_PLAYERS))
                .collect(Collectors.toSet());
    }

    public Game createGame(UUID playerId, String gameName) {
        Optional<Player> player = playerRepository.getPlayerById(playerId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerId + " not found");
        }
        return gameRepository.createGame(player.get(), gameName);
    }

    public Game getGameById(UUID id) {
        Optional<Game> game = gameRepository.getGameById(id);
        if (game.isEmpty()) {
            throw new HangmanException("Game with id " + id + " not found");
        }
        return game.get();
    }

    public void addPlayerToGame(UUID gameId, UUID playerId) {
        Optional<Player> player = playerRepository.getPlayerById(playerId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerId + " not found");
        }
        gameRepository.addPlayerToGame(gameId, player.get());
    }

    public void removeGame(UUID gameId) {
        gameRepository.removeGame(gameId);
    }

    public WordToGuess getWordToGuess(Game game) {
        return gameRepository.getWordToGuess(game);
    }

    public WordToGuess guessLetter(Game game, char letter) {
        WordToGuess wordToGuess = getWordToGuess(game);
        if (wordToGuess.addGuessedLetter(letter)) {
            String newState = wordToGuess.getWord().chars()
                    .mapToObj(c -> wordToGuess.getGuessedLetters().contains((char) c) ? (char) c : '_')
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
            return gameRepository.updateWordToGuessState(game, newState);
        } else {
            throw new HangmanException("Letter " + letter + " already guessed");
        }
    }

}
