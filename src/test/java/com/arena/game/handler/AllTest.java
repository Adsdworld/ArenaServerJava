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
import com.arena.utils.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllTest extends ArenaTestBase {

    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {
        Logger.info("_____________________________________________");
        Logger.info(">>> " + testInfo.getDisplayName());
    }


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
        message.setAction(ActionEnum.Login);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.Logged, responses);

        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseEnum.Logged, response.getReponse(), "Expected response to be " + ResponseEnum.Logged);

        /**
         * Check if the UUID is set correctly
         * This checks if the UUID in the response matches the test UUID.
         */
        assertEquals(TestClientJava.testUuid, response.getUuid(), "Expected testUuid to be " + TestClientJava.testUuid);

        /**
         * Check if the player is registered on the server
         * This checks if the player with the test UUID is registered in the server's player list.
         */
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
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GameCreated, responses);

        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseEnum.GameCreated, response.getReponse(), "Expected response to be " + ResponseEnum.GameCreated);

        /**
         * Check if the game is created on the server
         * This checks if the game with the specified name is present in the server's game list.
         */
        assertTrue(Server.getInstance().getGames()
                .stream()
                .anyMatch(game -> {
                    if (game.getGameNameEnum().equals(GameNameEnum.Game1)) {
                        return true;
                    }
                    return false;
                }
                ),
                "Game with name '" + GameNameEnum.Game1 + "' should be created on server");
    }


    @Test
    @Order(3)
    void testUnityGameAlreadyExists() throws InterruptedException {
        Message message = new Message();
        message.setAction(ActionEnum.CreateGame);
        message.setGameName(GameNameEnum.Game1);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GameAlreadyExists, responses);

        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseEnum.GameAlreadyExists, response.getReponse(), "Expected response to be " + ResponseEnum.GameAlreadyExists);

        /**
         * Check if the game is created on the server
         * This checks if the game with the specified name is present in the server's game list.
         */
        assertTrue(Server.getInstance().getGames()
                        .stream()
                        .anyMatch(game -> {
                                    if (game.getGameNameEnum().equals(GameNameEnum.Game1)) {
                                        return true;
                                    }
                                    return false;
                                }
                        ),
                "Game with name '" + GameNameEnum.Game1 + "' should be created on server");
    }

    /**
     * Test Unity : Join Game
     * This test simulates a player joining a game by sending a join game message to the server.
     * <p>
     * The expected behavior is that the server responds with a "Joined" response.
     *
     * @throws InterruptedException
     */

    @Test
    @Order(5)
    void testUnityJoin() throws InterruptedException {
        Message message = new Message();
        message.setAction(ActionEnum.Join);
        message.setGameName(GameNameEnum.Game1);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.Joined, responses);

        assertNotNull(response, "Response should not be null After Join");
        assertEquals(ResponseEnum.Joined, response.getReponse(), "Expected response to be " + ResponseEnum.Joined);

        /**
         * Check if the player is added to the game
         * This checks if the player with the test UUID is present in the game's player list.
         */
        assertTrue(
                Server.getInstance().getGames().stream()
                        .filter(game -> game.getGameNameEnum() == GameNameEnum.Game1)
                        .findFirst()
                        .map(game -> game.getPlayers().stream()
                                .anyMatch(p -> p.getUuid().equals(TestClientJava.testUuid)))
                        .orElse(false),
                "Player with UUID '" + TestClientJava.testUuid + "' should be added to the game " + GameNameEnum.Game1
        );
    }

    // TODO: Order3 CloseGame > GameAlreadyExists
    // TODO: Order4 CloseGame > GamesLimitReached try to create Game6 "Game 6" when there are already 5 games created
    // TODO: Order6 Join > PlayerAlreadyInGame
    // TODO: Order7 Just listen GameState Response to see that Server send game state to all players in the game
    // TODO: CooldownStart > GameState vérifier que un gamestate parmis les plusieurs reçus contient les cooldowns mis à jour (cooldownUpToDate = false for Response response : responses (filter multiples response by GameState) reponse.cooldownQStart, cooldownQEnd, cooldownWStart, cooldownWEnd, cooldownEStart, cooldownEEnd, cooldownRStart, cooldownREnd are okay according to cooldownQMs, cooldownWMs, cooldownEMs, cooldownRMs)

    //TODO: Look at Jacoco coverage and make improvements
}
