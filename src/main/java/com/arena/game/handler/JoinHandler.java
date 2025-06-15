package com.arena.game.handler;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.game.entity.EntityPositions;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.champion.Garen;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class JoinHandler implements IMessageHandler {
    /**
     * Handle the {@link com.arena.player.ActionEnum#Join}  for a {@link Player}  in a {@link Game}.
     *
     * @param message the {@link Message} containing the join action details.
     * @implNote This method processes the join action for a player, checking if the game exists, if the player is already in the game, and if not, creating a new entity for the player and adding it to the game.
     * @author A.SALLIER
     * @date 2025-06-15
     */
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

                // TODO: Permettre au joueur de choisir son équipe : bien pour faire une demo, car aucun outil pour forcer une équipe
                // rajouter un propriété dans le message
                // rajouter un élément dans l'interface ServerSelector pour choisir BLEU ROUGE SPECTATEUR

                int blue = game.getPlayersOfTeam(1).size();
                int red = game.getPlayersOfTeam(2).size();

                int team;

                Collection<LivingEntity> buildings = new ArrayList<>();

                if (blue >= red) {
                    team = 2;
                    buildings.addAll(game.getLivingEntityByGeneralId(EntityPositions.RED_NEXUS.keySet()));
                    buildings.addAll(game.getLivingEntityByGeneralId(EntityPositions.RED_INHIBITORS.keySet()));

                } else {
                    team = 1;
                    buildings.addAll(game.getLivingEntityByGeneralId(EntityPositions.BLUE_NEXUS.keySet()));
                    buildings.addAll(game.getLivingEntityByGeneralId(EntityPositions.BLUE_INHIBITORS.keySet()));
                }

                if (blue == 0 || red == 0) {
                    for (LivingEntity building : buildings) {
                        building.setSkinAnimation(building.getSkinAnimationForSpawn());
                        building.lockSkinAnimation(building.getSkinAnimationDurationForSpawn(), () -> {
                            building.setSkinAnimation(building.getSkinAnimationForIdle());
                        });
                    }
                }

                /* Create entity  and add it to game */
                Garen garen = new Garen(player.getUuid(), team);

                garen.spawnAtTeamSpawn();

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
            response.send(gameNameEnum);

            /* Assign the new entity to the player */
            game.yourEntityIs(player.getUuid());

            // TODO : improve message add possibility to choose team and champion


        } else {
            // TODO: improve this response create a special ResponseEnum for this case and handle it in the client unity
            response.setResponse(ResponseEnum.GameNotFound);
            response.setNotify("Could not Join : " + message.getGameName().getGameName() + " not found, please create it first.");
            response.setGameName(message.getGameName());
            response.send(message.getUuid());

            Logger.failure("Could not Join : " + message.getGameName().getGameName() + " not found, please create it first.");
        }
    }
}
