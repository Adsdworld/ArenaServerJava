package com.arena.game.handler;

import com.arena.network.message.Message;
import com.arena.server.Server;
import com.arena.utils.Logger;

public class CooldownStartHandler implements IMessageHandler {
    public void handle(Message message) {

        Logger.info("Entering CooldownStartHandler with message: " + message);
        Server server = Server.getInstance();


        boolean gameFound = server.getGames().stream().anyMatch(game -> {
            if (game.getGameNameEnum().equals(message.getGameName())) {
                Logger.info("Game name found: " + message.getGameName());

                game.getLivingEntities().stream()
                        .filter(entity -> entity.getId().equals(message.getUuid()))
                        .findFirst()
                        .ifPresent(entity -> {
                            entity.setCooldownQStart(message.getCooldownQStart());
                            entity.setCooldownWStart(message.getCooldownWStart());
                            entity.setCooldownEStart(message.getCooldownEStart());
                            entity.setCooldownRStart(message.getCooldownRStart());

                            Logger.info("setcooldownWEnd: " + message.getCooldownQStart() + entity.getCooldownWMs());

                            entity.setCooldownQEnd(message.getCooldownQStart() + entity.getCooldownQMs());
                            entity.setCooldownWEnd(message.getCooldownWStart() + entity.getCooldownWMs());
                            entity.setCooldownEEnd(message.getCooldownEStart() + entity.getCooldownEMs());
                            entity.setCooldownREnd(message.getCooldownRStart() + entity.getCooldownRMs());
                        });

                return true;
            }
            return false;
        });

        if (!gameFound) {
            Logger.info("Game not found for CooldownStartHandler: " + message.getGameName());
        }


    }
}
