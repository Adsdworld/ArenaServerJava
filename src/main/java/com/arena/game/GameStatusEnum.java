package com.arena.game;

public enum GameStatusEnum {
    /**
     * Creating Game
     * Initialise teams.
     */
    Creating("Creating Game"),

    /**
     * Game Created
     * Game is ready to start.
     */
    Created("Game Created"),

    /**
     * Game is starting.
     * Countdown to start the game.
     */
    Starting("Starting Game"),

    /**
     * Playing Game
     * The game is currently in progress.
     */
    Playing("Playing Game"),

    /**
     * Game Paused
     * The game is temporarily paused.
     */
    Paused("Game Paused"),

    /**
     * Game Ended
     * The game has finished.
     */
    Ended("Game Ended"),

    /**
     * Stopping Game
     * The game is in the process of stopping.
     */
    Stopping("Stopping Game"),

    /**
     * Game Stopped
     * The game has been stopped.
     */
    Stopped("Game Stopped");

    private final String gameStatus;

    GameStatusEnum(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameStatus() {
        return gameStatus;
    }
}
