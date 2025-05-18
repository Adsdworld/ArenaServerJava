package com.arena;

import com.arena.utils.Logger;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        Logger.info("**********************************************************");
        Logger.info("Démarrage du serveur");

        // TODO: start server
    }
}
