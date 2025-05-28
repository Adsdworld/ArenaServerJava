package com.arena.network.response;

import com.arena.game.GameNameEnum;

public interface IResponseSender {

    /**
     * Sends a response message to all players in the specified game.
     *
     * @param message   The message to send.
     * @param gameName  The name of the game to which the message should be sent.
     */
    void sendResponse(Response response);

    /**
     * Sends a game-specific response response to all players in the specified game.
     *
     * @param response   The response to send.
     * @param gameName  The name of the game to which the response should be sent.
     */
    void sendGameResponse(Response response, GameNameEnum gameName);

    /**
     * Sends a response message to a specific player identified by their UUID.
     *
     * @param uuid      The UUID of the player to whom the message should be sent.
     * @param response  The response message to send.
     */
    void sendUuidResponse(String uuid, Response response);
}
