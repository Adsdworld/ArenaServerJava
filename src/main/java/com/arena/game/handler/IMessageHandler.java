package com.arena.game.handler;

import com.arena.network.message.Message;

public interface IMessageHandler {
    public void handle(Message message);
}
