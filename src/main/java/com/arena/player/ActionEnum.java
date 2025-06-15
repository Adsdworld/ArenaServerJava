package com.arena.player;

import com.google.gson.annotations.SerializedName;

/**
 * Enum representing various actions that can be performed by a {@link Player}  in the game.
 */
public enum ActionEnum {
    /**
     * Perform on WebSocket OnOpen.
     */
    @SerializedName("Login")
    Login("Login"),

    /**
     * Action to create a game.
     */
    @SerializedName("Create Game")
    CreateGame("Create Game"),

    /**
     * Action to join a game.
     */
    @SerializedName("Join")
    Join("Join"),

    /**
     * Action to close a game.
     */
    @SerializedName("Close Game")
    CloseGame("Close Game"),

    /**
     * Cast Q
     */
    @SerializedName("Cast Q")
    CastQ("Cast Q"),

    /**
     * Cast W
     */
    @SerializedName("Cast W")
    CastW("Cast W"),

    /**
     * Cast E
     */
    @SerializedName("Cast E")
    CastE("Cast E"),

    /**
     * Cast R
     */
    @SerializedName("Cast R")
    CastR("Cast R"),

    /**
     * To send your player state to the server.
     */
    @SerializedName("Player State Update")
    PlayerStateUpdate("Player State Update");

    private final String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
