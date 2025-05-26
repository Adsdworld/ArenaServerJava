package com.arena.player;

import com.google.gson.annotations.SerializedName;

public enum ActionEnum {
    @SerializedName("Login")
    Login("Login"),

    @SerializedName("Create Game")
    CreateGame("Create Game"),

    @SerializedName("Join")
    Join("Join");

    private final String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
