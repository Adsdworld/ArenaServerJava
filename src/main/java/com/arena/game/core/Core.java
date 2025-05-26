package com.arena.game.core;

import com.arena.game.handler.CreateGameHandler;
import com.arena.game.handler.IMessageHandler;
import com.arena.game.handler.JoinHandler;
import com.arena.network.JavaWebSocket;
import com.arena.network.message.Message;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
import com.arena.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Core {

    private static Core core;

    public static Core getInstance() {
        if (core == null) {
            core = new Core();
        }
        return core;
    }

    private final Map<ActionEnum, IMessageHandler> handlers = new HashMap<>();

    // Queue triée par timestamp
    private final PriorityBlockingQueue<Message> messageQueue = new PriorityBlockingQueue<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Core() {
        handlers.put(ActionEnum.CreateGame, new CreateGameHandler());
        handlers.put(ActionEnum.Join, new JoinHandler());
        // Traitement toutes les 50ms
        scheduler.scheduleAtFixedRate(this::processMessages, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void receive(Message message) {
        messageQueue.offer(message); // ajoute à la queue triée
    }

    private void processMessages() {
        long now = System.currentTimeMillis();

        // On peut fixer une tolérance (ex: 2 ticks max d'écart, soit 100 ms)
        long tolerance = 100;

        while (!messageQueue.isEmpty()) {
            Message next = messageQueue.peek(); // pas encore retiré

            if (next.getTimeStamp() <= now + tolerance) {
                messageQueue.poll(); // on retire car il est dans la bonne fenêtre
                handleMessage(next); // traitement de l'action
            } else {
                break; // les autres messages sont pour plus tard
            }
        }

        // Une fois les messages traités, on renvoie l’état du jeu
        sendGameState();
    }

    private void handleMessage(Message message) {
        // Logique spécifique à ton jeu : mouvement, attaque, etc.
        Logger.info("Traitement du message : " + message.toString());

        IMessageHandler handler = handlers.get(message.getAction());

        if (handler != null) {
            handler.handle(message);
        } else {
            Logger.failure("Couldn't find handler for action " + message.getAction());
        }
    }

    private void sendGameState() {

        // TODO: construire et envoyer l'état du jeu aux clients
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}