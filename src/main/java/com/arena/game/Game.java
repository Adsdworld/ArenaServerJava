package com.arena.game;

import com.arena.game.entity.Entity;
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
    private ArrayList<Entity> entities;

    private ArrayList<Player> players;



    /**
     * TODO: Faire que ce soit le constructeur qui envoie creating game puis un all to when created
     * @param gameNameEnum
     */
    public Game(GameNameEnum gameNameEnum) {
        this.gameStatusEnum = GameStatusEnum.Creating;
        this.gameNameEnum = gameNameEnum;

        this.entities = new ArrayList<>();
        this.players = new ArrayList<>();

        this.gameStatusEnum = GameStatusEnum.Created;
    }


    /**
     * Add a player to the game Entity should have the player's UUID
     * default champion is Garen
     * @param player
     */
    public void addPlayer(Player player, int team) {

        Response response = new Response();
        response.setResponse(ResponseEnum.Joined);
        response.setGameName(gameNameEnum);

        if (!isUUIDAlreadyInGame(player.getUuid())) {
            Garen garen = new Garen(player.getUuid(), team);
            addEntity(garen);
            players.add(player);
        } else {
            Logger.warn("Player with UUID " + player.getUuid() + " is already in the game " + gameNameEnum.getGameName());
        }

        // TODO : improve message add possibility to choose team and champion
        response.setNotify("Joined " + gameNameEnum.getGameName() + " as Garen in team " + team);
        response.Send(gameNameEnum);

        Logger.game("Player with UUID " + player.getUuid() + " added to game " + gameNameEnum.getGameName(), gameNameEnum);
    }

    /**
     * Create a new entity in the game, with a uuid non exitent
     * @return the created entity
     */
    public void addEntity(Entity entity) {
        if (!isUUIDAlreadyInGame(entity.getId())) {
            entities.add(entity);
            Logger.info("Entity with ID " + entity.getId() + " added to game " + gameNameEnum.getGameName());
        } else {
            Logger.warn("Entity with ID " + entity.getId() + " is already in the game " + gameNameEnum.getGameName());
        }
    }

    public boolean isUUIDAlreadyInGame(String uuid) {
        for (Entity entity : entities) {
            if (entity.getId().equals(uuid)) {
                return true;
            }
        }
        return false;
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
