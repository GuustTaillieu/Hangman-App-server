package be.howest.ti.hangman.data;

import be.howest.ti.hangman.model.Chat;
import be.howest.ti.hangman.model.Game;

import java.util.List;

public interface ChatRepository {

    Chat addChat(Game game, Chat chat);
    List<Chat> getAllChats(Game game);

}
