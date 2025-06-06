package com.arena.game.core;

import com.arena.game.Game;
import com.arena.game.entity.LivingEntity;
import com.arena.game.handler.*;
import com.arena.network.JavaWebSocket;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Core {

    private static Core core;
    private static boolean _isEnteringTick;

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
        handlers.put(ActionEnum.CloseGame, new CloseGameHandler());
        handlers.put(ActionEnum.CooldownStart, new CooldownStartHandler());

        // Traitement toutes les 50ms
        _isEnteringTick = false;
        scheduler.scheduleAtFixedRate(this::processMessages, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void receive(Message message) {
        messageQueue.offer(message); // ajoute à la queue triée
    }

    private void processMessages() {
        long now = System.currentTimeMillis();
        //Logger.info("Processing " + messageQueue.size() + " messages");

        // On peut fixer une tolérance (ex: 2 ticks max d'écart, soit 100 ms)
        long tolerance = 100;
        _isEnteringTick = true;

        while (!messageQueue.isEmpty()) {
            if (_isEnteringTick) {
                StringBuilder stringBuilder = new StringBuilder("Actions in queue (" + messageQueue.size() + ") : ");
                for (Message message : messageQueue) {
                    stringBuilder.append(message.getAction()).append(", ");
                }
                _isEnteringTick = false;
            }

            Message next = messageQueue.peek(); // pas encore retiré

            if (next.getTimeStamp() <= now + tolerance) {
                messageQueue.poll(); // on retire car il est dans la bonne fenêtre
                //Logger.info("Processing message: " + next.getAction());
                handleMessage(next); // traitement de l'action
            }
        }

        // Une fois les messages traités, on renvoie l’état du jeu
        sendGameState();
    }

    private void handleMessage(Message message) {
        // Logique spécifique à ton jeu : mouvement, attaque, etc.
        //Logger.info("Traitement du message : " + message.toString());

        IMessageHandler handler = handlers.get(message.getAction());

        if (handler != null) {
            handler.handle(message);
            //Logger.info("Handled action: " + message.getAction() + " for player: " + message.getUuid());
        } else {
            Logger.failure("Couldn't find handler for action " + message.getAction());
        }
    }

    private void sendGameState() {
        //Logger.info("Starting to send game state to all clients..." + new Date());
        for (Game game : Server.getInstance().getGames()) {
            Response response = new Response();
            response.setResponse(ResponseEnum.GameState);
            response.setGameName(game.getGameNameEnum());
            response.setLivingEntities(game.getLivingEntities());
            response.Send(game.getGameNameEnum(), true);
        }
        //Logger.server("Game state sent to all clients at " + new Date());

        // TODO: construire et envoyer l'état du jeu aux clients


    }

    public void retryLater(Message message) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Core.getInstance().receive(message);
            scheduler.shutdown();
        }, 100, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}