package com.arena.server;

import com.arena.game.Game;
import com.arena.game.GameNameEnum;
import com.arena.game.entity.building.Tower;
import com.arena.network.message.Message;
import com.arena.player.ActionEnum;
import com.arena.player.Player;
import com.arena.utils.logger.Logger;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServer {
    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {
        Logger.info("_____________________________________________");
        Logger.info(">>> " + testInfo.getDisplayName());
    }

    @Test
    @Order(1)
    void initServer() {
        Server server = Server.getInstance();
        for (Game g : new ArrayList<>(server.getGames())) {
            Message message = new Message();
            message.setAction(ActionEnum.CloseGame);
            message.setGameName(g.getGameNameEnum());
            server.closeGame(message);
        }

        Server.getInstance().getPlayersMap().clear();
    }

    @Test
    @Order(2)
    void testServerConstructeur() throws InterruptedException {
        Server server = Server.getInstance();

        assertNotNull(server);
        assertEquals(server.getGames(), new ArrayList<>(), "Server should start with an empty game list");
        assertEquals(server.getPlayersMap(), new ConcurrentHashMap<>(), "Server should start with an empty player map");
    }

    @Test
    @Order(3)
    void testToGetAGameThatDoesNotExistByPlayer() throws InterruptedException {
        Server server = Server.getInstance();

        assertNull(server.getGameOfPlayer(new Player("123-ABC")));
    }

    @Test
    @Order(4)
    void testToGetAGameExistByPlayer() throws InterruptedException {
        Server server = Server.getInstance();
        Player player = new Player("123-ABC-server");

        server.registerPlayer(player);

        Message message = new Message();
        message.setAction(ActionEnum.CloseGame);
        message.setGameName(GameNameEnum.Game6);
        message.setUuid("123-ABC-server");
        server.closeGame(message);

        message.setAction(ActionEnum.CreateGame);
        message.setGameName(GameNameEnum.Game6);
        message.setUuid("123-ABC-server");
        assertTrue(server.createGame(message), "Game should be created successfully");

        Game game = server.gameExists(GameNameEnum.Game6);

        assertNotNull(game, "Game should exist after creation");

        server.subscribePlayerToGame(player, game);

        assertNotNull(server.getGameOfPlayer(new Player("123-ABC-server")));
    }


    @Test
    @Order(5)
    void testToGetAGameThatDoesNotExistByLivingEntity() throws InterruptedException {
        Server server = Server.getInstance();
        Tower entity = new Tower("123-ABC", 0);

        assertNull(server.getGameOfEntity(entity));
    }



}
