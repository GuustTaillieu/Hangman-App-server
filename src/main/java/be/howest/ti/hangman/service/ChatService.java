package be.howest.ti.hangman.service;

import be.howest.ti.hangman.data.ChatRepository;
import be.howest.ti.hangman.data.GameRepository;
import be.howest.ti.hangman.data.PlayerRepository;
import be.howest.ti.hangman.model.Chat;
import be.howest.ti.hangman.model.Game;
import be.howest.ti.hangman.model.Player;
import be.howest.ti.hangman.util.exceptions.HangmanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(
            @Qualifier("inMemoryGameRepository") GameRepository gameRepository,
            @Qualifier("inMemoryPlayerRepository") PlayerRepository playerRepository,
            @Qualifier("inMemoryChatRepository") ChatRepository chatRepository
    ) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.chatRepository = chatRepository;
    }

    public Chat addChat(UUID gameId, UUID playerId, String message) {
        Optional<Game> game = gameRepository.getGameById(gameId);
        Optional<Player> player = playerRepository.getPlayerById(playerId);
        if (game.isEmpty()) {
            throw new HangmanException("Game with id " + gameId + " not found");
        }
        if (player.isEmpty()) {
            throw new HangmanException("Player with id " + playerId + " not found");
        }
        Chat chat = new Chat(message, player.get());
        return chatRepository.addChat(game.get(), chat);
    }

    public List<Chat> getAllChats(UUID gameId) {
        Optional<Game> game = gameRepository.getGameById(gameId);
        if (game.isEmpty()) {
            throw new HangmanException("Game with id " + gameId + " not found");
        }
        return chatRepository.getAllChats(game.get());
    }

}
