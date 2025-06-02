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

    /// <summary>
    /// Do not change the UUID of a player.
    /// </summary>
    public void setUuid(String uuid) throws Exception {
        throw new Exception("Do not change the UUID of a player.");
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return uuid != null ? uuid.equals(player.uuid) : player.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

}
