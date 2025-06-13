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

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
        handlers.put(ActionEnum.PlayerStateUpdate, new PlayerStateUpdateHandler());
        handlers.put(ActionEnum.CastQ, new CastQHandler());
        handlers.put(ActionEnum.CastW, new CastWHandler());
        handlers.put(ActionEnum.CastE, new CastEHandler());
        handlers.put(ActionEnum.CastR, new CastRHandler());


        // Traitement toutes les 50ms
        _isEnteringTick = false;
        scheduler.scheduleAtFixedRate(this::processMessages, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void receive(Message message) {
        if (message == null) {
            Logger.failure("Received null message");
        } else if (message.getAction() == null) {
            Logger.failure("Unknown or invalid action in message: " + message);
        } else {
            messageQueue.offer(message);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneOffset.UTC);
            Logger.info("Offered message to queue: " + message.getAction() + " with timestamp: " + formatter.format(Instant.ofEpochMilli(message.getTimeStamp())));
        }
    }


    private void processMessages() {
        try {
            long now = System.currentTimeMillis();
            //Logger.info("Processing " + messageQueue.size() + " messages");


            /* Raspberry Pi 3b+ (1 Go) : max 178ms for a CreateGame action
             * VivoBook A.SALLIER (16 Go) : max 9ms for a CreateGame action
             *
             */
            long tolerance = 200;
            _isEnteringTick = true;
            boolean _isEmpty = messageQueue.isEmpty();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneOffset.UTC);

            while (!messageQueue.isEmpty()) {
                if (_isEnteringTick) {
                    StringBuilder stringBuilder = new StringBuilder("Actions in queue (" + messageQueue.size() + ") : ");
                    for (Message message : messageQueue) {
                        stringBuilder.append(message.getAction()).append(", ");
                    }
                    Logger.server(stringBuilder.toString());
                    _isEnteringTick = false;
                    if (!_isEmpty) {
                        //Logger.server("Processing messages at " + formatter.format(Instant.ofEpochMilli(now)) + " with tolerance: " + tolerance + "ms");
                    }
                }



                Message next = messageQueue.peek(); // pas encore retiré
                if (next == null) {
                    break;
                }

                assert next != null;
                if (next.getTimeStamp() < now - tolerance) {
                    //Logger.server("Skipping message: " + next.getUuid() + " >>> "+ next.getAction() + " (timestamp: " + formatter.format(Instant.ofEpochMilli(next.getTimeStamp())) + ")");
                    messageQueue.poll();
                } else if (next.getTimeStamp() <= now) {
                    //Logger.server("Processing message: " + next.getUuid() + " >>> " + next.getAction() + " (timestamp: " + formatter.format(Instant.ofEpochMilli(next.getTimeStamp())) + ")");
                    handleMessage(next);
                    messageQueue.poll();
                } else {
                    //Logger.server("Message not ready yet: " + next.getUuid() + " >>> " + next.getAction() + " (timestamp: " + formatter.format(Instant.ofEpochMilli(next.getTimeStamp())) + ")");
                    break;
                }
            }

            // Une fois les messages traités, on renvoie l’état du jeu
            sendGameState();

            if (!_isEmpty) {
                long endTime = System.currentTimeMillis();
                long duration = endTime - now;
                Logger.info("processMessages total duration: " + duration + " ms");
            }
        } catch (Exception e) {
            Logger.error("Exception while processing messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleMessage(Message message) {
        try {
            // Logique spécifique à ton jeu : mouvement, attaque, etc.
            //Logger.info("Traitement du message : " + message.toString());

            IMessageHandler handler = handlers.get(message.getAction());

            if (handler != null) {
                handler.handle(message);
                //Logger.info("Handled action: " + message.getAction() + " for player: " + message.getUuid());
            } else {
                Logger.failure("Couldn't find handler for action " + message.getAction());
            }
        } catch (Exception e) {
            Logger.error("Exception while handling message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendGameState() {
        try {
            //Logger.info("Starting to send game state to all clients..." + new Date());
            for (Game game : Server.getInstance().getGames()) {
                Response response = new Response();
                response.setResponse(ResponseEnum.GameState);
                response.setGameName(game.getGameNameEnum());
                response.setLivingEntities(game.getLivingEntities());
                response.Send(game.getGameNameEnum(), true);
            }
            //Logger.server("Game state sent to all clients at " + new Date());
        } catch (Exception e) {
            Logger.error("Exception while sending game state: " + e.getMessage());
            e.printStackTrace();
        }


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