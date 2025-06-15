package com.arena;

import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.building.Inhibitor;
import com.arena.game.entity.building.Nexus;
import com.arena.game.entity.building.Tower;
import com.arena.game.entity.building.TowerDead;
import com.arena.game.entity.champion.Garen;
import com.arena.network.response.Response;
import com.arena.player.ResponseEnum;
import com.arena.utils.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public  class TestClientJava extends WebSocketClient {

    private final PriorityBlockingQueue<Response> responseQueue = new PriorityBlockingQueue<>();

    private long lastSentTimestamp = System.currentTimeMillis();

    public static final String testUuid = "test-uuid-12345-abcde-67890-fghij";

    // A simple Gson could not deserialize abstract classes such as LivingEntity.
    //public static final Gson gson = new Gson();

    /* * A Gson with a RuntimeTypeAdapterFactory to handle polymorphic types.
     * This allows us to deserialize LivingEntity and its subclasses correctly.
     */
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory.of(LivingEntity.class, "name")
                            .registerSubtype(Garen.class, "Garen")
                            .registerSubtype(Tower.class, "Tower")
                            .registerSubtype(TowerDead.class, "TowerDead")
                            .registerSubtype(Inhibitor.class, "Inhibitor")
                            .registerSubtype(Nexus.class, "Nexus")
            )
            .create();

    private static TestClientJava instance;

    public static TestClientJava getInstance() {
        if (instance == null) {
            instance = new TestClientJava(URI.create("ws://localhost:" + ArenaTestBase.TEST_PORT));
        }
        return instance;
    }

    public TestClientJava(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        MessageService.setMessageSender(new MessageSender());
        Logger.test("Client connected to server: " + getURI());
    }

    @Override
    public void onMessage(String response_) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Logger.test("Response : " + response_);

        Response response = gson.fromJson(response_, Response.class);

        if (response.getTimestamp() >= lastSentTimestamp) {
            responseQueue.add(response);
            //Logger.test("Response added to queue: " + response.getTimestamp() + " | Last sent: " + lastSentTimestamp);
        } else {
            Logger.test("#Warning Ignored response with old timestamp: " + response.getTimestamp());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Client déconnecté");
    }

    @Override
    public void onError(Exception e) {
        Logger.error("Client error: " + e.getMessage());
    }

    /**
     * Wait for the next messages in the queue.
     * This method will block until at least one message is available or the timeout is reached.
     * @return
     * @throws InterruptedException
     */
    public ArrayList<Response> waitForNextMessages() throws InterruptedException {
        Thread.sleep(1000);

        ArrayList<Response> responses = new ArrayList<>();

        long serverTimeoutMillis = 3000;
        Response firstResponse = responseQueue.poll(serverTimeoutMillis, TimeUnit.MILLISECONDS);
        if (firstResponse != null) {
            responses.add(firstResponse);
            responseQueue.drainTo(responses);
        }
        return responses;
    }

    public static ArrayList<Response> waitForNextMessagesStatic() throws InterruptedException {
        return getInstance().waitForNextMessages();
    }

    /**
     * Get the last sent timestamp.
     * This is used to ensure that responses are processed in the correct order.
     */
    public long getLastSentTimestamp() {
        return lastSentTimestamp;
    }

    public static long getLastSentTimestampStatic() {
        return getInstance().getLastSentTimestamp();
    }

    /**
     * Set the last sent timestamp.
     * This is used to ensure that responses are processed in the correct order.
     */
    public void setLastSentTimestamp(long lastSentTimestamp_) {
        if (lastSentTimestamp <= lastSentTimestamp_) {
            if (!(lastSentTimestamp == lastSentTimestamp_)) {
                lastSentTimestamp = lastSentTimestamp_;
            }
        } else {
            Logger.test("#Warning: Attempted to set lastSentTimestamp to a value less than the current value. Current: " + lastSentTimestamp + ", Attempted: " + lastSentTimestamp_);
        }
    }

    public static void setLastSentTimestampStatic(long lastSentTimestamp_) {
        getInstance().setLastSentTimestamp(lastSentTimestamp_);
    }

    /**
     * @param responseEnum
     * @param responses
     * @return
     */
    public Response filterResponse(ResponseEnum responseEnum, ArrayList<Response> responses) {
        for (Response response : responses) {
            if (response.getResponse().equals(responseEnum)) {
                return response;
            }
        }
        return null;
    }

    public static Response filterResponseStatic(ResponseEnum responseEnum, ArrayList<Response> responses) {
        return getInstance().filterResponse(responseEnum, responses);
    }

    public ArrayList<Response> filterResponse(List<ResponseEnum> responsesEnum, ArrayList<Response> responses) {
        ArrayList<Response> filteredResponses = new ArrayList<>();
        for (Response response : responses) {
            for (ResponseEnum responseEnum : responsesEnum) {
                if (response.getResponse().equals(responseEnum)) {
                    filteredResponses.add(response);
                }
            }
        }
        return filteredResponses;
    }

    public static ArrayList<Response> filterResponseStatic(List<ResponseEnum> responsesEnum, ArrayList<Response> responses) {
        return getInstance().filterResponse(responsesEnum, responses);
    }
}
