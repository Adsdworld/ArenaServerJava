package com.arena.server;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.player.Player;
import com.arena.utils.Logger;

import java.util.ArrayList;

public class Server {

    private static Server instance;

    private ArrayList<Player> players;

    public ArrayList<Game> games;

    private boolean creatingGame;

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

    public synchronized void createGame(GameNameEnum gameNameEnum) {
        if (!creatingGame) {
            if (games.size() < MAX_GAMES) {
                creatingGame = true;

                Game game = gameExists(gameNameEnum);

                if (game != null) {
                    Logger.info("Game already exists: " + gameNameEnum.getGameName());
                } else {
                    game = new Game(gameNameEnum);
                    games.add(game);
                    Logger.info("Created game: " + gameNameEnum.getGameName());
                }
            } else {
                Logger.failure("Cannot create more games, maximum reached: " + MAX_GAMES);
            }
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
        if (player == null) {
            Logger.error("Could not register a null player.");
            return;
        }
        players.add(player);
        Logger.info("Registering player: " + player.getUuid());
    }

    public void unregisterPlayer(Player player) {
        if (player != null && players.contains(player)) {
            players.remove(player);
            Logger.info("Unregistering player: " + player.getUuid());
        } else {
            Logger.error("Could not unregister player: " + (player != null ? player.getUuid() : "null"));
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
