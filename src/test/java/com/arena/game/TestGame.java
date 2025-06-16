package com.arena.game;

import com.arena.MessageService;
import com.arena.game.core.Core;
import com.arena.network.message.Message;
import com.arena.player.ActionEnum;
import com.arena.server.Server;
import com.arena.utils.logger.Logger;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestGame {
    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {
        Logger.info("_____________________________________________");
        Logger.info(">>> " + testInfo.getDisplayName());
    }

    @Test
    @Order(1)
    void testConstructeur() throws InterruptedException {
        Server server = Server.getInstance();

        assertNotNull(server);
    }

}
