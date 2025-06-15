package com.arena.network.response;

import com.arena.game.GameNameEnum;

/**
 * This interface defines methods for sending response messages to players in a game.
 */
public interface IResponseSender {

    /**
     * Sends a response message to all players in the specified game.
     *
     * @param response The {@link Response} to send.
     * @param silent If true, the response will not be logged by the {@link com.arena.utils.logger.Logger} .
     * @author A.SALLIER
     * @date 2025-06-15
     */
    void sendResponse(Response response, boolean silent);

    /**
     * Sends a game-specific response to all players in the specified game.
     *
     * @param response The {@link Response} to send.
     * @param gameName The name of the game to which the response should be sent.
     * @param silent If true, the response will not be logged by the {@link com.arena.utils.logger.Logger}.
     * @implNote
     * @author A.SALLIER
     * @date 2025-06-15
     */
    void sendGameResponse(Response response, GameNameEnum gameName, boolean silent);

    /**
     * Sends a response message to a specific player identified by their UUID.
     *
     * @param uuid The UUID of the player to whom the message should be sent.
     * @param response The {@link Response} to send.
     * @param silent If true, the response will not be logged by the {@link com.arena.utils.logger.Logger}.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    void sendUuidResponse(String uuid, Response response, boolean silent);
}
