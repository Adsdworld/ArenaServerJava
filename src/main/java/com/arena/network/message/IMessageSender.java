package com.arena.network.message;

import com.arena.game.GameNameEnum;

public interface IMessageSender {
    void sendMessage(Message message, GameNameEnum gameName);
}
