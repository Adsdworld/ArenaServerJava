package com.arena.game;

import com.arena.game.entity.Entity;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.champion.Garen;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;
import static com.arena.game.entity.EntityPositions.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Vector;

public class Game {
    /* Identifier for the game */
    GameNameEnum gameNameEnum;

    /* Status of the game */
    GameStatusEnum gameStatusEnum;

    /* Teams in the game */
    private ArrayList<LivingEntity> livingEntities;

    private ArrayList<Player> players;

    /**
     * @param gameNameEnum
     */
    public Game(GameNameEnum gameNameEnum) {
        this.gameStatusEnum = GameStatusEnum.Creating;
        this.gameNameEnum = gameNameEnum;

        this.livingEntities = new ArrayList<>();
        this.players = new ArrayList<>();

        this.gameStatusEnum = GameStatusEnum.Created;
    }

    public LivingEntity getLivingEntity(Player player) {
        LivingEntity livingEntity = getLivingEntities().stream()
                .filter(entity -> entity.getId().equals(player.getUuid()))
                .findFirst()
                .orElse(null);
        if (livingEntity == null) {
            Response response = new Response();
            response.setResponse(ResponseEnum.Info);
            response.setNotify("Living entity not found for player: " + player.getUuid() + ". Please check if you have sent a Join Action.");
            response.Send(player.getUuid());
            Logger.warn("Living entity not found for player: " + player.getUuid() + ". Please check if you have sent a Join Action.");
        }
        return livingEntity;
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
    public void yourEntityIs(String entityId, GameNameEnum gameName) {

        Game game = Server.getInstance().gameExists(gameNameEnum);

        Response response = new Response();
        response.setGameName(gameNameEnum);

        if (game != null) {
            response.setResponse(ResponseEnum.YourEntityIs);
            response.setText(entityId);
        } else {
            response.setText("default");
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

    public void clearLivingEntities() {
        livingEntities.clear();
        Logger.game("Living entities cleared in game " + gameNameEnum.getGameName(), gameNameEnum);
    }

    /**
     * Clears the Unity game state for a specific player.
     * This method sends a response to the player to clear all entities in the Unity client game by sending an empty list of living entities.
     *
     * @param player the {@link Player} for whom the game state should be cleared.
     * @implNote This method creates a new {@link Response} object, sets the response type to {@link ResponseEnum#GameState}, and sends an empty list of living entities to the specified player.
     * @author A.SALLIER
     * @date 2025-06-09
     */
    public void clearUnityGame(Player player) {
        Response response = new Response();
        response.setResponse(ResponseEnum.GameState);
        response.setGameName(gameNameEnum);
        response.setLivingEntities(new ArrayList<>());
        response.Send(player.getUuid(), true);
    }

    /**
     * Clears the Unity game state for all players in the game.
     * This method sends a response to all players to clear all entities in the Unity client game by sending an empty list of living entities.
     *
     * @implNote This method creates a new {@link Response} object, sets the response type to {@link ResponseEnum#GameState}, and sends an empty list of living entities to all players in the game.
     * @author A.SALLIER
     * @date 2025-06-09
     */
    public void clearUnityGame(Game game) {
        game.clearLivingEntities();
        Response response = new Response();
        response.setResponse(ResponseEnum.GameState);
        response.setGameName(gameNameEnum);
        response.setLivingEntities(new ArrayList<>());
        response.Send(game.getGameNameEnum(), true);
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
