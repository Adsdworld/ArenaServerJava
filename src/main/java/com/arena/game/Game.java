package com.arena.game;

import com.arena.player.Player;

import java.util.ArrayList;

public class Game {
    /* Identifier for the game */
    GameNameEnum gameNameEnum;

    /* Status of the game */
    GameStatusEnum gameStatusEnum;

    /* Teams in the game */
    private ArrayList<Player> BlueTeam;
    private ArrayList<Player> RedTeam;

    public Game(GameNameEnum gameNameEnum) {
        this.gameStatusEnum = GameStatusEnum.Creating;
        this.gameNameEnum = gameNameEnum;

        this.BlueTeam = new ArrayList<>();
        this.RedTeam = new ArrayList<>();

        this.gameStatusEnum = GameStatusEnum.Created;
    }

    public boolean addPlayerToBlueTeam(Player player) {
        if (BlueTeam.size() < 5) {
            BlueTeam.add(player);
            return true;
        }
        return false;
    }

    public boolean addPlayerToRedTeam(Player player) {
        if (RedTeam.size() < 5) {
            RedTeam.add(player);
            return true;
        }
        return false;
    }

    // Getters and Setters
    public GameNameEnum getGameNameEnum() {
        return gameNameEnum;
    }
    public void setGameNameEnum(GameNameEnum gameNameEnum) {
        this.gameNameEnum = gameNameEnum;
    }

    public ArrayList<Player> getBlueTeam() {
        return BlueTeam;
    }

    public ArrayList<Player> getRedTeam() {
        return RedTeam;
    }
}
