package com.arena.player;

import com.google.gson.annotations.SerializedName;

public enum ResponseEnum {

    /**
     * Info : success
     */
    @SerializedName("Info")
    Info("Info"),

    // Login
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
     */
    @SerializedName("Games Limit Reached")
    GamesLimitReached("Games Limit Reached"),


    /**
     * JoinGame : success
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
    
    
    @SerializedName("Spawned")
    Spawned("Spawned"),



    ;

    private final String response;

    ResponseEnum(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
