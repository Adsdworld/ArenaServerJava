package com.arena.player;

import com.google.gson.annotations.SerializedName;

/**
 * Enum representing various responses that can be sent by the server to the client.
 */
public enum ResponseEnum {

    /**
     * Info : success
     */
    @SerializedName("Info")
    Info("Info"),


    /**
     * Login : success
     * Response to login.
     * This is sent when the {@link Player} have been added to the {@link com.arena.server.Server} .
     */
    @SerializedName("Logged")
    Logged("Logged"),


    /*
     * CreateGame : success
     * Response to create a game.
     */
    @SerializedName("Game Created")
    GameCreated("Game Created"),
    /**
     * CreateGame : failure
     * Response game already exists.
     */
    @SerializedName("Game Already Exists")
    GameAlreadyExists("Game Already Exists"),
    /**
     * CreateGame : failure
     * Response games limit reached.
     * Use gameNameEnum Game 6 to test this.
     */
    @SerializedName("Games Limit Reached")
    GamesLimitReached("Games Limit Reached"),


    /**
     * JoinGame : success
     * Response to join a game.
     */
    @SerializedName("Joined")
    Joined("Joined"),
    /**
     * JoinGame : failure
     * Response player already in game.
     */
    @SerializedName("Player Already In Game")
    PlayerAlreadyInGame("Player Already In Game"),


    /**
     * CloseGame : success
     * Response to close a game.
     */
    @SerializedName("Game Closed")
    GameClosed("Game Closed"),
    /**
     * CloseGame : failure
     * Response game not found.
     */
    @SerializedName("Game Not Found")
    GameNotFound("Game Not Found"),

    /**
     * Game State : None
     * Response to send a game state.
     */
    @SerializedName("Game State")
    GameState("Game State"),

    /**
     * Your Entity Is : None
     * Response to set the entity controlled by the {@link Player}.
     */
    @SerializedName("Your Entity Is")
    YourEntityIs("Your Entity Is");

    private final String response;

    ResponseEnum(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
