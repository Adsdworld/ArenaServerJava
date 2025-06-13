package com.arena.game;

import com.arena.game.entity.LivingEntity;
import com.arena.game.zone.Zone;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    /* Identifier for the game */
    GameNameEnum gameNameEnum;

    /* Status of the game */
    GameStatusEnum gameStatusEnum;

    //private ArrayList<LivingEntity> livingEntities;
    private final ConcurrentHashMap<String, LivingEntity> livingEntities;


    private final ConcurrentHashMap<String, Player> players;

    /**
     * @param gameNameEnum
     */
    public Game(GameNameEnum gameNameEnum) {
        this.gameNameEnum = gameNameEnum;

        this.livingEntities = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();

        this.gameStatusEnum = GameStatusEnum.Created;
    }

    public LivingEntity getLivingEntity(Player player) {
        LivingEntity livingEntity = getLivingEntitiesMap().values().stream()
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
     * @implNote This method checks if the {@link Game}  exists and sends a response with the {@code entityId} .
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public void yourEntityIs(String entityId) {

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

        LivingEntity existsInGame = this.livingEntityAlreadyExists(livingEntity.getId());

        if (existsInGame == null) {
            getLivingEntitiesMap().putIfAbsent(livingEntity.getId(), livingEntity);
            Logger.game(livingEntity.getName() + " " + livingEntity.getId() + " added to game " + gameNameEnum.getGameName(), gameNameEnum);
        } else {
            Logger.warn(livingEntity.getName() + " " + livingEntity.getId() + " already exists in game " + gameNameEnum.getGameName());
        }
    }

    public void removeEntity(LivingEntity livingEntity) {
        if (livingEntity != null) {
            getLivingEntitiesMap().remove(livingEntity.getId());
            Logger.game(livingEntity.getName() + " " + livingEntity.getId() + " removed from game " + gameNameEnum.getGameName(), gameNameEnum);
        } else {
            Logger.failure("Cannot remove null entity from game " + gameNameEnum.getGameName());
        }
    }

    public List<Player> getPlayersOfTeam(int team) {
        return getPlayersMap().values().stream()
                .filter((player -> getLivingEntitiesOfTeam(team).stream()
                        .anyMatch(livingEntity -> livingEntity.getId().equals(player.getUuid()))))
                .toList();
    }

    public List<LivingEntity> getLivingEntitiesOfTeam(int team) {
        return getLivingEntitiesMap().values().stream()
                .filter(entity -> entity.getTeam() == team)
                .toList();
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

        for (LivingEntity livingEntity : getLivingEntitiesMap().values()) {
            if (livingEntity.getId().equals(id)) {
                result = livingEntity;
                break;
            }
        }

        return result;
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
        Response response = new Response();
        response.setResponse(ResponseEnum.GameState);
        response.setGameName(gameNameEnum);
        response.setLivingEntities(new ArrayList<>());
        response.Send(game.getGameNameEnum(), true);
    }

    public void dealDamageToEnnemies(LivingEntity attacker, Zone zone, int damage) {
        List<LivingEntity> enemies = getEnemies(attacker);

        for (LivingEntity enemy : enemies) {
            if (zone.isInZone(attacker, enemy)) {
                float enemyHealth = enemy.getHealth();
                enemy.takeDamage(damage);
                Logger.game(enemy.getName() + "(health: " + enemyHealth +") " + enemy.getId() + " took " + damage + " damage from " + attacker.getName() + attacker.getId() + " new health: " + enemy.getHealth(), gameNameEnum);
            }
        }
    }

    public void dealDamageToEnnemies(LivingEntity attacker, Zone zone, int damage, long duration) {
        List<LivingEntity> enemies = getEnemies(attacker);

        for (LivingEntity enemy : enemies) {
            if (zone.isInZone(attacker, enemy)) {
                enemy.heal(damage);
            }
        }
    }

    public void healSelf(LivingEntity livingEntity) {
        livingEntity.heal(livingEntity.getWTotalShield());
    }


    public List<LivingEntity> getEnemies(LivingEntity livingEntity) {
         int team = livingEntity.getTeam();

        return this.livingEntities.values().stream()
                .filter(e -> e != livingEntity) // évite de se filtrer soi-même
                .filter(e -> switch (team) {
                    case 0 -> false;                      // neutre, pas d'ennemi
                    case 1 -> e.getTeam() == 2;
                    case 2 -> e.getTeam() == 1;
                    default -> false;
                })
                .toList();
    }

    // Getters and Setters
    public GameNameEnum getGameNameEnum() {
        return gameNameEnum;
    }
    public void setGameNameEnum(GameNameEnum gameNameEnum) {
        this.gameNameEnum = gameNameEnum;
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }
    public ConcurrentHashMap<String, Player> getPlayersMap() {
        return players;
    }

    public Collection<LivingEntity> getLivingEntities() {
        return livingEntities.values();
    }

    public ConcurrentHashMap<String, LivingEntity> getLivingEntitiesMap() {
        return livingEntities;
    }
}
