package com.arena;

import com.arena.network.message.Message;

import static com.arena.TestClientJava.testUuid;

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
            long now = System.currentTimeMillis();
            message.setUuid(testUuid);
            message.setTimeStamp(now);

            TestClientJava.getInstance().setLastSentTimestamp(now);

            MessageService.getMessageSender().sendMessage(message);
        } else {
            System.err.println("No MessageSender is set in MessageService.");
        }
    }
}
