package be.howest.ti.hangman.data;

import be.howest.ti.hangman.model.Chat;
import be.howest.ti.hangman.model.Game;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryChatRepository implements ChatRepository {

    private final Map<Game, List<Chat>> chats;

    public InMemoryChatRepository() {
        this.chats = new HashMap<>();
    }

    @Override
    public Chat addChat(Game game, Chat chat) {
        if (!chats.containsKey(game)) {
            List<Chat> chatList = new ArrayList<>();
            chatList.add(chat);
            chats.put(game, chatList);
        } else {
            chats.get(game).add(chat);
        }
        return chat;
    }

    @Override
    public List<Chat> getAllChats(Game game) {
        if (!chats.containsKey(game)) {
            chats.put(game, new ArrayList<>());
        }
        return chats.get(game);
    }
}
