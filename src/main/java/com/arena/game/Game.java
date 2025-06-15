package com.arena.game;

import com.arena.game.entity.LivingEntity;
import com.arena.game.zone.Zone;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    /* Identifier for the game */
    GameNameEnum gameNameEnum;

    private final ConcurrentHashMap<String, LivingEntity> livingEntities;

    private final ConcurrentHashMap<String, Player> players;

    /**
     * Constructor for the Game class.
     *
     * @param gameNameEnum the {@link GameNameEnum} representing the name of the game.
     * @implNote This constructor initializes the game with a specific name and creates empty maps for living entities and players.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public Game(GameNameEnum gameNameEnum) {
        this.setGameNameEnum(gameNameEnum);
        this.livingEntities = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
    }

    /**
     * Get the {@link LivingEntity} associated with a player.
     *
     * @param player the {@link Player} for whom to retrieve the living entity.
     * @return the {@link LivingEntity} associated with the player, or null if not found.
     * @implNote This method searches through the map of living entities to find one that matches the player's UUID. If no entity is found, it sends a notification to the player and logs a warning.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public LivingEntity getLivingEntity(Player player) {
        LivingEntity livingEntity = getLivingEntitiesMap().values().stream()
                .filter(entity -> entity.getId().equals(player.getUuid()))
                .findFirst()
                .orElse(null);
        if (livingEntity == null) {
            Response response = new Response();
            response.setResponse(ResponseEnum.Info);
            response.setNotify("Living entity not found for player: " + player.getUuid() + ". Please check if you have sent a Join Action.");
            response.send(player.getUuid());
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
        response.send(entityId);
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

    /**
     * Removes a {@link LivingEntity} from the game.
     *
     * @param livingEntity the {@link LivingEntity} to remove.
     * @implNote This method checks if the {@link LivingEntity} is not null before attempting to remove it. If it is null, it logs a failure message. If it exists, it removes the entity from the game and logs the action.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void removeEntity(LivingEntity livingEntity) {
        if (livingEntity != null) {
            getLivingEntitiesMap().remove(livingEntity.getId());
            Logger.game(livingEntity.getName() + " " + livingEntity.getId() + " removed from game " + gameNameEnum.getGameName(), gameNameEnum);
        } else {
            Logger.failure("Cannot remove null entity from game " + gameNameEnum.getGameName());
        }
    }

    /**
     * Retrieves a list of players associated with a specific team in the game.
     *
     * @param team the team number (1 or 2) for which to retrieve players.
     * @return a list of {@link Player} objects belonging to the specified team.
     * @implNote This method filters the players based on their association with living entities of the specified team.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public List<Player> getPlayersOfTeam(int team) {
        return getPlayersMap().values().stream()
                .filter((player -> getLivingEntitiesOfTeam(team).stream()
                        .anyMatch(livingEntity -> livingEntity.getId().equals(player.getUuid()))))
                .toList();
    }

    /**
     * Retrieves a list of living entities associated with a specific team in the game.
     *
     * @param team the team number (1 or 2) for which to retrieve living entities.
     * @return a list of {@link LivingEntity} objects belonging to the specified team.
     * @implNote This method filters the living entities based on their team number.
     * @author A.SALLIER
     * @date 2025-06-15
     */
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
        response.send(player.getUuid(), true);
    }

    /**
     * Clears the Unity game state for all players in the game.
     * This method sends a response to all players to clear all entities in the Unity client game by sending an empty list of living entities.
     *
     * @param game the {@link Game} for which the game state should be cleared.
     * @implNote This method creates a new {@link Response} object, sets the response type to {@link ResponseEnum#GameState}, and sends an empty list of living entities to all players in the game.
     * @author A.SALLIER
     * @date 2025-06-09
     */
    public void clearUnityGame(Game game) {
        Response response = new Response();
        response.setResponse(ResponseEnum.GameState);
        response.setGameName(gameNameEnum);
        response.setLivingEntities(new ArrayList<>());
        response.send(game.getGameNameEnum(), true);
    }

    /**
     * Deals damage to all enemies of the attacker in the specified zone.
     *
     * @param attacker the {@link LivingEntity} that is dealing damage.
     * @param zone the {@link Zone} in which the damage is being dealt.
     * @param damage the amount of damage to deal to each enemy.
     * @implNote This method iterates through all enemies of the attacker, checks if they are within the specified zone, and applies damage to them. It also logs the damage dealt to each enemy.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void dealDamageToEnnemies(LivingEntity attacker, Zone zone, int damage) {
        List<LivingEntity> enemies = getEnemies(attacker);

        for (LivingEntity enemy : enemies) {
            if (zone.isInZone(attacker, enemy)) {
                float enemyHealth = enemy.getHealth();
                enemy.takeDamage(damage);
                if (enemyHealth != 0) {
                    Logger.game(enemy.getName() + "(health: " + enemyHealth +") " + enemy.getId() + " took " + damage + " damage from " + attacker.getName() + attacker.getId() + " new health: " + enemy.getHealth(), gameNameEnum);
                }
            }
        }
    }

    /*public void dealDamageToEnnemies(LivingEntity attacker, Zone zone, int damage, long duration) {
        List<LivingEntity> enemies = getEnemies(attacker);

        for (LivingEntity enemy : enemies) {
            if (zone.isInZone(attacker, enemy)) {
                enemy.heal(damage);
            }
        }
    }*/

    /**
     * Retrieves a {@link LivingEntity} by its general ID.
     *
     * @param generalId the {@link String} general ID of the LivingEntity to retrieve.
     * @return the {@link LivingEntity} with the specified general ID, or null if not found.
     * @implNote This method searches through the map of living entities to find one that matches the provided general ID. If no entity is found, it logs a warning message.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public LivingEntity getLivingEntityByGeneralId(String generalId) {
        LivingEntity livingEntity = getLivingEntitiesMap().values().stream()
                .filter(entity -> entity.getGeneralId().equals(generalId))
                .findFirst()
                .orElse(null);
        if (livingEntity == null) {
            Logger.warn("Living entity not found for generalId: " + generalId + ". Please check if you have added the entity and be sure to provide a generalId and not an id.");
        }
        return livingEntity;
    }

    /**
     * Retrieves a collection of {@link LivingEntity} objects by their general IDs.
     *
     * @param generalId a collection of {@link String} general IDs of the LivingEntities to retrieve.
     * @return a collection of {@link LivingEntity} objects that match the provided general IDs. If no entities are found, a warning is logged.
     * @implNote This method filters the map of living entities to find those that match any of the provided general IDs. If no entities are found, it logs a warning message.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public Collection<LivingEntity> getLivingEntityByGeneralId(Collection<String> generalId) {
        Collection<LivingEntity> livingEntity = getLivingEntitiesMap().values().stream()
                .filter(entity -> generalId.contains(entity.getGeneralId()))
                .toList();
        if (livingEntity.isEmpty()) {
            Logger.warn("Living entity not found for generalId: " + generalId + ". Please check if you have added the entity and be sure to provide a generalId and not an id.");
        }
        return livingEntity;
    }

    /**
     * Heals the living entity by retrieving W TotalShield and applying it to the entity's health.
     *
     * @param livingEntity the {@link LivingEntity} to heal.
     * @implNote This method retrieves the total shield of the living entity and applies it to its health, effectively healing it.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public void healSelf(LivingEntity livingEntity) {
        livingEntity.heal(livingEntity.getWTotalShield());
    }

    /**
     * Retrieves a list of enemies for a given living entity based on its team.
     *
     * @param livingEntity the {@link LivingEntity} for which to retrieve enemies.
     * @return a list of {@link LivingEntity} objects that are considered enemies of the specified living entity.
     * @implNote This method filters the living entities based on their team affiliation. It checks if the living entity is attackable and not the same as the one provided, then determines enemies based on the team number:
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public List<LivingEntity> getEnemies(LivingEntity livingEntity) {
         int team = livingEntity.getTeam();

        return this.livingEntities.values().stream()
                .filter(LivingEntity::isAttackable)
                .filter(e -> e != livingEntity) // évite de se filtrer soi-même
                .filter(e -> switch (team) {
                    case 0 -> false;                      // neutre, pas d'ennemi
                    case 1 -> e.getTeam() == 2;
                    case 2 -> e.getTeam() == 1;
                    default -> false;
                })
                .toList();
    }

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
