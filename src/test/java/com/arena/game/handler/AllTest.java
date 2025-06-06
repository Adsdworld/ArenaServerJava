package com.arena.game.handler;

import com.arena.ArenaTestBase;
import com.arena.MessageService;
import com.arena.TestClientJava;
import com.arena.game.GameNameEnum;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.ActionEnum;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllTest extends ArenaTestBase {

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
    @Order(1)
    void testUnityLogin() throws InterruptedException {
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

    /**
     * Test Unity : Create Game
     * This test simulates a player creating a game by sending a create game message to the server.
     * <p>
     * The expected behavior is that the server responds with a "GameCreated" response.
     *
     * @throws InterruptedException
     */


    @Test
    @Order(2)
    void testUnityCreateGame() throws InterruptedException {
        Message message = new Message();
        message.setAction(ActionEnum.CreateGame);
        message.setGameName(GameNameEnum.Game1);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        System.out.println("Réponses reçues : " + responses);

        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GameCreated, responses);

        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseEnum.GameCreated, response.getReponse(), "Expected response to be " + ResponseEnum.GameCreated);
    }

    /**
     * Test Unity : Join Game
     * This test simulates a player joining a game by sending a join game message to the server.
     * <p>
     * The expected behavior is that the server responds with a "Joined" response.
     *
     * @throws InterruptedException
     */

//    @Test
//    @Order(3)
//    void testUnityJoinGame() throws InterruptedException {
//        Message message = TestClientJava.CreateMessageStatic();
//        message.setAction(ActionEnum.Join);
//        message.setGameName(GameNameEnum.Game1);
//
//
//        MessageService.Send(message);
//
//        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
//
//        Response response = TestClientJava.filterResponseStatic(ResponseEnum.Joined, responses);
//
//        assertNotNull(response, "Response should not be null After Join");
//        assertEquals(ResponseEnum.Joined, response.getReponse(), "Expected response to be " + ResponseEnum.Joined);
//    }


}
