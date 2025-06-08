package com.arena.network;

import com.arena.game.core.Core;
import com.arena.network.response.Response;
import com.arena.network.response.ResponseService;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;
import com.arena.network.message.Message;
import com.google.gson.*;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * JavaWebSocket is a singleton WebSocket server that listens for incoming connections
 * and processes messages in JSON format.
 * It uses Gson for JSON serialization/deserialization.
 */
public class JavaWebSocket extends WebSocketServer {

    public int port;
    private static JavaWebSocket instance;
    private final Gson gson = new Gson();

    public Map<WebSocket, Player> webSocketToUuid;
    public Map<Player, WebSocket> uuidToWebSocket;

    private JavaWebSocket(int port) {
        super(new InetSocketAddress(port));
        this.port = port;

        ResponseService.setResponseSender(new JavaWebSocketResponseSender());

        Logger.info("JavaWebSocket created on port " + port);
        webSocketToUuid = new HashMap<WebSocket, Player>();
        uuidToWebSocket = new HashMap<Player, WebSocket>();
    }

    // Méthode d'accès au singleton
    public static synchronized JavaWebSocket getInstance() {
        if (instance == null) {
            throw new IllegalStateException("JavaWebSocket not initialized. Call initialize(port) first.");
        }
        return instance;
    }

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

        /* Reason for closing the connection:
         * - code: the WebSocket close code.
         * - reason: a string explaining why the connection was closed.
         * - remote: true if the close was initiated by the remote peer, false if it was initiated by the server.
         */
        String reason_ = reason != null && !reason.isEmpty() ? reason : " No reason provided";
        Logger.info("Connection closed: " + conn.getRemoteSocketAddress() + reason_ + " (code: " + code + ", remote: " + remote + ")");
        Player player = webSocketToUuid.get(conn);
        if (player != null) {
            uuidToWebSocket.remove(player);
        }
        webSocketToUuid.remove(conn);

        // TODO: remove the player from the server using the connection linked to uuid of the player
    }

    @Override
    public void onMessage(WebSocket conn, String messageJson) {
        try {
            validateJson(messageJson, Message.class);

            Message message = gson.fromJson(messageJson, Message.class);

            Logger.info(message.getUuid() + " >>> " + message.getAction() + " : " + messageJson);

            if (message.getAction() == ActionEnum.Login) {
                Player player = new Player(message.getUuid());
                JavaWebSocket.getInstance().webSocketToUuid.put(conn, player);
                JavaWebSocket.getInstance().uuidToWebSocket.put(player, conn);

                Server.getInstance().registerPlayer(player);

                Response response = new Response();
                response.setResponse(ResponseEnum.Logged);
                response.setUuid(player.getUuid());
                response.setNotify("Connected to the server ! Welcome to Arena!");
                response.Send(player.getUuid());

                // TODO: design a spectator message on join
                /* for (Game game : Server.getInstance().getGames()) {
                    game.addSpectator(player);
                    //Message message = new Message();
                    //message.setAction();

                }*/
                return;
            }

            Core.getInstance().receive(message);
        } catch (Exception e) {
            Logger.failure("Failed to parse message: " + e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Logger.error("WebSocket error: " + ex.getMessage());
    }



    // TODO: move it to Utils
    // TODO: improve it to
    /**
     * Enum not found error : enum value present in the message but not in the Java enum
     * field not found error : field present in the message but not in the Java class
     */

    /**
     *
     *
     * @param messageJson
     * @param clazz
     */
    public static void validateJson(String messageJson, Class<?> clazz) {
        Gson gson = new Gson();

        JsonObject jsonRaw = JsonParser.parseString(messageJson).getAsJsonObject();

        Object parsedObject = gson.fromJson(messageJson, clazz);

        JsonObject jsonParsed = gson.toJsonTree(parsedObject).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonRaw.entrySet()) {
            String key = entry.getKey();
            JsonElement rawValue = entry.getValue();

            // Ignore les champs null dans le JSON brut
            if (rawValue == null || rawValue.isJsonNull()) {
                continue;
            }

            // Si le champ n'existe pas dans l'objet Java ou est devenu null → problème
            if (!jsonParsed.has(key) || jsonParsed.get(key).isJsonNull()) {
                Logger.warn("Donnée transmise ignorée ou enum non reconnue : '" + key + "' = " + rawValue);
            }
        }
    }
}
