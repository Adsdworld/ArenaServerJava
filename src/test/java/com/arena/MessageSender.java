package com.arena;

import com.arena.network.message.Message;
import com.arena.utils.json.JsonService;
import com.arena.utils.logger.Logger;

import static com.arena.ArenaTestBase.client_test;

public class MessageSender implements IMessageSender {

    @Override
    public void sendMessage(Message message) {
        //client_test.send(message.toString());
        client_test.send(JsonService.getWorker().toJson(message));
        Logger.test("Message : " + message);
    }
}
