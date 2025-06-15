package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.game.entity.LivingEntity;
import com.arena.network.message.Message;
import com.arena.player.Player;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;

public class CastQHandler implements IMessageHandler {

    /**
     * Handle the cast Q action for a player.
     *
     * @param message the {@link Message} containing the cast action details.
     * @implNote This method processes the cast Q action for a player, updating the cooldown start and end times for the player's living entity in the game.
     * @author A.SALLIER
     * @date 2025-06-12
     */
    public void handle(Message message) {

        Server server = Server.getInstance();

        Player player;
        Game game;

        if (server != null
                && (player = server.getPlayerByUuid(message.getUuid())) != null
                && (game = server.getGameOfPlayer(player)) != null) {
            LivingEntity entity = game.getLivingEntity(player);

            if (entity != null) {
                Logger.info("Player " + player.getUuid() + " is casting Q with entity ID: " + entity.getId() + " at " + message.getLivingEntity().getCooldownQStart() + " in game " + game.getGameNameEnum());
                long castStart = message.getLivingEntity().getCooldownQStart();
                long castDuration = entity.getCooldownQMs();
                long castEnd = castStart + castDuration;

                if (castStart >= entity.getCooldownQEnd() && !entity.isLocked() && !entity.isCastLocked()) {
                    entity.lockEntityCast(true);

                    entity.setCooldownQStart(castStart);
                    entity.setCooldownQEnd(castEnd);

                    entity.useQ();

                    /* Set the skin animation */
                    entity.setSkinAnimation(entity.getSkinAnimationForQ());

                    /* Lock the skin animation */
                    entity.lockSkinAnimation(entity.getSkinAnimationDurationForQ(), () -> entity.lockEntityCast(false));
                }
            } else {
                Logger.error("LivingEntity not found for player " + player.getUuid() + " in game " + game.getGameNameEnum());
            }
        }
    }
}
