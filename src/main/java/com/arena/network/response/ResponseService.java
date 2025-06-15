package com.arena.network.response;

import com.arena.game.GameNameEnum;

/**
 * This service class is responsible for sending responses to players in the game.
 */
public class ResponseService {

    private static IResponseSender responseSender;

    public static void setResponseSender(IResponseSender sender) {
        responseSender = sender;
    }

    public static IResponseSender getResponseSender() {
        return responseSender;
    }

    /**
     * Sends a response to all players in the game.
     *
     * @param response The {@link Response} to send.
     * @param silent If true, the response will not be logged by the {@link com.arena.utils.logger.Logger}.
     * @implNote This method checks if a response sender is set before attempting to send the response.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void send(Response response, boolean silent) {
        if (responseSender != null) {
            responseSender.sendResponse(response, silent);
        } else {
            System.err.println("⚠ No ResponseSender is set in MessageService.");
        }
    }

    /**
     * Sends a response to all players in the specified game.
     *
     * @param response The {@link Response} to send.
     * @param gameName The name of the game as a {@link GameNameEnum}.
     * @param silent If true, the response will not be logged by the {@link com.arena.utils.logger.Logger}.
     * @implNote This method checks if a response sender is set before attempting to send the response.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void sendToGame(Response response, GameNameEnum gameName, boolean silent) {
        if (responseSender != null) {
            responseSender.sendGameResponse(response, gameName, silent);
        } else {
            System.err.println("⚠ No ResponseSender is set in MessageService.");
        }
    }

    /**
     * Sends a response to a specific player identified by their UUID.
     *
     * @param uuid The UUID of the player to whom the message should be sent.
     * @param response The {@link Response} to send.
     * @param silent If true, the response will not be logged by the {@link com.arena.utils.logger.Logger}.
     * @implNote This method checks if a response sender is set before attempting to send the response.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void sendToUuid(String uuid, Response response, boolean silent) {
        if (responseSender != null) {
            responseSender.sendUuidResponse(uuid, response, silent);
        } else {
            System.err.println("⚠ No ResponseSender is set in MessageService.");
        }
    }
}
