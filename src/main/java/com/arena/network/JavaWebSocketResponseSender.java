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
    public void sendResponse(Response response) {
        for (Player player : Server.getInstance().getPlayers()) {
            if (player.getUuid() == null) {
                Logger.failure("Player UUID is null, cannot send response: " + response);
                continue;
            }
            response.setUuid(player.getUuid());
            sendToConn(getConnByUuid(player.getUuid()), response);
        }
        Logger.game("Send To All : " + response);
    }

    @Override
    public void sendGameResponse(Response response, GameNameEnum gameName) {
        for (Game game : Server.getInstance().getGames()) {
            if (game.getGameNameEnum().equals(gameName)) {
                for (Player player : game.getBlueTeam()){
                    response.setUuid(player.getUuid());
                    sendToConn(getConnByUuid(player.getUuid()), response);
                }
                for (Player player : game.getRedTeam()) {
                    response.setUuid(player.getUuid());
                    sendToConn(getConnByUuid(player.getUuid()), response);
                }
                for (Player player : game.getSpectators()) {
                    response.setUuid(player.getUuid());
                    sendToConn(getConnByUuid(player.getUuid()), response);
                }
                Logger.game("Send "+ gameName +" : " + response, gameName);
            }
        }
    }

    @Override
    public void sendUuidResponse(String uuid, Response response) {
        response.setUuid(uuid);
        sendToConn(getConnByUuid(uuid), response);
        Logger.game("Send To " + uuid + " : " + response);
    }

    private WebSocket getConnByUuid(String uuid) {
        for (WebSocket conn : JavaWebSocket.getInstance().getConnections()) {
            if (JavaWebSocket.getInstance().webSocketToUuid.get(conn).getUuid().equals(uuid)) {
                return conn;
            }
        }
        return null;
    }

    private void sendToConn (WebSocket conn, Response response) {
        if (conn != null && conn.isOpen()) {
            Gson gson = new Gson();
            conn.send(gson.toJson(response));
        } else {
            Logger.failure("Connection is null or closed for conn: " + conn);
        }
    }
}
