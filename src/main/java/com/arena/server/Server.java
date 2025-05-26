package com.arena.server;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.player.Player;
import com.arena.utils.Logger;

import java.util.ArrayList;

public class Server {

    private static Server instance;

    public ArrayList<Game> games;

    private boolean creatingGame;

    private final int MAX_GAMES = 5;

    private Server() {
        games = new ArrayList<>();
        creatingGame = false;
    }

    public static synchronized Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void createGame(GameNameEnum gameNameEnum) {
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
}
