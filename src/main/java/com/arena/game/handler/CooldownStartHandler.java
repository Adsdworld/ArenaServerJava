package com.arena.game.handler;

import com.arena.network.message.Message;
import com.arena.server.Server;

public class CooldownStartHandler implements IMessageHandler {
    public void handle(Message message) {
        Server server = Server.getInstance();

        server.getGames().stream().anyMatch(game -> {
            if (game.getGameNameEnum().equals(message.getGameName())) {
                game.getLivingEntities().stream().filter(entity -> entity.getId().equals(message.getUuid())).findFirst().ifPresent(entity -> {
                    entity.setCooldownQStart(message.getCooldownQStart()); //TODO: add a check in method if false return now
                    entity.setCooldownWStart(message.getCooldownWStart());
                    entity.setCooldownEStart(message.getCooldownEStart());
                    entity.setCooldownRStart(message.getCooldownRStart());


                    entity.setCooldownQEnd(message.getCooldownQStart() + entity.getCooldownQMs());
                    entity.setCooldownWEnd(message.getCooldownWStart() + entity.getCooldownWMs());
                    entity.setCooldownEEnd(message.getCooldownEStart() + entity.getCooldownEMs());
                    entity.setCooldownREnd(message.getCooldownRStart() + entity.getCooldownRMs());
                });
                return true;
            }
            return false;
        });

    }
}
