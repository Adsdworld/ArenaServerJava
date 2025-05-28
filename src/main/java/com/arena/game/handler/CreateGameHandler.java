package com.arena.game.handler;

import com.arena.game.core.Core;
import com.arena.network.message.Message;
import com.arena.server.Server;
import com.arena.utils.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CreateGameHandler implements IMessageHandler {
    public void handle(Message message) {
        Server server = Server.getInstance();

        if (server.isCreatingGame()) {
            retryLater(message); // Réessaie après 100ms
            return;
        }

        server.createGame(message);
    }

    private void retryLater(Message message) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Core.getInstance().receive(message);
            scheduler.shutdown();
        }, 100, TimeUnit.MILLISECONDS);
    }
}
