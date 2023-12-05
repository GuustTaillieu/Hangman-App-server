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

    public Game createGame(UUID hostId, String gameName) {
        Optional<Player> player = playerRepository.getPlayerById(hostId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + hostId + " not found");
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

    public void removePlayerFromGame(Game game, Player player) {
        game.removePlayer(player);
        player.setGame(null);
        if (game.getPlayers().isEmpty() || game.getHost().equals(player)) {
            removeGame(game);
        }
    }

    public void removeGame(Game game) {
        gameRepository.removeGame(game);
    }

    public void guessLetter(Game game, Player player, char letter) {
        WordToGuess wordToGuess = gameRepository.getWordToGuess(game);
        wordToGuess.guessLetter(player, letter);
        if (allPlayersDoneWithWord(wordToGuess)) {
            game.incrementCurrentWordToGuessIndex();
        }
    }

    private boolean allPlayersDoneWithWord(WordToGuess wordToGuess) {
        return wordToGuess.getBelongingGame().getPlayers().stream().allMatch(wordToGuess::isDoneWithWord);
    }

    public void addWordToGame(Game game, UUID playerUUID, String word) {
        Optional<Player> player = playerRepository.getPlayerById(playerUUID);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerUUID + " not found");
        }
        if (game.getWords().stream().anyMatch(w -> w.getOwner().getId().equals(playerUUID))) {
            throw new HangmanException("Player already sent a word");
        }
        game.addWordToGuess(player.get(), word);
        if (everyPlayerHasSentAWord(game)) {
            game.setStatus(GameStatus.STARTED);
        }
    }

    private boolean everyPlayerHasSentAWord(Game game) {
        return game.getWords().stream().map(WordToGuess::getOwner).collect(Collectors.toSet()).equals(game.getPlayers());
    }

    public void nextWord(Game game) {
        game.incrementCurrentWordToGuessIndex();
    }

    public void resetGame(Game game) {
        game.setStatus(GameStatus.WAITING_FOR_PLAYERS);
        game.getWords().clear();
        game.setCurrentWordToGuessIndex(0);
    }
}
