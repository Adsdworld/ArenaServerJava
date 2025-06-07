package com.arena.game;

import com.arena.game.entity.Entity;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.champion.Garen;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Game {
    /* Identifier for the game */
    GameNameEnum gameNameEnum;

    /* Status of the game */
    GameStatusEnum gameStatusEnum;

    /* Teams in the game */
    private ArrayList<LivingEntity> livingEntities;

    private ArrayList<Player> players;

    private static final float X_BLUE = 272.9823f;
    private static final float Y_BLUE = 5.0f;
    private static final float Z_BLUE = 300.6143f;

    private static final float X_RED = 723.0251f;
    private static final float Y_RED = 5.0f;
    private static final float Z_RED = 752.9824f;


    /*
        Ou définir les tours hinibiteurs, nexus

        Ou les sbires ?*/

    private static final float X_TOWER = 534.0f;
    private static final float Y_TOWER = 5.0f;
    private static final float Z_TOWER = 558.0f;



    /**
     * TODO: Faire que ce soit le constructeur qui envoie creating game puis un all to when created
     * @param gameNameEnum
     */
    public Game(GameNameEnum gameNameEnum) {
        this.gameStatusEnum = GameStatusEnum.Creating;
        this.gameNameEnum = gameNameEnum;

        this.livingEntities = new ArrayList<>();
        this.players = new ArrayList<>();

        this.gameStatusEnum = GameStatusEnum.Created;
    }


    /**
     * Add a player to the {@link Game} .
     *
     * @param player the {@link Player} to add.
     * @param team {@code int}.
     * @implNote This method checks if the {@link Player} already exists in the {@link Game} , if not, it creates a new {@link Entity} , notifies all players of the {@link Game} , and sends a {@link Response}  to the player indicating the {@link Entity}  he is controlling else it notifies all players that the player has rejoined the {@link Game}  and sends a {@link Response}  to the player indicating that he has rejoined the {@link Game} .
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public void addPlayer(Player player, int team) {

        LivingEntity existsInGame = livingEntityAlreadyExists(player.getUuid());

        Response response = new Response();
        response.setGameName(gameNameEnum);

        if (existsInGame == null) {
            /* Create entity  and add it to game */
            Garen garen = new Garen(player.getUuid(), team);


            switch (team) {
                case 1:
                    garen.setPosX(X_BLUE);
                    garen.setPosY(Y_BLUE);
                    garen.setPosZ(Z_BLUE);
                    break;
                case 2:
                    garen.setPosX(X_RED);
                    garen.setPosY(Y_RED);
                    garen.setPosZ(Z_RED);
                    break;
                default:
                    garen.setPosX(X_TOWER);
                    garen.setPosY(Y_TOWER);
                    garen.setPosZ(Z_TOWER);
            }


            Gson gson = new Gson();
            Logger.info(gson.toJson(garen));

            addEntity(garen);

            /* Register player to the game */
            players.add(player);

            /* Notify all players of the game */
            response.setNotify(garen.getName() + " " + garen.getId()  + " Joined "+ gameNameEnum.getGameName() + " in team " + team);
            response.setResponse(ResponseEnum.Joined);
            response.Send(gameNameEnum);

            Logger.game(garen.getName() + " " + garen.getId()  + " Joined "+ gameNameEnum.getGameName() + " in team " + team, gameNameEnum);
        } else {
            /* Notify all players of the game */
            response.setNotify(existsInGame.getName() + " " + existsInGame.getId()  + " Rejoined "+ gameNameEnum.getGameName() + " in team " + existsInGame.getTeam());

            //TODO: it might be better to send a ResponseEnum.ReJoined instead PlayerAlreadyInGame
            response.setResponse(ResponseEnum.PlayerAlreadyInGame);

            Logger.game(existsInGame.getName() + " " + existsInGame.getId()  + " Rejoined "+ gameNameEnum.getGameName() + " in team " + existsInGame.getTeam(), gameNameEnum);
        }

        /* Assign the new entity to the player */
        yourEntityIs(response.getUuid(), response.getGameName());

        // TODO : improve message add possibility to choose team and champion

    }

    /**
     * Send a response to the client indicating the entity he is controlling in the game.
     *
     * @param entityId the {@link String}  of the entity.
     * @param gameName the {@link GameNameEnum}  to which the entity belongs.
     * @implNote This method checks if the {@link Game}  exists and sends a response with the {@code entityId} .
     * @author A.SALLIER
     * @date 2025-06-07
     */
    private void yourEntityIs(String entityId, GameNameEnum gameName) {

        Game game = Server.getInstance().gameExists(gameNameEnum);

        Response response = new Response();
        response.setGameName(gameNameEnum);

        if (game != null) {
            response.setResponse(ResponseEnum.YourEntityIs);
            response.setUuid(entityId);
        } else {
            response.setUuid("Entity_default");
        }
        response.Send(entityId);
    }

    /**
     * Adds a {@link LivingEntity} to the game.
     *
     * @param livingEntity the {@link LivingEntity} to add.
     * @implNote This method checks if the {@link LivingEntity} already exists in the {@link Game} . If it does not, it adds the {@link LivingEntity}  to the game and logs the action. If it does exist, it logs a warning message.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public void addEntity(LivingEntity livingEntity) {

        LivingEntity existsInGame = livingEntityAlreadyExists(livingEntity.getId());

        if (existsInGame == null) {
            if (livingEntity != null) {
                livingEntities.add(livingEntity);
                Logger.game(livingEntity.getName() + " " + livingEntity.getId() + " added to game " + gameNameEnum.getGameName(), gameNameEnum);
            } else {
                // TODO: add a failure for game Logger.failure("some failure message", gameNameEnum);
                Logger.failure("Cannot add null entity to game " + gameNameEnum.getGameName());
            }
        } else {
            Logger.warn(livingEntity.getName() + " " + livingEntity.getId() + " already exists in game " + gameNameEnum.getGameName());
        }
    }

    /**
     * Checks if a {@link LivingEntity} with the given {@code id} already exists in the game.
     *
     * @param id the {@code String} identifier of the LivingEntity to check
     * @return the {@link LivingEntity} if it exists; {@code null} otherwise
     * @implNote This method iterates through the list of {@code livingEntities}  in the game to find a match.
     * @author A.Sallier
     * @date 2025-06-07
     */
    public LivingEntity livingEntityAlreadyExists(String id) {
        LivingEntity result = null;

        Game game = Server.getInstance().gameExists(gameNameEnum);

        if (game == null) {
            Logger.warn("Game " + gameNameEnum.getGameName() + " does not exist.");
        } else {
            for (LivingEntity livingEntity : livingEntities) {
                if (livingEntity.getId().equals(id)) {
                    result = livingEntity;
                    break; // on peut sortir dès qu'on a trouvé
                }
            }
        }

        return result;
    }

    // Getters and Setters
    public GameNameEnum getGameNameEnum() {
        return gameNameEnum;
    }
    public void setGameNameEnum(GameNameEnum gameNameEnum) {
        this.gameNameEnum = gameNameEnum;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<LivingEntity> getLivingEntities() {
        return livingEntities;
    }
}
