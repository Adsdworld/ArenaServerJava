package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.game.entity.LivingEntity;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;

public class PlayerStateUpdateHandler implements IMessageHandler {
    public void handle(Message message) {
        Server server = Server.getInstance();

        Player player = server.getPlayerByUuid(message.getUuid());

        if (player != null) {

            Game game = server.getGameOfPlayer(player);

            if (game != null) {
                LivingEntity livingEntity = game.getLivingEntity(player);
                if (livingEntity != null) {
                    livingEntity.update(message.getLivingEntity());
                    /* Update is send to players in the game by the Core when all actions have been processed */
                } else {
                    Response response = new Response();
                    response.setResponse(ResponseEnum.Info);
                    response.setNotify("Living entity not found for player: " + message.getUuid() + ". Please check if you have sent a Join Action.");
                    response.Send(player.getUuid());
                }
            } else {
                Response response = new Response();
                response.setResponse(ResponseEnum.Info);
                response.setNotify("Game not found for player: " + message.getUuid() + ". Please check if you have sent a Join Action.");
                response.Send(player.getUuid());
            }
        }
    }
}
