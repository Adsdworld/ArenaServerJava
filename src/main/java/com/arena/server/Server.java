package com.arena.server;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.game.core.Core;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.building.Inhibitor;
import com.arena.game.entity.building.Nexus;
import com.arena.game.entity.building.Tower;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.utils.Logger;
import com.arena.utils.Position;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.arena.game.entity.EntityPositions.*;

public class Server {

    private static Server instance;

    private ArrayList<Player> players;

    public ArrayList<Game> games;

    private boolean creatingGame;
    private boolean closingGame;

    private final int maxGames = 5;

    private Server() {
        games = new ArrayList<>();
        creatingGame = false;
        players = new ArrayList<>();
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

    public boolean isCreatingGame() {
        return creatingGame;
    }

    public boolean isClosingGame() {
        return closingGame;
    }

    /**
     * Creates a new game based on the provided {@link Message}.
     *
     * @param message the {@link Message}  containing the {@link GameNameEnum}  to create the {@link Game}.
     * @implNote This method checks if a {@link Game}  is already being created. If so, it logs a warning and retries later. If not, it proceeds to create the {@link Game} , checking if it already exists or if the maximum number of games has been reached. It sends a {@link Response}  back to the client with the result of the operation.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public synchronized void createGame(Message message) {
        if (creatingGame) {
            Logger.warn("A game is already being created, please wait.");
            Core.getInstance().retryLater(message);

        } else {
            creatingGame = true;

            try {
                GameNameEnum gameNameEnum = message.getGameName();
                Game game = gameExists(gameNameEnum);

                Response response = new Response();

                if (game != null) {
                    Logger.warn(gameNameEnum.getGameName() + " already exists.");
                    response.setResponse(ResponseEnum.GameAlreadyExists);
                    response.setNotify(gameNameEnum.getGameName() + " already exists.");

                } else if (games.size() >= maxGames) {
                    Logger.warn("Cannot create more than " + maxGames + " games.");
                    response.setResponse(ResponseEnum.GamesLimitReached);
                    response.setNotify("Cannot create more than " + maxGames + " games.");

                } else {
                    Logger.game("Creating " + gameNameEnum.getGameName());
                    Game newGame = new Game(gameNameEnum);

                    CreateNexusInhibitorAndTowers(newGame);

                    games.add(newGame);

                    response.setResponse(ResponseEnum.GameCreated);
                    response.setNotify(gameNameEnum.getGameName() + " created successfully.");
                    response.setGameName(gameNameEnum);
                }

                response.Send(message.getUuid());

            } finally {
                creatingGame = false;
            }
        }
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
        return games.stream()
                .filter(game -> game.getPlayers().contains(player))
                .findFirst()
                .orElse(null);
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
        return players.stream()
                .filter(player -> player.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
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
        game.getPlayers().add(player);
    }

    private void CreateNexusInhibitorAndTowers(Game game) {
        /* Towers */
        for (Map.Entry<String, Position> map : BLUE_TOWERS.entrySet()) {
            String id = map.getKey();
            Position position = map.getValue();

            LivingEntity livingEntity = new Tower(id, 1);
            livingEntity.setPos(position);
            game.addEntity(livingEntity);
        }
        for (Map.Entry<String, Position> map : RED_TOWERS.entrySet()) {
            String id = map.getKey();
            Position position = map.getValue();

            LivingEntity livingEntity = new Tower(id, 2);
            livingEntity.setPos(position);
            game.addEntity(livingEntity);
        }

        /* Inhibitors */
        for (Map.Entry<String, Position> map : BLUE_INHIBITORS.entrySet()) {
            String id = map.getKey();
            Position position = map.getValue();

            LivingEntity livingEntity = new Inhibitor(id, 1);
            livingEntity.setPos(position);
            game.addEntity(livingEntity);
        }
        for (Map.Entry<String, Position> map : RED_INHIBITORS.entrySet()) {
            String id = map.getKey();
            Position position = map.getValue();

            LivingEntity livingEntity = new Inhibitor(id, 2);
            livingEntity.setPos(position);
            game.addEntity(livingEntity);
        }

        /* Nexus */
        for (Map.Entry<String, Position> map : BLUE_NEXUS.entrySet()) {
            String id = map.getKey();
            Position position = map.getValue();

            LivingEntity livingEntity = new Nexus(id, 1);
            livingEntity.setPos(position);
            game.addEntity(livingEntity);
        }
        for (Map.Entry<String, Position> map : RED_NEXUS.entrySet()) {
            String id = map.getKey();
            Position position = map.getValue();

            LivingEntity livingEntity = new Nexus(id, 2);
            livingEntity.setPos(position);
            game.addEntity(livingEntity);
        }
    }

    /**
     * Closes a game based on the provided {@link Message}.
     *
     * @param message the {@link Message} containing the {@link GameNameEnum} to close the {@link Game}.
     * @implNote This method checks if a {@link Game}  is already being closed. If so, it logs a warning and retries later. If not, it proceeds to close the {@link Game}, checking if it exists. It sends a {@link Response} back to the client with the result of the operation.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public synchronized void closeGame(Message message) {
        if (closingGame) {
            Logger.warn("A game is already being closed, please wait.");
            Core.getInstance().retryLater(message);
        } else {

            closingGame = true;
            try {
                Server.getInstance().getGames().removeIf(Objects::isNull);

                GameNameEnum gameNameEnum = message.getGameName();
                Game game = gameExists(gameNameEnum);

                Response response = new Response();

                if (game == null) {
                    Logger.warn(gameNameEnum.getGameName() + " does not exist.");
                    // TODO: create the unity handler for this case game not found
                    response.setResponse(ResponseEnum.GameNotFound);
                    response.setNotify(gameNameEnum.getGameName() + " does not exist.");
                    response.Send(message.getUuid());

                } else {
                    Logger.game("Closing " + gameNameEnum.getGameName());

                    /* Switch view to default entity for all players in the game */
                    Response response1 = new Response();
                    response1.setResponse(ResponseEnum.YourEntityIs);
                    response1.setText("default");
                    response1.setGameName(gameNameEnum);
                    response1.Send(gameNameEnum);

                    /* Send a clear game state to all players before closing for removing all entities */
                    game.clearUnityGame(game);

                    games.remove(game);
                    response.setResponse(ResponseEnum.GameClosed);
                    response.setGameName(gameNameEnum);
                    response.setNotify(gameNameEnum.getGameName() + " closed successfully.");
                    response.Send();
                }

            } finally {
                closingGame = false;
            }
        }
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
            players.add(player);
            Logger.server("Registering player : " + player.getUuid());
        }
    }

    /**
     * Unregisters a player from the server.
     *
     * @param player the player to unregister
     * @implNote This method checks if the {@code player} is already registered on the {@link Server} before attempting to remove him.
     * @author A.SALLIER
     * @date 2025-06-07
     */
    public void unregisterPlayer(Player player) {
        if (player != null && players.contains(player)) {
            players.remove(player);
            Logger.server("Unregistering player : " + player.getUuid());
        } else {
            Logger.failure("Could not unregister player : " + (player != null ? player.getUuid() : "null"));
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }


}
