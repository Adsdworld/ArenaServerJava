package com.arena.server;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.game.core.Core;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.utils.Logger;

import java.util.ArrayList;
import java.util.Objects;

public class Server {

    private static Server instance;

    private ArrayList<Player> players;

    public ArrayList<Game> games;

    private boolean creatingGame;
    private boolean closingGame;

    private final int MAX_GAMES = 5;

    private Server() {
        games = new ArrayList<>();
        creatingGame = false;
        players = new ArrayList<>();
    }

    public static synchronized Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public boolean isCreatingGame () {
        return creatingGame;
    }

    public boolean isClosingGame () {
        return closingGame;
    }

    public synchronized void createGame(Message message) {
        if (creatingGame) {
            Logger.warn("A game is already being created, please wait.");
            Core.getInstance().retryLater(message);
            return;
        }

        creatingGame = true;
        try {
            Server.getInstance().getGames().removeIf(Objects::isNull);

            GameNameEnum gameNameEnum = message.getGameName();
            Game game = gameExists(gameNameEnum);

            Response response = new Response();

            if (game != null) {
                Logger.warn(gameNameEnum.getGameName() + " already exists.");
                response.setResponse(ResponseEnum.GameAlreadyExists);
                response.setNotify(gameNameEnum.getGameName() + " already exists.");
                response.Send(message.getUuid());
                return;
            }

            if (games.size() >= MAX_GAMES) {
                Logger.warn("Cannot create more than " + MAX_GAMES + " games.");
                response.setResponse(ResponseEnum.GamesLimitReached);
                response.setNotify("Cannot create more than " + MAX_GAMES + " games.");
                response.Send(message.getUuid());
                return;
            }

            Logger.game("Creating " + gameNameEnum.getGameName());
            games.add(new Game(gameNameEnum));
            response.setResponse(ResponseEnum.GameCreated);
            response.setNotify(gameNameEnum.getGameName() + " created successfully.");
            response.setGameName(gameNameEnum);
            response.Send();
        } finally {
            creatingGame = false;
        }
    }

    public synchronized void closeGame(Message message) {
        if (closingGame) {
            Logger.warn("A game is already being closed, please wait.");
            Core.getInstance().retryLater(message);
            return;
        }

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
                return;
            }

            Logger.game("Closing " + gameNameEnum.getGameName());
            games.remove(game);
            response.setResponse(ResponseEnum.GameClosed);
            response.setGameName(gameNameEnum);
            response.setNotify(gameNameEnum.getGameName() + " closed successfully.");
            response.Send();
        } finally {
            closingGame = false;
        }
    }


    // Getters and Setters
    public ArrayList<Game> getGames() {
        return games;
    }

    public Game gameExists(GameNameEnum gameNameEnum) {
        return games.stream()
                .filter(game -> game.getGameNameEnum() == gameNameEnum)
                .findFirst()
                .orElse(null);
    }

    public void registerPlayer(Player player) {

        if (players.contains(player)) {
            Logger.warn("Player " + player.getUuid() + " is already registered.");
            return;
        }

        if (player == null) {
            Logger.failure("Could not register a null player.");
            return;
        }
        players.add(player);
        Logger.server("Registering player : " + player.getUuid());
    }

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
