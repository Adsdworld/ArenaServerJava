package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.network.message.Message;
import com.arena.player.Player;
import com.arena.server.Server;
import com.arena.utils.Logger;

public class JoinHandler implements IMessageHandler {
    public void handle(Message message) {
        Game game = Server.getInstance().gameExists(message.getGameName());
        if (game != null) {
            Player player = new Player(message.getUuid());
            game.addPlayerToBlueTeam(player);
            Logger.info("Player " + player.getUuid() + " joined game " + message.getGameName());
        } else {
            Logger.failure("Couldn't find game " + message.getGameName());
        }
    }
}
