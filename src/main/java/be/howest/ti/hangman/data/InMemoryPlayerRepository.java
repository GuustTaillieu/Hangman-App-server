package be.howest.ti.hangman.data;

import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("inMemoryPlayerRepository")
public class InMemoryPlayerRepository implements PlayerRepository {

    private final Set<Player> players = new HashSet<>();

    @Override
    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public Optional<Player> getPlayerById(UUID playerId) {
        return players.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst();
    }

    @Override
    public Optional<Player> getPlayerBySessionId(String sessionId) {
        return players.stream()
                .filter(player -> player.getSessionId().equals(sessionId))
                .findFirst();
    }

    @Override
    public void removePlayer(UUID playerId) {
        Optional<Player> player = getPlayerById(playerId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerId + " not found");
        }
        players.remove(player.get());
    }

    @Override
    public int getScore(UUID playerId) {
        Optional<Player> player = getPlayerById(playerId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerId + " not found");
        }
        return player.get().getScore();
    }

    @Override
    public void editScore(UUID playerId, int score) {
        Optional<Player> player = getPlayerById(playerId);
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerId + " not found");
        }
        player.get().setScore(score);
    }
}
