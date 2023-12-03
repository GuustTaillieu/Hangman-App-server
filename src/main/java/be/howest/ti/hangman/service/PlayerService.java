package be.howest.ti.hangman.service;

import be.howest.ti.hangman.data.PlayerRepository;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(@Qualifier("inMemoryPlayerRepository") PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void addPlayer(Player player) {
        playerRepository.addPlayer(player);
    }

    public Set<Player> getPlayers() {
        return playerRepository.getPlayers();
    }

    public Player getPlayerById(UUID playerId) {
        Optional<Player> player = playerRepository.getPlayerById(playerId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerId + " not found");
        }
        return player.get();
    }

    public Player getPlayerBySessionId(String sessionId) {
        Optional<Player> player = playerRepository.getPlayerBySessionId(sessionId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with sessionId " + sessionId + " not found");
        }
        return player.get();
    }

    public void removePlayer(UUID playerId) {
        playerRepository.removePlayer(playerId);
    }

    public void editScore(UUID playerId, int pointScored) {
        if (pointScored < 0) {
            throw new HangmanException("Score cannot be negative");
        }
        int prevScore = playerRepository.getScore(playerId);
        playerRepository.editScore(playerId, prevScore + pointScored);
    }
}
