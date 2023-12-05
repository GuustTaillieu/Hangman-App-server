package be.howest.ti.hangman.data;

import be.howest.ti.hangman.model.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PlayerRepository {

    Set<Player> getPlayers();
    void addPlayer(Player player);
    Optional<Player> getPlayerById(UUID playerId);
    Optional<Player> getPlayerBySessionId(String sessionId);
    void removePlayer(UUID playerId);
    int getScore(UUID playerId);
    void editScore(UUID playerId, int score);

}
