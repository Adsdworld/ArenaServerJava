package com.arena;

import com.arena.network.message.Message;

// TODO: class inutilisé, on à considéré qu'un meessage du serveur est un Reponse.java mais pas Message.java.
public class MessageService {

    private static IMessageSender messageSender;

    public static void setMessageSender(IMessageSender sender) {
        messageSender = sender;
    }

    private static IMessageSender getMessageSender() {
        return messageSender;
    }

    public static void Send(Message message) {
        if (messageSender != null) {
            TestClientJava.getInstance().setLastSentTimestamp(System.currentTimeMillis());
            //TestClientJava.getInstance().clearMessages();
            MessageService.getMessageSender().sendMessage(message);
        } else {
            System.err.println("⚠ No MessageSender is set in MessageService.");
        }
    }
}
