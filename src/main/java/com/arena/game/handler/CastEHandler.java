package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.game.entity.LivingEntity;
import com.arena.network.message.Message;
import com.arena.player.Player;
import com.arena.server.Server;

public class CastEHandler implements IMessageHandler {

    /**
     * Handle the cast E action for a player.
     *
     * @param message the {@link Message} containing the cast action details.
     * @implNote This method processes the cast E action for a player, updating the cooldown start and end times for the player's living entity in the game.
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

            long castStart = message.getLivingEntity().getCooldownEStart();
            long castDuration = entity.getCooldownEMs();
            long castEnd = castStart + castDuration;

            if (castStart >= entity.getCooldownEEnd() && !entity.isLocked() && !entity.isCastLocked()) {
                entity.lockEntityCast(true);

                entity.setCooldownEStart(castStart);
                entity.setCooldownEEnd(castEnd);

                entity.useE();

                /* Set the skin animation */
                entity.setSkinAnimation(entity.getSkinAnimationForE());

                /* Lock the skin animation */
                entity.lockSkinAnimation(entity.getSkinAnimationDurationForE(), () -> entity.lockEntityCast(false));
            }
        }
    }
}
