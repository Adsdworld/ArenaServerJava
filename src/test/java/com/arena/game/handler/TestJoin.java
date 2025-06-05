package com.arena.game.handler;

import com.arena.ArenaTestBase;
import com.arena.MessageService;
import com.arena.TestClientJava;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.Logger;
import org.junit.jupiter.api.Test;
import com.google.gson.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestJoin extends ArenaTestBase {

    private static Gson gson = new Gson();

    /**
     * Test Unity : Join
     * This test simulates a player joining a game by sending a login message to the server.
     * <p>
     * The expected behavior is that the server responds with a "Logged" response.
     * <p>
     * [2025-06-05 17:27:41.677][UnityWebSocket][SendMessage][info] Message envoyé au serveur : {"_uuid":"8fe884b1-ac53-4f23-b4ab-f0a3e09d1029","_ability":null,"_x":null,"_z":null,"_timestamp":1749137261557,"_action":"Login","_gameName":"Game 1"}
     *
     * @throws InterruptedException
     */
    @Test
    void testJoin() throws InterruptedException {
        Message message = new Message();
        message.setTimeStamp(System.currentTimeMillis());
        message.setAction(ActionEnum.Login);
        message.setUuid(TestClientJava.testUuid);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        Response response = TestClientJava.filterResponseStatic(ResponseEnum.Logged, responses);

        assertNotNull(response, "Response should not be null");

        assertEquals(ResponseEnum.Logged, response.getReponse(), "Expected response to be " + ResponseEnum.Logged);

        assertEquals(TestClientJava.testUuid, response.getUuid(), "Expected testUuid to be " + TestClientJava.testUuid);

        assertTrue(Server.getInstance().getPlayers()
                        .stream()
                        .anyMatch(p -> p.getUuid().equals(TestClientJava.testUuid)),
                "Player with UUID '" + TestClientJava.testUuid + "' should be registered on server");
    }
}
