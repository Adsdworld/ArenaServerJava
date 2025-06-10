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
                            entity.setCooldownQStart(message.getLivingEntity().getCooldownQStart());
                            entity.setCooldownWStart(message.getLivingEntity().getCooldownWStart());
                            entity.setCooldownEStart(message.getLivingEntity().getCooldownEStart());
                            entity.setCooldownRStart(message.getLivingEntity().getCooldownRStart());

                            Logger.info("setcooldownWEnd: " + message.getLivingEntity().getCooldownQStart() + entity.getCooldownWMs());

                            entity.setCooldownQEnd(message.getLivingEntity().getCooldownQStart() + entity.getCooldownQMs());
                            entity.setCooldownWEnd(message.getLivingEntity().getCooldownWStart() + entity.getCooldownWMs());
                            entity.setCooldownEEnd(message.getLivingEntity().getCooldownEStart() + entity.getCooldownEMs());
                            entity.setCooldownREnd(message.getLivingEntity().getCooldownRStart() + entity.getCooldownRMs());
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
