package com.arena;

import com.arena.utils.Logger;
import com.arena.server.Server;

public class Main {
    public static void main(String[] args) {

        /*
         * Surefire in pom.xml handle UTF-8 for tests and code.
         * //System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
         * //System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
         */

        Logger.info("******************************************");
        Logger.info("Démarrage du serveur");

        Server server = new Server();
        server.start();
    }
}
