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

        if (game != null) {
            Player player = new Player(message.getUuid());

            // TODO: Permettre au joueur de choisir son équipe
            // rajouter un propriété dans le message
            // rajouter un élément dans l'interface ServerSelector pour choisir BLEU ROUGE SPECTATEUR



            int team = ThreadLocalRandom.current().nextInt(2);

            switch (team) {
                case 0:
                    game.addPlayer(player, 1);
                    break;
                case 1:
                    game.addPlayer(player, 2);
                    break;
            }

        } else {

            // TODO: improve this response create a special ResponseEnum for this case and handle it in the client unity
            Response response = new Response();
            response.setResponse(ResponseEnum.GameNotFound); /* Basic GameNotFound, bad for context */
            response.setNotify("Couldn't not " + message.getAction() + " game " + message.getGameName().getGameName());
            response.setGameName(message.getGameName());
            response.Send(message.getUuid());

            Logger.failure("Couldn't find game " + message.getGameName());
        }
    }
}
