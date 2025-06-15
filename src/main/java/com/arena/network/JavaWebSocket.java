package com.arena.network;

import com.arena.game.Game;
import com.arena.game.core.Core;
import com.arena.network.response.Response;
import com.arena.network.response.ResponseService;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;
import com.arena.network.message.Message;
import com.arena.utils.json.JsonService;
import com.google.gson.JsonParseException;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JavaWebSocket is a singleton WebSocket server that listens for incoming connections
 * and processes messages in JSON format.
 * It uses the JsonService for JSON serialization/deserialization.
 */
public class JavaWebSocket extends WebSocketServer {

    public int port;
    private static JavaWebSocket instance;
    private final JsonService jsonService = new JsonService();

    public ConcurrentHashMap<WebSocket, Player> webSocketToUuid;
    public ConcurrentHashMap<Player, WebSocket> uuidToWebSocket;

    private JavaWebSocket(int port) {
        super(new InetSocketAddress(port));
        this.port = port;

        ResponseService.setResponseSender(new JavaWebSocketResponseSender());

        Logger.info("JavaWebSocket created on port " + port);
        webSocketToUuid = new ConcurrentHashMap<>();
        uuidToWebSocket = new ConcurrentHashMap<>();
    }

    /**
     * getInstance is a synchronized method that returns the singleton instance of JavaWebSocket.
     *
     * @return JavaWebSocket instance.
     * @implNote if the instance is not initialized, it throws an IllegalStateException.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static synchronized JavaWebSocket getInstance() {
        if (instance == null) {
            throw new IllegalStateException("JavaWebSocket not initialized. Call initialize(port) first.");
        }
        return instance;
    }

    /**
     * initialize is a synchronized method that creates a new instance of JavaWebSocket
     *
     * @param port the port on which the WebSocket server will listen.
     * @implNote checks if the instance is already initialized to avoid creating multiple instances.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static synchronized void initialize(int port) {
        if (instance == null) {
            instance = new JavaWebSocket(port);
        }
    }

    @Override
    public void onStart() {
        Logger.info("JavaWebSocket started and listening on port " + port);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Logger.info("New connection from: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

        try {
            /* Reason for closing the connection:
             * - code: the WebSocket close code.
             * - reason: a string explaining why the connection was closed.
             * - remote: true if the close was initiated by the remote peer, false if it was initiated by the server.
             */
            String reason1 = reason != null && !reason.isEmpty() ? reason : " No reason provided";
            Logger.info("Connection closed: " + conn.getRemoteSocketAddress() + reason1 + " (code: " + code + ", remote: " + remote + ")");
            Player player = webSocketToUuid.get(conn);
            if (player != null) {
                Server server = Server.getInstance();

                /* Unregister player from the server, it avoids sending response to null connections */
                uuidToWebSocket.remove(player);
                Server.getInstance().getPlayersMap().remove(player.getUuid());

                /* Remove player from all games, player entity still exists in the game if he rejoins later,
                * It avoids sending multiples 'Game State' to one Player */
                for (Game game : server.getGames()) {
                    game.getPlayersMap().remove(player.getUuid());
                }
            }
            webSocketToUuid.remove(conn);

        } catch (JsonParseException e) {
            Logger.error("Exception while closing connection: " + e.getMessage());
        } finally {
            Logger.server("Connection closed successfully: " + conn.getRemoteSocketAddress() + " (code: " + code + ", remote: " + remote + ")");
        }
    }

    /**
     * onMessage is called when a message is received from a {@link WebSocket} connection.
     *
     * @param conn the {@link WebSocket} connection that sent the message.
     * @param messageJson the message received in JSON format.
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
     */
    @Override
    public void onMessage(WebSocket conn, String messageJson) {
        try {
            jsonService.fromJson(messageJson, Message.class);

            Message message = jsonService.fromJson(messageJson, Message.class);

            if (message.getAction() != ActionEnum.PlayerStateUpdate) {
                Logger.info(message.getUuid() + " >>> " + message.getAction() + " : " + messageJson);
            }

            /* Check if the message is a login action
             * This logic could not be pass to the Core because we need the conn/Websocket to register the Player
             */
            if (message.getAction() == ActionEnum.Login) {
                Player player = new Player(message.getUuid());
                JavaWebSocket.getInstance().webSocketToUuid.putIfAbsent(conn, player);
                JavaWebSocket.getInstance().uuidToWebSocket.putIfAbsent(player, conn);

                Server.getInstance().registerPlayer(player);

                Response response = new Response();
                response.setResponse(ResponseEnum.Logged);
                response.setUuid(player.getUuid());
                response.setNotify("Connected to the server ! Welcome to Arena!");
                response.send(player.getUuid());

                return;
            }

            Core.getInstance().receive(message);
        } catch (JsonParseException e) {
            Logger.failure("Failed to parse message: " + e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Logger.error("WebSocket error: " + ex.getMessage());
    }
}
