package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class JoinHandler implements IMessageHandler {
    public void handle(Message message) {
        Game game = Server.getInstance().gameExists(message.getGameName());

        Response response = new Response();
        response.setResponse(ResponseEnum.Info);

        if (game != null) {
            Player player = new Player(message.getUuid());

            // TODO: Permettre au joueur de choisir son équipe
            // rajouter un propriété dans le message
            // rajouter un élément dans l'interface ServerSelector pour choisir BLEU ROUGE SPECTATEUR



            int team = ThreadLocalRandom.current().nextInt(3);

            switch (team) {
                case 0:
                    if (game.getSpectators().contains(player)) {
                        game.getBlueTeam().removeIf(p -> p.getUuid().equals(player.getUuid()));
                        game.getRedTeam().removeIf(p -> p.getUuid().equals(player.getUuid()));
                        response.setNotify("You are already spectating the game " + message.getGameName().getGameName());
                        return;
                    }
                    game.addSpectator(player);
                    response.setNotify("You are now spectating the game " + message.getGameName().getGameName());
                    break;
                case 1:
                    if (game.getBlueTeam().contains(player)) {
                        game.getRedTeam().removeIf(p -> p.getUuid().equals(player.getUuid()));
                        game.getSpectators().removeIf(p -> p.getUuid().equals(player.getUuid()));
                        response.setNotify("You are already in the blue team of the game " + message.getGameName().getGameName());
                        return;
                    }
                    game.addPlayerToBlueTeam(player);
                    response.setNotify("You are now in the blue team of the game " + message.getGameName().getGameName());
                    break;
                case 2:
                    if (game.getRedTeam().contains(player)) {
                        game.getBlueTeam().removeIf(p -> p.getUuid().equals(player.getUuid()));
                        game.getSpectators().removeIf(p -> p.getUuid().equals(player.getUuid()));
                        response.setNotify("You are already in the red team of the game " + message.getGameName().getGameName());
                        return;
                    }
                    game.addPlayerToRedTeam(player);
                    response.setNotify("You are now in the red team of the game " + message.getGameName().getGameName());
                    break;
            }


            Logger.server("Player " + player.getUuid() + " joined game " + message.getGameName());


        } else {
            response.setNotify("Couldn't find game " + message.getGameName().getGameName());
            Logger.failure("Couldn't find game " + message.getGameName());
        }
    }
}
