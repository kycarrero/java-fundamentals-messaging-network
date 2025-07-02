package com.aroldev.messagingnetwork;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TestUtilities {
    public static Class<?> getMessageClass() throws ClassNotFoundException {
        return Class.forName("com.aroldev.messagingnetwork.Message");
    }

    public static Object newMessage(String message, String sender, String type) {
        try {
            long now = System.currentTimeMillis();
            Class<?> messageClass = getMessageClass();
            Class<?> messageTypeClass = Class.forName("com.aroldev.messagingnetwork.Enum.MessageType");

            Object messageType = Enum.valueOf((Class<Enum>) messageTypeClass, type);

            Constructor<?> constructor = messageClass.getConstructor(String.class, long.class, messageTypeClass, String.class);
            return constructor.newInstance(message, now, messageType, sender);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object newMessage(String message) {
        return newMessage(message, "Mario", "TEXT");
    }
}
