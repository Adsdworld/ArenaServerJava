package com.arena.game.handler;

import com.arena.ArenaTestBase;
import com.arena.MessageService;
import com.arena.TestClientJava;
import com.arena.game.GameNameEnum;
import com.arena.game.entity.LivingEntity;
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
     * Test Unity : Login
     * This test simulates a player logging in by sending a login message to the server.
     * <p>
     * The expected behavior is that the server responds with a "Logged" response.
     *
     * @throws InterruptedException, Exception
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
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
     * @throws InterruptedException, Exception
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
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


    /**
     * Test Unity : Game Already Exists
     * This test simulates a player trying to create a game that already exists by sending a create game message to the server.
     * <p>
     * The expected behavior is that the server responds with a "GameAlreadyExists" response.
     *
     * @throws InterruptedException, Exception
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
     */
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
     * Test Unity : Games Limit Reached
     * This test simulates a player trying to create more games than the limit allows by sending create game messages to the server.
     * <p>
     * The expected behavior is that the server responds with a "GamesLimitReached" response when trying to create a 6th game.
     *
     * @throws InterruptedException, Exception
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
     */


    @Test
    @Order(4)
    void testUnityGameLimitReached() throws InterruptedException {
        // Create 5 games
        for (int i = 2; i <= 6; i++) {
            Message message = new Message();
            message.setAction(ActionEnum.CreateGame);
            message.setGameName(GameNameEnum.valueOf("Game" + i));

            MessageService.Send(message);
        }

        // Try to create a 6th game
        Message message = new Message();
        message.setAction(ActionEnum.CreateGame);
        message.setGameName(GameNameEnum.Game6);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GamesLimitReached, responses);

        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseEnum.GamesLimitReached, response.getReponse(), "Expected response to be " + ResponseEnum.GamesLimitReached);
    }


    /**
     * Test Unity : Join Game
     * This test simulates a player joining a game by sending a join game message to the server.
     * <p>
     * The expected behavior is that the server responds with a "Joined" response and adds the player to the game.
     *
     * @throws InterruptedException, Exception
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
     */

    @Test
    @Order(5)
    void testUnityJoin1() throws InterruptedException {
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


    /**
     * Test Unity : Player Already In Game
     * This test simulates a player trying to join a game that they are already in by sending a join game message to the server.
     * <p>
     * The expected behavior is that the server responds with a "PlayerAlreadyInGame" response.
     *
     * @throws InterruptedException, Exception
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
     */
    @Test
    @Order(6)
    void testUnityJoin2() throws InterruptedException {
        Message message = new Message();
        message.setAction(ActionEnum.Join);
        message.setGameName(GameNameEnum.Game1);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.PlayerAlreadyInGame, responses);

        assertNotNull(response, "Response should not be null After Join");
        assertEquals(ResponseEnum.PlayerAlreadyInGame, response.getReponse(), "Expected response to be " + ResponseEnum.PlayerAlreadyInGame);
    }


    /**
     * Test Unity : Game State
     * This test simulates a player requesting the game state by sending a game state message to the server.
     * <p>
     * The expected behavior is that the server responds with a "GameState" response containing the game state data.
     *
     * @throws InterruptedException, Exception
     * @implNote This test checks if the server sends the game state to all players in the game.
     * @implNote
     * @author A.BENETREAU
     * @date 2025-06-10
     */
    @Test
    @Order(7)
    void testUnityGameState() throws InterruptedException {
        // This test is to check if the server sends the game state to all players in the game
        // We will just listen for the GameState response and check if it contains the expected data

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GameState, responses);

        assertNotNull(response, "Response should not be null After GameState");
        assertEquals(ResponseEnum.GameState, response.getReponse(), "Expected response to be " + ResponseEnum.GameState);

        // Check if the game state contains the expected data
        assertFalse(response.getLivingEntities().isEmpty(), "GameState should contain living entities");
    }


    /**
     * Test Unity : Cooldown Start
     * This test simulates a player starting a cooldown by sending a cooldown start message to the server.
     * <p>
     * The expected behavior is that the server responds with a "GameState" response with updated cooldowns.
     *
     * @throws InterruptedException, Exception
     * @implNote This test checks if the server updates the cooldowns in the game state.
     * @author A.BENETREAU
     * @date 2025-06-10
     */
    @Test
    @Order(8)
    void testUnityCooldownStart() throws InterruptedException {

        //TODO:  lancer le cooldown Q

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GameState, responses);

        LivingEntity livingEntity = (LivingEntity) response.getLivingEntities()
                .stream()
                .filter(l -> l.getId().equals(TestClientJava.testUuid))
                .findFirst()
                .orElse(null);

        assertNull(livingEntity, "Living entity should not be null");
        long QStart = livingEntity.getCooldownQMs();
        long QEnd = livingEntity.getCooldownQEnd();
        long QMs = livingEntity.getCooldownQMs();

        assertTrue(cooldownUpToDateTrouve, "Au moins un GameState doit contenir des cooldowns mis à jour");
    }

    // TODO: Order3 CloseGame > GameAlreadyExists
    // TODO: Order4 CloseGame > GamesLimitReached try to create Game6 "Game 6" when there are already 5 games created
    // TODO: Order6 Join > PlayerAlreadyInGame
    // TODO: Order7 Just listen GameState Response to see that Server send game state to all players in the game
    // TODO: CooldownStart > GameState vérifier que un gamestate parmis les plusieurs reçus contient les cooldowns mis à jour
    //  (cooldownUpToDate = false for Response response : responses (filter multiples response by GameState)
    //  reponse.cooldownQStart, cooldownQEnd, cooldownWStart, cooldownWEnd, cooldownEStart, cooldownEEnd, cooldownRStart,
    //  cooldownREnd are okay according to cooldownQMs, cooldownWMs, cooldownEMs, cooldownRMs)

    //TODO: Look at Jacoco coverage and make improvements
}
