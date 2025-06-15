package com.arena.game.handler;

import com.arena.ArenaTestBase;
import com.arena.MessageService;
import com.arena.TestClientJava;
import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.game.entity.LivingEntity;
import com.arena.game.entity.champion.Garen;
import com.arena.game.utils.EntityInit;
import com.arena.game.utils.Position;
import com.arena.network.message.Message;
import com.arena.network.response.Response;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
import com.arena.player.ResponseEnum;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AllTest is a test class that contains various tests for the Arena game.
 *
 * > Login : Logged
 * > Create Game 1 : Game Created
 * > Create Game 1 : Game Already Exists
 * > Create Game 2, 3, 4, 5, 6 : Games Limit Reached
 * > Join Game 1 : Joined
 * > Join Game 1 : Player Already In Game
 * < Game State contains entities
 */
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
        assertEquals(ResponseEnum.Logged, response.getResponse(), "Expected response to be " + ResponseEnum.Logged);

        /**
         * Check if the UUID is set correctly
         * This checks if the UUID in the response matches the test UUID.
         */
        assertEquals(TestClientJava.testUuid, response.getUuid(), "Expected testUuid to be " + TestClientJava.testUuid);

        /**
         * Check if the player is registered on the server
         * This checks if the player with the test UUID is registered in the server's player list.
         */
        assertTrue(Server.getInstance().getPlayersMap().values()
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
        assertEquals(ResponseEnum.GameCreated, response.getResponse(), "Expected response to be " + ResponseEnum.GameCreated);

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
        assertEquals(ResponseEnum.GameAlreadyExists, response.getResponse(), "Expected response to be " + ResponseEnum.GameAlreadyExists);

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
        assertEquals(ResponseEnum.GamesLimitReached, response.getResponse(), "Expected response to be " + ResponseEnum.GamesLimitReached);
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
        assertEquals(ResponseEnum.Joined, response.getResponse(), "Expected response to be " + ResponseEnum.Joined);

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
        assertEquals(ResponseEnum.PlayerAlreadyInGame, response.getResponse(), "Expected response to be " + ResponseEnum.PlayerAlreadyInGame);
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
        assertEquals(ResponseEnum.GameState, response.getResponse(), "Expected response to be " + ResponseEnum.GameState);

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
    void testUnityCooldownQStart() throws InterruptedException {

        Message message = new Message();
        message.setAction(ActionEnum.CastQ);
        LivingEntity entity = new Garen(TestClientJava.testUuid, 2);
        entity.setCooldownQStart(System.currentTimeMillis());
        message.setLivingEntity(entity);

        MessageService.Send(message);

        long qtimestamp = TestClientJava.getLastSentTimestampStatic();

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        ArrayList<Response> res = TestClientJava.filterResponseStatic(List.of(ResponseEnum.GameState), responses);

        LivingEntity matchingEntity = res.stream()
                .flatMap(response -> response.getLivingEntities().stream())
                .filter(e -> e.getCooldownQStart() == qtimestamp)
                .findFirst()
                .orElse(null);
        assertNotNull(matchingEntity, "Entity with cooldown Q should be present in GameState:" +matchingEntity);

        assertEquals(qtimestamp, matchingEntity.getCooldownQStart(), "Cooldown Q start should be updated by casting Q");
        assertEquals(qtimestamp + entity.getCooldownQMs(), matchingEntity.getCooldownQEnd(), "Cooldown Q End should be updated by casting Q");

        try {
            LivingEntity e = new Garen(TestClientJava.testUuid, 2);
            Thread.sleep(e.getSkinAnimationDurationForQ() + 1000);
        } catch (Exception e) {
            Logger.test("Error while waiting for Q to end" + e.getMessage());
        }
    }

    @Test
    @Order(9)
    void testUnityCooldownWStart() throws InterruptedException {

        Message message = new Message();
        message.setAction(ActionEnum.CastW);
        LivingEntity entity = new Garen(TestClientJava.testUuid, 2);
        entity.setCooldownWStart(System.currentTimeMillis());
        message.setLivingEntity(entity);

        MessageService.Send(message);

        long timestamp = TestClientJava.getLastSentTimestampStatic();

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        ArrayList<Response> res = TestClientJava.filterResponseStatic(List.of(ResponseEnum.GameState), responses);

        LivingEntity matchingEntity = res.stream()
                .flatMap(response -> response.getLivingEntities().stream())
                .filter(e -> e.getCooldownWStart() == timestamp)
                .findFirst()
                .orElse(null);
        assertNotNull(matchingEntity, "Entity with cooldown W should be present in GameState:" +matchingEntity);

        assertEquals(timestamp, matchingEntity.getCooldownWStart(), "Cooldown Q start should be updated by casting W");
        assertEquals(timestamp + entity.getCooldownWMs(), matchingEntity.getCooldownWEnd(), "Cooldown Q End should be updated by casting W");

        try {
            LivingEntity e = new Garen(TestClientJava.testUuid, 2);
            Thread.sleep(e.getSkinAnimationDurationForW() + 1000);
        } catch (Exception e) {
            Logger.test("Error while waiting for W to end" + e.getMessage());
        }
    }

    @Test
    @Order(11)
    void testUnityCooldownEStart() throws InterruptedException {

        Message message = new Message();
        message.setAction(ActionEnum.CastE);
        LivingEntity entity = new Garen(TestClientJava.testUuid, 2);
        entity.setCooldownEStart(System.currentTimeMillis());
        message.setLivingEntity(entity);

        MessageService.Send(message);

        long timestamp = TestClientJava.getLastSentTimestampStatic();

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        ArrayList<Response> res = TestClientJava.filterResponseStatic(List.of(ResponseEnum.GameState), responses);

        LivingEntity matchingEntity = res.stream()
                .flatMap(response -> response.getLivingEntities().stream())
                .filter(e -> e.getCooldownEStart() == timestamp)
                .findFirst()
                .orElse(null);
        assertNotNull(matchingEntity, "Entity with cooldown E should be present in GameState:" +matchingEntity);

        assertEquals(timestamp, matchingEntity.getCooldownEStart(), "Cooldown Q start should be updated by casting E");
        assertEquals(timestamp + entity.getCooldownEMs(), matchingEntity.getCooldownEEnd(), "Cooldown Q End should be updated by casting E");

        try {
            LivingEntity e = new Garen(TestClientJava.testUuid, 2);
            Thread.sleep(e.getSkinAnimationDurationForE() + 1000);
        } catch (Exception e) {
            Logger.test("Error while waiting for E to end" + e.getMessage());
        }
    }

    @Test
    @Order(12)
    void testUnityCooldownRStart() throws InterruptedException {

        Message message = new Message();
        message.setAction(ActionEnum.CastR);
        LivingEntity entity = new Garen(TestClientJava.testUuid, 2);
        entity.setCooldownRStart(System.currentTimeMillis());
        message.setLivingEntity(entity);

        MessageService.Send(message);

        long timestamp = TestClientJava.getLastSentTimestampStatic();

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        ArrayList<Response> res = TestClientJava.filterResponseStatic(List.of(ResponseEnum.GameState), responses);

        LivingEntity matchingEntity = res.stream()
                .flatMap(response -> response.getLivingEntities().stream())
                .filter(e -> e.getCooldownRStart() == timestamp)
                .findFirst()
                .orElse(null);
        assertNotNull(matchingEntity, "Entity with cooldown R should be present in GameState:" +matchingEntity);

        assertEquals(timestamp, matchingEntity.getCooldownRStart(), "Cooldown Q start should be updated by casting R");
        assertEquals(timestamp + entity.getCooldownRMs(), matchingEntity.getCooldownREnd(), "Cooldown Q End should be updated by casting R");

        try {
            LivingEntity e = new Garen(TestClientJava.testUuid, 2);
            Thread.sleep(e.getSkinAnimationDurationForR() + 1000);
        } catch (Exception e) {
            Logger.test("Error while waiting for R to end" + e.getMessage());
        }
    }

    @Test
    @Order(13)
    void testUnityPlayerUpdateRunning() throws InterruptedException {

        Message message = new Message();
        message.setAction(ActionEnum.PlayerStateUpdate);
        Server server = Server.getInstance();

        Player player = server.getPlayerByUuid(TestClientJava.testUuid);
        Game game = server.getGameOfPlayer(player);
        LivingEntity entity = game.getLivingEntity(player);

        entity.setPosX(445);
        entity.setPosY(8);
        entity.setPosZ(490);
        entity.setRotationY(180);
        entity.setMoving(true);
        entity.setHasArrived(false);
        entity.setPosXDesired(101);
        entity.setPosYDesired(201);
        entity.setPosZDesired(101);

        message.setLivingEntity(entity);
        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        ArrayList<Response> res = TestClientJava.filterResponseStatic(List.of(ResponseEnum.GameState), responses);

        LivingEntity matchingEntity = res.stream()
                .flatMap(response -> response.getLivingEntities().stream())
                .filter(e -> e.getPosX() == 445)
                .findFirst()
                .orElse(null);
        assertNotNull(matchingEntity, "Entity should be present at posX:" +matchingEntity);

        assertEquals(entity.getSkinAnimationForRunning(), matchingEntity.getSkinAnimation(), "Running animation should be set for the entity");
    }

    @Test
    @Order(14)
    void testUnityPlayerUpdateIdle() throws InterruptedException {

        Message message = new Message();
        message.setAction(ActionEnum.PlayerStateUpdate);
        Server server = Server.getInstance();

        Player player = server.getPlayerByUuid(TestClientJava.testUuid);
        Game game = server.getGameOfPlayer(player);
        LivingEntity entity = game.getLivingEntity(player);

        entity.setPosX(445);
        entity.setPosY(8);
        entity.setPosZ(490);
        entity.setRotationY(180);
        entity.setMoving(false);
        entity.setHasArrived(false);
        entity.setPosXDesired(101);
        entity.setPosYDesired(201);
        entity.setPosZDesired(101);

        message.setLivingEntity(entity);
        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();

        ArrayList<Response> res = TestClientJava.filterResponseStatic(List.of(ResponseEnum.GameState), responses);

        LivingEntity matchingEntity = null;
        for (Response response : res) {
            for (LivingEntity e : response.getLivingEntities()) {
                if (e.getPosX() == 445) {
                    matchingEntity = e; // keep overwriting, so the last match stays
                }
            }
        }
        assertNotNull(matchingEntity, "Entity should be present at posX:" +matchingEntity);

        assertEquals(entity.getSkinAnimationForIdle(), matchingEntity.getSkinAnimation(), "Idle animation should be set for the entity");
    }

    @Test
    @Order(15)
    void endToWin() throws InterruptedException {
        Map<String, EntityInit> BLUE = new LinkedHashMap<>();
        BLUE.put("T1_MID_BLUE", new EntityInit(new Position(445.92f, 8.07f, 490.81f, 180f), true, "T2_MID_BLUE"));
        BLUE.put("T2_MID_BLUE", new EntityInit(new Position(419.68f, 8.07f, 438.94f, 180f), "T3_MID_BLUE"));
        BLUE.put("T3_MID_BLUE", new EntityInit(new Position(375.67f, 9.37f, 403.40f, 180f), "INHIB_MID_BLUE"));
        BLUE.put("INHIB_MID_BLUE", new EntityInit(new Position(360.45f, -7.36f, 389.32f, 0f), List.of("T4_BOT_BLUE", "T4_TOP_BLUE")));
        BLUE.put("T4_TOP_BLUE", new EntityInit(new Position(312.82f, 9.37f, 357.67f, 180f), "NEXUS_BLUE"));
        BLUE.put("T4_BOT_BLUE", new EntityInit(new Position(327.72f, 9.37f, 342.34f, 180f), "NEXUS_BLUE"));
        BLUE.put("NEXUS_BLUE", new EntityInit(new Position(307.62f, -13.5f, 337.96f, 0f), "None"));

        Server server = Server.getInstance();
        Player player = server.getPlayerByUuid(TestClientJava.testUuid);
        Game game = server.getGameOfPlayer(player);
        LivingEntity entity = game.getLivingEntity(player);

        ArrayList<Response> responses = new ArrayList<>();

        for (String entityInit : BLUE.keySet()) {
            entity.setPos(BLUE.get(entityInit).getPosition());
            Message message = new Message();
            message.setAction(ActionEnum.PlayerStateUpdate);
            message.setLivingEntity(entity);
            MessageService.Send(message);

            boolean dead = false;
            while (!dead) {
                Message message1 = new Message();
                message1.setAction(ActionEnum.CastQ);
                entity.setCooldownQStart(System.currentTimeMillis());
                message1.setLivingEntity(entity);
                MessageService.Send(message1);

                Logger.test("entry before writing response");
                responses = TestClientJava.waitForNextMessagesStatic();

                ArrayList<Response> res = TestClientJava.filterResponseStatic(List.of(ResponseEnum.GameState), responses);

                Response latest = res.stream()
                        .max(Comparator.comparingLong(Response::getTimestamp))
                        .orElse(null);

                LivingEntity enemy = latest.getLivingEntities().stream()
                        .filter(e -> Objects.equals(e.getGeneralId(), entityInit))
                        .findFirst()
                        .orElse(null);

                if (enemy == null || enemy.getHealth() <= 0) {
                    dead = true;
                }
            }
        }


        Response match = responses.stream()
                .filter(r -> r.getNotify() != null && r.getNotify().contains("wins!"))
                .findFirst()
                .orElse(null);

        assertNotNull(match, "Info response should contain a win message");
        Logger.test(match.getNotify());

    }

    @Test
    @Order(16)
    void testUnityCloseGame() throws InterruptedException {
        Message message = new Message();
        message.setAction(ActionEnum.CloseGame);
        message.setGameName(GameNameEnum.Game1);

        MessageService.Send(message);

        ArrayList<Response> responses = TestClientJava.waitForNextMessagesStatic();
        Response response = TestClientJava.filterResponseStatic(ResponseEnum.GameClosed, responses);

        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseEnum.GameClosed, response.getResponse(), "Expected response to be " + ResponseEnum.GameClosed);

        /**
         * Check if the game is created on the server
         * This checks if the game with the specified name is present in the server's game list.
         */
        assertFalse(Server.getInstance().getGames()
                        .stream()
                        .anyMatch(game -> {
                                    if (game.getGameNameEnum().equals(GameNameEnum.Game1)) {
                                        return true;
                                    }
                                    return false;
                                }
                        ),
                "Game with name '" + GameNameEnum.Game1 + "' should be closed on server");
    }


}
