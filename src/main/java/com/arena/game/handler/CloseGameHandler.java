package com.arena.game.handler;

import com.arena.game.core.Core;
import com.arena.network.message.Message;
import com.arena.server.Server;

public class CloseGameHandler implements IMessageHandler {
    public void handle(Message message) {
        Server server = Server.getInstance();

        server.closeGame(message);
    }
}
