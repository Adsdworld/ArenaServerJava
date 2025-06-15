package com.arena.player;

/**
 * Player class represents a player in the game with a unique identifier (UUID).
 *
 * @param uuid the unique identifier of the player as a {@link String}.
 */
public record Player(String uuid) {
    /**
     * Get the UUID of the player.
     *
     * @return the UUID of the player as a {@link String} .
     * @implNote This method returns the UUID of the player, which is a unique identifier for each player in the game.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public String getUuid() {
        return uuid;
    }
}