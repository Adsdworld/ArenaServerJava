package com.arena.network;

import com.arena.game.GameNameEnum;
import com.arena.game.core.Core;
import com.arena.network.message.BroadcastMessage;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
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

    private static final int PORT = 54099;
    private static JavaWebSocket instance;
    private final Gson gson = new Gson();

    public Map<WebSocket, Player> webSocketToUuid;
    public Map<Player, WebSocket> uuidToWebSocket;

    private JavaWebSocket() {
        super(new InetSocketAddress(PORT));
        Logger.info("JavaWebSocket created on port " + PORT);
        webSocketToUuid = new HashMap<WebSocket, Player>();
        uuidToWebSocket = new HashMap<Player, WebSocket>();
    }

    // Méthode d'accès au singleton
    public static synchronized JavaWebSocket getInstance() {
        if (instance == null) {
            instance = new JavaWebSocket();
        }
        return instance;
    }

    @Override
    public void onStart() {
        Logger.info("JavaWebSocket started and listening on port " + PORT);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Logger.info("New connection from: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Logger.info("Connection closed: " + conn.getRemoteSocketAddress() + " Reason: " + reason);
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
                return;
            }

            Core.getInstance().receive(message);
        } catch (Exception e) {
            Logger.error("Failed to parse message: " + e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Logger.error("WebSocket error: " + ex.getMessage());
    }


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
