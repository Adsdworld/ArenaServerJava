package com.arena.player;

import com.google.gson.annotations.SerializedName;

public enum ResponseEnum {

    /**
     * Info : success
     */
    @SerializedName("Info")
    Info("Info"),

    /**
     * Login : success
     * When a player connect to the server class, not when he join a game
     * @author: @A.Sallier
     */
    @SerializedName("Logged")
    Logged("Logged"),

    /*
     * CreateGame : success
     */
    @SerializedName("Game Created")
    GameCreated("Game Created"),
    /**
     * CreateGame : failure
     */
    @SerializedName("Game Already Exists")
    GameAlreadyExists("Game Already Exists"),
    /**
     * CreateGame : failure
     * @note: Use gameNameEnum Game 6 to test this
     * @author: @A.Sallier
     */
    @SerializedName("Games Limit Reached")
    GamesLimitReached("Games Limit Reached"),


    /**
     * JoinGame : success
     * @note: Notify players
     */
    @SerializedName("Joined")
    Joined("Joined"),
    /**
     * JoinGame : failure
     */
    @SerializedName("Player Already In Game")
    PlayerAlreadyInGame("Player Already In Game"),

    /**
     * CloseGame : success
     */
    @SerializedName("Game Closed")
    GameClosed("Game Closed"),
    /**
     * CloseGame : failure
     */
    @SerializedName("Game Not Found")
    GameNotFound("Game Not Found"),
    

    /**
     * Spawn : success
     * @note: Notify players that and entity (not a player) has been spawned.
     */
    @SerializedName("Spawned")
    Spawned("Spawned"),


    @SerializedName("Game State")
    GameState("Game State"),

    /**
     * Join : success
     * @note: Send gameNameEnum and entityId.
     */
    @SerializedName("Your Entity Is")
    YourEntityIs("Your Entity Is"),



    ;

    private final String response;

    ResponseEnum(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
