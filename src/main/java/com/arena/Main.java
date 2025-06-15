package com.arena;

import com.arena.network.JavaWebSocket;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;
import com.arena.utils.json.GsonWorker;

/**
 * Main class
 * This is the entry point of the java application.
 * It starts the JavaWebSocket server.
 */
public class Main {
    /**
     * Main method, entry point of the application.
     * This method initializes the WebSocket server and starts the Arena server.
     *
     * @param args command line arguments, <port> to specify the port number for the WebSocket server.
     * @implNote If no port is specified, it defaults to 54099.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void main(String[] args) {

        /* Port configuration */
        int port = 54099;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                Logger.error("Port number must be an integer. Using default port: " + port);
            }
        }

        Logger.info("\n\n******************************************");
        Logger.info("Starting Arena...");

        // Démarrage du serveur WebSocket
        JavaWebSocket.initialize(port);
        JavaWebSocket.getInstance().start();

        // Démarrage du serveur Arena
        Server.getInstance();

        new GsonWorker();
    }
}
