package com.arena.network;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.network.response.IResponseSender;
import com.arena.network.response.Response;
import com.arena.player.Player;
import com.arena.server.Server;
import com.arena.utils.Logger;
import org.java_websocket.WebSocket;
import com.google.gson.Gson;


public class JavaWebSocketResponseSender implements IResponseSender {

    @Override
    public void sendResponse(Response response, boolean silent) {
        for (Player player : Server.getInstance().getPlayers()) {
            if (player.getUuid() == null) {
                Logger.failure("Player UUID is null, cannot send response: " + response);
                continue;
            }
            response.setUuid(player.getUuid());
            sendToConn(getConnByUuid(player.getUuid()), response);
        }
        if (!silent) {
            Logger.game("Send To All : " + response);
        }
    }

    @Override
    public void sendGameResponse(Response response, GameNameEnum gameName, boolean silent) {
        Server server = Server.getInstance();
        for (Game game : server.getGames()) {
            if (game.getGameNameEnum().equals(gameName)) {
                for (Player player : game.getPlayers()) {
                    if (server.getPlayers().contains(player)) {
                        response.setUuid(player.getUuid());
                        sendToConn(getConnByUuid(player.getUuid()), response);
                    }
                }
                if (!silent) {
                    Logger.game("Send " + gameName + " : " + response, gameName);
                }
            }
        }
    }

    @Override
    public void sendUuidResponse(String uuid, Response response, boolean silent) {
        Player player = new Player(uuid);

        if (Server.getInstance().getPlayers().contains(player)) {
            response.setUuid(uuid);
            sendToConn(getConnByUuid(uuid), response);
        }

        if (!silent) {
            Logger.server("Send To " + uuid + " : " + response);
        }
    }

    private WebSocket getConnByUuid(String uuid) {
        Player player = new Player(uuid);
        WebSocket conn = JavaWebSocket.getInstance().uuidToWebSocket.get(player);
        if (conn != null) {
            return conn;
        }
        Logger.failure("Player " + uuid + " not found in connections.");
        return null;
    }

    private void sendToConn(WebSocket conn, Response response) {
        try {
            Gson gson = new Gson();
            conn.send(gson.toJson(response));
        } catch (Exception e) {
            Logger.error("Error sending response to connection: " + conn.getRemoteSocketAddress());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
