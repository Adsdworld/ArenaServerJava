package com.arena.network.message;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.network.JavaWebSocket;
import com.arena.player.Player;
import com.arena.server.Server;
import com.arena.utils.Logger;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;

public class BroadcastMessage implements IMessageSender {

    private final Gson gson = new Gson();

    @Override
    public void sendMessage(Message message, GameNameEnum gameName) {
        for (Game game : Server.getInstance().getGames()) {
            if (game.getGameNameEnum().equals(gameName)) {
                for (Player player : game.getBlueTeam()) {
                    sendToConn(getConnByUuid(player.getUuid()), message);
                }
            }
        }
            String json = gson.toJson(message);
            for (WebSocket conn : JavaWebSocket.getInstance().getConnections()) {
                conn.send(json);
            }
    }

    @Override
    public void sendMessageToUuid(String uuid, Message message) {
        sendToConn(getConnByUuid(uuid), message);
    }

    private WebSocket getConnByUuid(String uuid) {
        for (WebSocket conn : JavaWebSocket.getInstance().getConnections()) {
            if (JavaWebSocket.getInstance().webSocketToUuid.get(conn).getUuid().equals(uuid)) {
                return conn;
            }
        }
        return null;
    }

    private void sendToConn (WebSocket conn, Message message) {
        if (conn != null && conn.isOpen()) {
            conn.send(gson.toJson(message));

            Logger.info("Message sent to conn: " + conn.getRemoteSocketAddress() + " - Message: " + message);
            Logger.info(gson.toJson(message));
        } else {
            Logger.failure("Connection is null or closed for conn: " + conn);
        }
    }
}
