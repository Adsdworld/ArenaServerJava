package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.game.entity.Entity;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.champion.Garen;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.arena.game.entity.EntityPositions.*;

public class JoinHandler implements IMessageHandler {
    public void handle(Message message) {

        /* Get the server instance */
        Server server = Server.getInstance();

        /* Get the game by GameNameEnum */
        Game game = server.gameExists(message.getGameName());

        /* Prepare the response */
        Response response = new Response();

        if (game != null) {

            /* Get the GameNameEnum of the game */
            GameNameEnum gameNameEnum = game.getGameNameEnum();

            /* Create a Player that represent the Unity client */
            Player player = new Player(message.getUuid());

            /* Check if the player already exists in the game */
            LivingEntity existsInGame = game.livingEntityAlreadyExists(player.getUuid());

            response.setGameName(gameNameEnum);

            if (existsInGame == null) {

                // TODO: Permettre au joueur de choisir son équipe
                // rajouter un propriété dans le message
                // rajouter un élément dans l'interface ServerSelector pour choisir BLEU ROUGE SPECTATEUR
                int team = ThreadLocalRandom.current().nextInt(2) + 1;

                /* Create entity  and add it to game */
                Garen garen = new Garen(player.getUuid(), team);

                switch (team) {
                    case 1:
                        garen.setPos(BLUE_SPAWN);
                        break;
                    case 2:
                        garen.setPos(RED_SPAWN);
                        break;
                    default:
                        garen.setPos(CENTER_SPAWN);
                        Logger.warn("Team not specified for player " + player.getUuid() + ", defaulting to CENTER_SPAWN.");
                        break;
                }

                /* Add the new entity to the game */
                game.addEntity(garen);

                /* Notify all players of the game */
                response.setNotify(garen.getName() + " " + garen.getId()  + " Joined "+ gameNameEnum.getGameName() + " in team " + team);
                response.setResponse(ResponseEnum.Joined);

                Logger.game(garen.getName() + " " + garen.getId()  + " Joined "+ gameNameEnum.getGameName() + " in team " + team, gameNameEnum);

            } else {
                /* Notify all players of the game */
                response.setNotify(existsInGame.getName() + " " + existsInGame.getId()  + " Rejoined "+ gameNameEnum.getGameName() + " in team " + existsInGame.getTeam());
                response.setResponse(ResponseEnum.PlayerAlreadyInGame);

                Logger.game(existsInGame.getName() + " " + existsInGame.getId()  + " Rejoined "+ gameNameEnum.getGameName() + " in team " + existsInGame.getTeam(), gameNameEnum);
            }

            /* Clear the Unity game of the player */
            game.clearUnityGame(player);

            /* Subscribe player to the game */
            server.subscribePlayerToGame(player, game);

            /* Send the notification */
            response.Send(gameNameEnum);

            /* Assign the new entity to the player */
            game.yourEntityIs(player.getUuid(), response.getGameName());

            // TODO : improve message add possibility to choose team and champion


        } else {
            // TODO: improve this response create a special ResponseEnum for this case and handle it in the client unity
            response.setResponse(ResponseEnum.GameNotFound);
            response.setNotify("Couldn't not " + message.getAction() + " game " + message.getGameName().getGameName());
            response.setGameName(message.getGameName());
            response.Send(message.getUuid());

            Logger.failure("Couldn't find game " + message.getGameName());
        }
    }
}
