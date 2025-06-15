package com.arena.game.handler;

import com.arena.network.message.Message;
import com.arena.server.Server;

public class CloseGameHandler implements IMessageHandler {
    /**
     * Handle the close game action.
     *
     * @param message the {@link Message} containing the close game action details.
     * @implNote This method call {@link Server#closeGame(Message)}.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void handle(Message message) {
        Server server = Server.getInstance();

        server.closeGame(message);
    }
}
