package com.arena;

import com.arena.network.message.Message;

public interface IMessageSender {
    void sendMessage(Message message);
}
