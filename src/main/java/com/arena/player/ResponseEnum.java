package com.arena.player;

import com.google.gson.annotations.SerializedName;

public enum ResponseEnum {
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



    // Join
    @SerializedName("Joined")
    Joined("Joined"),

    // CloseGame
    @SerializedName("Game Closed")
    GameClosed("Game Closed"),;

    private final String response;

    ResponseEnum(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
