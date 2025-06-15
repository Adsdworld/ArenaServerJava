package com.arena.game.handler;

import com.arena.network.message.Message;
import com.arena.server.Server;

public class CreateGameHandler implements IMessageHandler {
    /**
     * Handle the create game action.
     *
     * @param message the {@link Message} containing the create game action details.
     * @implNote This method call {@link Server#createGame(Message)}.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void handle(Message message) {
        Server server = Server.getInstance();

        server.createGame(message);
    }
}
