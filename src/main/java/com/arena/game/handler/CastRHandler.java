package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.game.entity.LivingEntity;
import com.arena.network.message.Message;
import com.arena.player.Player;
import com.arena.server.Server;

public class CastRHandler implements IMessageHandler {

    /**
     * Handle the cast R action for a player.
     *
     * @param message the {@link Message} containing the cast action details.
     * @implNote This method processes the cast R action for a player, updating the cooldown start and end times for the player's living entity in the game.
     * @author A.SALLIER
     * @date 2025-06-12
     */
    public void handle(Message message) {

        Server server = Server.getInstance();

        Player player = server.getPlayerByUuid(message.getUuid());

        if (player != null) {

            Game game = server.getGameOfPlayer(player);

            if (game != null) {
                LivingEntity entity = game.getLivingEntity(player);
                if (entity != null) {
                    long castStart = message.getLivingEntity().getCooldownRStart();
                    long castDuration = entity.getCooldownRMs();
                    long castEnd = castStart + castDuration;

                    if (castStart >= entity.getCooldownREnd() && !entity.isLocked() && !entity.isCastLocked()) {
                        entity.lockEntityCast(true);

                        entity.setCooldownRStart(castStart);
                        entity.setCooldownREnd(castEnd);

                        entity.useR();

                        /* Set the skin animation */
                        entity.setSkinAnimation(entity.getSkinAnimationForR());

                        /* Lock the skin animation */
                        entity.LockSkinAnimation(entity.getSkinAnimationDurationForR(), () -> {
                            entity.lockEntityCast(false);
                        });
                    }
                }
            }
        }
    }
}
