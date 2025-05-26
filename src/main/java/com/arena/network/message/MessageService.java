package com.arena.network.message;

import com.arena.game.GameNameEnum;

public class MessageService {

    private static IMessageSender messageSender;

    public static void setMessageSender(IMessageSender sender) {
        messageSender = sender;
    }

    public static IMessageSender getMessageSender() {
        return messageSender;
    }

    public static void send(Message message, GameNameEnum gameName) {
        if (messageSender != null) {
            messageSender.sendMessage(message, gameName);
        } else {
            System.err.println("⚠ No MessageSender is set in MessageService.");
        }
    }
}
