package com.arena;

import org.junit.jupiter.api.BeforeAll;

public abstract class ArenaTestBase {

    public static TestClientJava client_test;

    private static boolean serverStarted = false;

    public static final int TEST_PORT = 54100;


    @BeforeAll
    public static void globalSetup() throws Exception {
        if (!serverStarted) {
            // Démarre le serveur une seule fois pour tous les tests
            new Thread(() -> Main.main(new String[]{String.valueOf(TEST_PORT)})).start();

            Thread.sleep(1000); // Laisser le temps au serveur de se lancer

            // Connexion du client_test WebSocket
            client_test = TestClientJava.getInstance();

            //new TestClientJava(new URI("ws://localhost:54099"));
            client_test.connectBlocking(); // Bloque jusqu'à la connexion
            serverStarted = true;
        }
    }
}
