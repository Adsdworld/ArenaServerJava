package com.arena.game.handler;

import com.arena.network.message.Message;

/**
 * IMessageHandler interface for handling messages from {@link com.arena.player.Player}
 */
public interface IMessageHandler {
    /**
     * Handles the incoming {@link Message}  from a player.
     *
     * @param message the {@link Message}  to handle.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    void handle(Message message);
}
