package com.arena.game.core;

import com.arena.network.message.Message;
import com.arena.player.ActionEnum;
import com.arena.utils.logger.Logger;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCore {
    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {
        Logger.info("_____________________________________________");
        Logger.info(">>> " + testInfo.getDisplayName());
    }

    @Test
    @Order(1)
    void CoreReceiveNullMessage() throws InterruptedException {
        assertFalse(Core.getInstance().receive(null));
    }

    @Test
    @Order(2)
    void CoreReceiveMessageWithNullAction() throws InterruptedException {
        Message message = new Message();
        message.setAction(null);
        assertFalse(Core.getInstance().receive(message));
    }

    /**
     * Skipping message and message not ready could have conflict with thread and cannot directly be test in processingQueue().
     * There is no access to messageQueue and timestamp are mostly dependant of network for Unity not for java test
     * Unity automatically calculate the difference between response timestamp from server and his own.
     */

    @Test
    @Order(3)
    void CoreHandleMessageWithBadAction() throws InterruptedException {
        Message message = new Message();
        message.setAction(ActionEnum.Unknown);
        assertFalse(Core.getInstance().handleMessage(message));
    }
}
