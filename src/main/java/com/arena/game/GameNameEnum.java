package com.arena.game;

import com.google.gson.annotations.SerializedName;

public enum GameNameEnum {
    @SerializedName("Game 1")
    Game1("Game 1"),

    @SerializedName("Game 2")
    Game2("Game 2"),

    @SerializedName("Game 3")
    Game3("Game 3"),

    @SerializedName("Game 4")
    Game4("Game 4"),

    @SerializedName("Game 5")
    Game5("Game 5");

    private final String gameName;

    GameNameEnum(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
