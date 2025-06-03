package com.arena.game;

import com.arena.game.entity.Entity;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.champion.Garen;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.utils.Logger;

import java.util.ArrayList;
import java.util.UUID;

public class Game {
    /* Identifier for the game */
    GameNameEnum gameNameEnum;

    /* Status of the game */
    GameStatusEnum gameStatusEnum;

    /* Teams in the game */
    private ArrayList<LivingEntity> livingEntities;

    private ArrayList<Player> players;



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
     * Add a player to the game Entity should have the player's UUID
     * default champion is Garen
     * @param player
     */
    public void addPlayer(Player player, int team) {

        LivingEntity existsInGame = LivingEntityAlreadyExists(player.getUuid());

        Response response = new Response();
        response.setGameName(gameNameEnum);

        if (existsInGame == null) {
            Garen garen = new Garen(player.getUuid(), team);
            addEntity(garen);
            players.add(player);
            response.setNotify(garen.getName() + " " + garen.getId()  + " Joined "+ gameNameEnum.getGameName() + " in team " + team);
            response.setResponse(ResponseEnum.Joined);
            response.Send(gameNameEnum);
            Logger.game(garen.getName() + " " + garen.getId()  + " Joined "+ gameNameEnum.getGameName() + " in team " + team, gameNameEnum);
        } else {
            response.setNotify(existsInGame.getName() + " " + existsInGame.getId()  + " Rejoined "+ gameNameEnum.getGameName() + " in team " + existsInGame.getTeam());
            response.setResponse(ResponseEnum.PlayerAlreadyInGame);
            Logger.game(existsInGame.getName() + " " + existsInGame.getId()  + " Rejoined "+ gameNameEnum.getGameName() + " in team " + existsInGame.getTeam(), gameNameEnum);
        }

        // TODO : improve message add possibility to choose team and champion

    }

    /**
     * Create a new entity in the game, with a uuid non exitent
     * @return the created entity
     */
    public void addEntity(LivingEntity livingEntity) {

        LivingEntity existsInGame = LivingEntityAlreadyExists(livingEntity.getId());

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

    public LivingEntity LivingEntityAlreadyExists(String uuid) {
        for (LivingEntity livingEntity : livingEntities) {
            if (livingEntity.getId().equals(uuid)) {
                return livingEntity;
            }
        }
        return null;
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
}
