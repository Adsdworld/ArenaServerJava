package com.arena.game.core;

import com.arena.game.Game;
import com.arena.game.handler.HandlersCast;
import com.arena.game.handler.HandlersGame;
import com.arena.game.handler.IMessageHandler;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.ActionEnum;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Core {

    private static Core core;
    private static boolean isEnteringTick;

    /**
     * getInstance is a method that returns the singleton instance of Core.
     *
     * @return Core instance.
     * @implNote If the instance is not initialized, it creates a new instance.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static Core getInstance() {
        if (core == null) {
            core = new Core();
        }
        return core;
    }

    public PriorityBlockingQueue<Message> getQueue() {
        return messageQueue;
    }

    private final Map<ActionEnum, IMessageHandler> handlers = new HashMap<>();

    private final PriorityBlockingQueue<Message> messageQueue = new PriorityBlockingQueue<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Core() {
        /*handlers.put(ActionEnum.CreateGame, new CreateGameHandler());
        handlers.put(ActionEnum.Join, new JoinHandler());
        handlers.put(ActionEnum.CloseGame, new CloseGameHandler());
        handlers.put(ActionEnum.PlayerStateUpdate, new PlayerStateUpdateHandler());
        handlers.put(ActionEnum.CastQ, new CastQHandler());
        handlers.put(ActionEnum.CastW, new CastWHandler());
        handlers.put(ActionEnum.CastE, new CastEHandler());
        handlers.put(ActionEnum.CastR, new CastRHandler());*/
        handlers.putAll(HandlersGame.HANDLERS);
        handlers.putAll(HandlersCast.HANDLERS);


        isEnteringTick = false;
        scheduler.scheduleAtFixedRate(this::processMessages, 0, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * Receive is a method that receives a message and adds it to the message queue.
     *
     * @param message the {@link Message} to be processed.
     * @return true if the message was successfully added to the queue, false otherwise.
     * @implNote Checks if the message is null or if the action is invalid, logging an error if so.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public boolean receive(Message message) {
        boolean result = false;
        if (message == null) {
            Logger.failure("Received null message");
        } else if (message.getAction() == null) {
            Logger.failure("Unknown or invalid action in message: " + message);
        } else {
            messageQueue.offer(message);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneOffset.UTC);
            //Logger.info("Offered message to queue: " + message.getAction() + " with timestamp: " + formatter.format(Instant.ofEpochMilli(message.getTimeStamp())));
            result = true;
        }
        return result;
    }

    /**
     * processMessages is a method that processes messages in the queue.
     *
     * @implNote It checks the timestamp of each message against the current time and a tolerance value.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    private void processMessages() {
        long now = System.currentTimeMillis();
        //Logger.info("Processing " + messageQueue.size() + " messages");


        /* Raspberry Pi 3b+ (1 Go) : max 178ms for a CreateGame action
         * VivoBook A.SALLIER (16 Go) : max 9ms for a CreateGame action
         *
         */
        long tolerance = 200;
        isEnteringTick = true;
        boolean isQueueEmpty = messageQueue.isEmpty();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .withZone(ZoneOffset.UTC);

        while (!messageQueue.isEmpty()) {
            if (isEnteringTick) {
                StringBuilder stringBuilder = new StringBuilder("Actions in queue (" + messageQueue.size() + ") : ");
                for (Message message : messageQueue) {
                    stringBuilder.append(message.getAction()).append(", ");
                }
                Logger.server(stringBuilder.toString());
                isEnteringTick = false;
                if (!isQueueEmpty) {
                    Logger.server("Processing messages at " + formatter.format(Instant.ofEpochMilli(now)) + " with tolerance: " + tolerance + "ms");
                }
            }



            Message next = messageQueue.peek(); // pas encore retiré
            /*if (next == null) {
                break;
            }*/

            if (next.getTimeStamp() < now - tolerance) {
                Logger.server("Skipping message: " + next.getUuid() + " >>> "+ next.getAction() + " (timestamp: " + formatter.format(Instant.ofEpochMilli(next.getTimeStamp())) + ")");
                messageQueue.poll();
            } else if (next.getTimeStamp() <= now + tolerance) {
                Logger.server("Processing message: " + next.getUuid() + " >>> " + next.getAction() + " (timestamp: " + formatter.format(Instant.ofEpochMilli(next.getTimeStamp())) + ")");
                handleMessage(next);
                messageQueue.poll();
            } else {
                Logger.server("Message not ready yet: " + next.getUuid() + " >>> " + next.getAction() + " (timestamp: " + formatter.format(Instant.ofEpochMilli(next.getTimeStamp())) + ")");
                break;
            }
        }

        sendGameState();

        if (!isQueueEmpty) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - now;
            Logger.info("processMessages total duration: " + duration + " ms");
        }
    }

    /**
     * handleMessage is a method that handles a message by finding the appropriate handler and processing it.
     *
     * @param message the {@link Message} to be handled.
     * @return true if the handler was found, false otherwise.
     * @implNote It retrieves the handler for the action specified in the message and calls its handle method.
     * @author A.SALLIER
     * @date 2025-06-16
     */
    public boolean handleMessage(Message message) {
        //Logger.info("Traitement du message : " + message.toString());

        IMessageHandler handler = handlers.get(message.getAction());

        if (handler != null) {
            handler.handle(message);
            //Logger.info("Handled action: " + message.getAction() + " for player: " + message.getUuid());
            return true;
        } else {
            Logger.failure("Couldn't find handler for action " + message.getAction());
            return false;
        }
    }

    private void sendGameState() {
            //Logger.info("Starting to send game state to all clients..." + new Date());
            for (Game game : Server.getInstance().getGames()) {
                Response response = new Response();
                response.setResponse(ResponseEnum.GameState);
                response.setGameName(game.getGameNameEnum());
                response.setLivingEntities(game.getLivingEntities());
                response.send(game.getGameNameEnum(), true);
            }
            //Logger.server("Game state sent to all clients at " + new Date());
    }

    /*public void retryLater(Message message) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Core.getInstance().receive(message);
            scheduler.shutdown();
        }, 100, TimeUnit.MILLISECONDS);
    }*/

    /*public void shutdown() {
        scheduler.shutdown();
    }*/
}