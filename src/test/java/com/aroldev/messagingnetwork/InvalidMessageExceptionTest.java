package com.aroldev.messagingnetwork;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class InvalidMessageExceptionTest {

    @Test
    void testExceptionThrownOnInvalidMessage() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> InvalidMessageException = Class.forName("com.aroldev.messagingnetwork.Exceptions.InvalidMessageException");

        Class<?> Phone = Class.forName("com.aroldev.messagingnetwork.Phone");
        Class<?> Message = Class.forName("com.aroldev.messagingnetwork.Message");
        Class<?> MessageType = Class.forName("com.aroldev.messagingnetwork.Enum.MessageType");
        Object messageType = Enum.valueOf((Class<Enum>) MessageType, "TEXT");

        Object phone = Phone
                .getConstructor(String.class, int.class, int.class)
                .newInstance("Phone", 1, 5);
        Object invalidMsg = Message
                .getConstructor(String.class, long.class, MessageType, String.class)
                .newInstance("", System.currentTimeMillis(), messageType, "Test");

        Method sendMessage = Phone.getMethod("sendMessage", Message);


        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                () -> sendMessage.invoke(phone, invalidMsg),
                "Ahora un mensaje incorrecto debe lanzar un InvalidMessageException.");

        Throwable cause = thrown.getCause();
        assertNotNull(cause, "InvocationTargetException should wrap a cause");
        assertEquals(InvalidMessageException, cause.getClass(), "Expected InvalidMessageException");

        // Optional: check message
        assertTrue(cause.getMessage().toLowerCase().contains("invalid"), "El mensaje de error debe mencionar 'invalid'");

    }
}
