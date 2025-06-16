package com.arena.server;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.game.entity.EntityPositions;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.building.Inhibitor;
import com.arena.game.entity.building.Nexus;
import com.arena.game.entity.building.Tower;
import com.arena.game.utils.EntityInit;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.utils.logger.Logger;
import com.arena.game.utils.Position;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server is a singleton class that manages the game server, including game creation, player registration, and game state management.
 */
public class Server {

    private static Server instance;

    private final ConcurrentHashMap<String, Player> players;

    public ArrayList<Game> games;

    private static final int MAX_GAMES = 5;

    private Server() {
        games = new ArrayList<>();
        players = new ConcurrentHashMap<>();
    }

    /**
     * Singleton pattern to ensure only one instance of Server exists.
     * jc
     *
     * @return {@link Server} instance
     * @implNote This method initializes the {@link Server} instance if it is not already created.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public static synchronized Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    /**
     * Creates a new game based on the provided {@link Message}.
     *
     * @param message the {@link Message}  containing the {@link GameNameEnum}  to create the {@link Game}.
     * @return {@code true} if the game was created successfully; {@code false} otherwise.
     * @implNote This method checks if a {@link Game}  is already being created. If so, it logs a warning and retries later. If not, it proceeds to create the {@link Game} , checking if it already exists or if the maximum number of games has been reached. It sends a {@link Response}  back to the client with the result of the operation.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public boolean createGame(Message message) {
        GameNameEnum gameNameEnum = message.getGameName();
        Game game = gameExists(gameNameEnum);

        boolean result = false;

        Response response = new Response();

        if (game != null) {
            Logger.warn(gameNameEnum.getGameName() + " already exists.");
            response.setResponse(ResponseEnum.GameAlreadyExists);
            response.setNotify(gameNameEnum.getGameName() + " already exists.");
            response.send(message.getUuid());

        } else if (games.size() >= MAX_GAMES) {
            Logger.warn("Cannot create more than " + MAX_GAMES + " games.");
            response.setResponse(ResponseEnum.GamesLimitReached);
            response.setNotify("Cannot create more than " + MAX_GAMES + " games.");
            response.send(message.getUuid());

        } else {
            Logger.game("Creating " + gameNameEnum.getGameName());
            Game newGame = new Game(gameNameEnum);

            createNexusInhibitorAndTowers(newGame);

            games.add(newGame);

            response.setResponse(ResponseEnum.GameCreated);
            response.setNotify(gameNameEnum.getGameName() + " created successfully.");
            response.setGameName(gameNameEnum);
            response.send();
            result = true;
        }
        return result;
    }

    /**
     * Retrieves a Game instance based on the provided Player.
     *
     * @param player the {@link Player} whose game is to be retrieved.
     * @return the {@link Game} instance that contains the specified player, or {@code null} if no such game exists.
     * @implNote This method iterates through the list of games and returns the first game that contains the specified player in its list of players.
     * @author A.SALLIER
     * @date 2025-06-10
     */
    public Game getGameOfPlayer(Player player) {
        Game game = games.stream()
                .filter(g -> g.getPlayers().contains(player))
                .findFirst()
                .orElse(null);
        if (game == null) {
            Response response = new Response();
            response.setResponse(ResponseEnum.Info);
            response.setNotify("Game not found for player: " + player.getUuid() + ". Please check if you have sent a Create Game Action.");
            response.send(player.getUuid());
            Logger.warn("Game not found for player: " + player.getUuid() + ". Please check if you have sent a Create Game Action.");
        }
        return game;
    }

    /**
     * Retrieves the {@link Game}  of a given {@link LivingEntity} .
     *
     * @param entity the {@link LivingEntity} whose game is to be retrieved.
     * @return the {@link Game} instance that contains the specified entity, or {@code null} if no such game exists.
     * @implNote This method iterates through the list of games and returns the first game that contains the specified entity in its map of living entities.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public Game getGameOfEntity(LivingEntity entity) {
        Server server = Server.getInstance();

        Game game = server.getGames().stream()
                .filter(g -> g.getLivingEntitiesMap().containsKey(entity.getId()))
                .findFirst()
                .orElse(null);
        if (game == null) {
            Logger.warn("Game not found for entity: " + entity.getId() + ". Please check if the entity is present in a game.");
        }
        return game;
    }

    /**
     * Retrieves a player by their UUID.
     *
     * @param uuid the UUID of the player to retrieve
     * @return the {@link Player} with the specified UUID, or {@code null} if not found.
     * @implNote This method iterates through the list of players and returns the first one that matches the given UUID.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public Player getPlayerByUuid(String uuid) {
        Player player = getPlayersMap().values().stream()
                .filter(p -> p.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
        if (player == null) {
            Logger.warn("Player with UUID " + uuid + " not found. Please check if you have sent a Login Action.");
        }
        return player;
    }

    /**
     * Subscribe a player to a game.
     * {@code Game.players} receive {@code ResponseEnum.GameState} events, that allow them to receive updates.
     *
     * @param player the {@link Player} to subscribe.
     * @param game   the {@link Game} to which the player is subscribing.
     * @implNote This method iterates through all {@code Server.games}  in the {@link Server}  and removes the {@code player}  from any game they were previously subscribed to, then adds him to the specified game.
     * @author A.SALLIER
     * @date 2025-06-09
     */
    public void subscribePlayerToGame(Player player, Game game) {
        Server server = Server.getInstance();

        for (Game g : server.getGames()) {
            g.getPlayers().removeIf(p -> p.equals(player));
        }
        game.getPlayersMap().putIfAbsent(player.getUuid(), player);
    }

    private void createNexusInhibitorAndTowers(Game game) {
        createEntities(game, EntityPositions.BLUE_TOWERS, "Tower", 1);
        createEntities(game, EntityPositions.RED_TOWERS, "Tower", 2);
        createEntities(game, EntityPositions.BLUE_INHIBITORS, "Inhibitor", 1);
        createEntities(game, EntityPositions.RED_INHIBITORS, "Inhibitor", 2);
        createEntities(game, EntityPositions.BLUE_NEXUS, "Nexus", 1);
        createEntities(game, EntityPositions.RED_NEXUS, "Nexus", 2);
    }

    private void createEntities(Game game, Map<String, EntityInit> map, String type, int team) {
        for (Map.Entry<String, EntityInit> entry : map.entrySet()) {
            EntityInit entityInit = entry.getValue();
            String id = game.getGameNameEnum().getGameName() + "_" + entry.getKey();
            Position position = entityInit.getPosition();

            LivingEntity livingEntity = switch (type) {
                case "Tower" -> new Tower(id, team);
                case "Inhibitor" -> new Inhibitor(id, team);
                case "Nexus" -> new Nexus(id, team);
                default -> throw new IllegalArgumentException("Unknown entity type: " + type);
            };

            livingEntity.setAttackable(entityInit.isAttackable());
            livingEntity.setNextObjective(entityInit.getNextObjectiveId());
            livingEntity.setPos(position);
            game.addEntity(livingEntity);
        }
    }


    /**
     * Closes a game based on the provided {@link Message}.
     *
     * @param message the {@link Message} containing the {@link GameNameEnum} to close the {@link Game}.
     * @return true if success
     * @implNote This method checks if a {@link Game}  is already being closed. If so, it logs a warning and retries later. If not, it proceeds to close the {@link Game}, checking if it exists. It sends a {@link Response} back to the client with the result of the operation.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public boolean closeGame(Message message) {
        Server.getInstance().getGames().removeIf(Objects::isNull);

        GameNameEnum gameNameEnum = message.getGameName();
        Game game = gameExists(gameNameEnum);

        boolean result = false;

        Response response = new Response();

        if (game == null) {
            Logger.warn(gameNameEnum.getGameName() + " does not exist.");
            response.setResponse(ResponseEnum.GameNotFound);
            response.setNotify(gameNameEnum.getGameName() + " does not exist.");
            response.send(message.getUuid());

        } else {
            Logger.game("Closing " + gameNameEnum.getGameName());

            /* Switch view to default entity for all players in the game */
            Response response1 = new Response();
            response1.setResponse(ResponseEnum.YourEntityIs);
            response1.setText("default");
            response1.setGameName(gameNameEnum);
            response1.send(gameNameEnum);

            /* Send a clear game state to all players before closing for removing all entities */
            game.clearUnityGame(game);

            games.remove(game);
            response.setResponse(ResponseEnum.GameClosed);
            response.setGameName(gameNameEnum);
            response.setNotify(gameNameEnum.getGameName() + " closed successfully.");
            response.send();
            result = true;
        }
        return result;
    }


    // Getters and Setters
    public ArrayList<Game> getGames() {
        return games;
    }

    /**
     * Checks if the {@link Server} instance exists.
     *
     * @return the {@link Server} instance if it exists; {@code null} otherwise.
     * @implNote This method checks if the {@link Server} instance is initialized and returns {@code null} if not.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public Server serverExists() {
        Server server = Server.getInstance();

        if (server == null) {
            Logger.failure("Server instance is null.");
            return null;
        }
        return server;
    }

    /**
     * Checks if a game with the given {@link GameNameEnum} exists.
     *
     * @param gameNameEnum the game name to check
     * @return the {@link Game} instance if it exists; {@code null} otherwise.
     * @implNote @implNote This method checks if the {@link Server} instance is initialized and returns {@code null} if not.
     * @author A.Sallier
     * @date 2025-06-07
     */
    public Game gameExists(GameNameEnum gameNameEnum) {

        Server server = serverExists();

        if (server == null) {
            Logger.failure("Server instance is null.");
            return null;
        }

        return games.stream()
                .filter(game -> game.getGameNameEnum() == gameNameEnum)
                .findFirst()
                .orElseGet(() -> {
                    //Logger.warn("Game " + gameNameEnum.getGameName() + " does not exist.");
                    return null;
                });
    }

    /**
     * Registers a {@link Player} to the {@link Server}.
     *
     * @param player the {@link Player} to register
     * @implNote This method checks if the {@link  Player} is already registered on the {@link  Server}. If so, it logs a warning. If the {@code player}  is {@code null} , it logs a failure. Otherwise, it adds the {@code player}  to the list of registered {@code players}  and logs the registration.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public void registerPlayer(Player player) {

        if (players.contains(player)) {
            Logger.warn("Player " + player.getUuid() + " is already registered.");

        } else if (player == null) {
            Logger.failure("Could not register a null player.");

        } else {
            getPlayersMap().putIfAbsent(player.getUuid(), player);
            Logger.server("Registering player : " + player.getUuid());
        }
    }

    public ConcurrentHashMap<String, Player> getPlayersMap() {
        return players;
    }
}
