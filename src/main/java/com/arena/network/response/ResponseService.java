package com.arena.network.response;

import com.arena.game.GameNameEnum;

public class ResponseService {

    private static IResponseSender responseSender;

    public static void setResponseSender(IResponseSender sender) {
        responseSender = sender;
    }

    public static IResponseSender getResponseSender() {
        return responseSender;
    }

    public static void send(Response response, boolean silent) {
        if (responseSender != null) {
            responseSender.sendResponse(response, silent);
        } else {
            System.err.println("⚠ No ResponseSender is set in MessageService.");
        }
    }

    public static void sendToGame(Response response, GameNameEnum gameName, boolean silent) {
        if (responseSender != null) {
            responseSender.sendGameResponse(response, gameName, silent);
        } else {
            System.err.println("⚠ No ResponseSender is set in MessageService.");
        }
    }

    public static void sendToUuid(String uuid, Response response, boolean silent) {
        if (responseSender != null) {
            responseSender.sendUuidResponse(uuid, response, silent);
        } else {
            System.err.println("⚠ No ResponseSender is set in MessageService.");
        }
    }
}
