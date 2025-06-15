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
    public static void main(String[] args) {

        /*
         * Surefire in pom.xml handle UTF-8 for tests and code.
         * //System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
         * //System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
         */

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
