package com.arena;

import com.arena.network.message.Message;
import com.arena.utils.Logger;

import static com.arena.ArenaTestBase.client_test;

public class MessageSender implements IMessageSender {

    @Override
    public void sendMessage(Message message) {
        client_test.send(message.toString());
        Logger.test("Message : " + message);
    }
}
