package com.arena.game.handler;

import com.arena.game.core.Core;
import com.arena.network.message.Message;
import com.arena.server.Server;

public class CreateGameHandler implements IMessageHandler {
    public void handle(Message message) {
        Server server = Server.getInstance();

        server.createGame(message);
    }
}
