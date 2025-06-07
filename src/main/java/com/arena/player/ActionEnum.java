package com.arena.player;

import com.google.gson.annotations.SerializedName;

public enum ActionEnum {
    @SerializedName("Login")
    Login("Login"),

    @SerializedName("Create Game")
    CreateGame("Create Game"),

    @SerializedName("Join")
    Join("Join"),

    @SerializedName("Close Game")
    CloseGame("Close Game"),

    @SerializedName("What Is My Entity")
    WhatIsMyEntity("What Is My Entity"),

    /*
     * Cooldown Start :
     * @note: Response through auto GameState
     * @author: @A.Sallier
     */
    @SerializedName("Cooldown Start")
    CooldownStart("Cooldown Start"),
    


    ;

    private final String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
