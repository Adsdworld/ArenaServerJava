package com.arena;

import com.arena.network.JavaWebSocket;
import com.arena.server.Server;
import com.arena.utils.Logger;

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

        Logger.info("\n\n******************************************");
        Logger.info("Démarrage du serveur");

        // Démarrage du serveur WebSocket
        JavaWebSocket.getInstance().start();

        // Démarrage du serveur Arena
        Server.getInstance();
    }
}
