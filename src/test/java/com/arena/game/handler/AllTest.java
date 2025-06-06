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


    @Test
    @Order(2)
    void testUnityCreateGame() throws InterruptedException {
        Message message = new Message();
        //message.setTimeStamp(System.currentTimeMillis());
        message.setGameName(GameNameEnum.Game1);
        message.setAction(ActionEnum.CreateGame);
        //message.setUuid(TestClientJava.testUuid);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        System.out.println("Réponses reçues : " + responses);

        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GameCreated, responses);

        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseEnum.GameCreated, response.getReponse(), "Expected response to be " + ResponseEnum.GameCreated);

}

}
