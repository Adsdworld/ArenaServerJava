package com.arena.player;

/**
 * Represents a unity client player.
 */
public class Player {
    /**
     * The unique identifier for the player.
     */
    private String uuid;

    /**
     * The name of the player.
     */
    private String name;

    // TODO: Add player name
    // TODO: Add pos x z y
    // TODO: Add rotation (0 to 360 degrees)
    // TODO: Add health

    public Player(String uuid) {
        this.uuid = uuid;
    }

    // Getters and setters
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
