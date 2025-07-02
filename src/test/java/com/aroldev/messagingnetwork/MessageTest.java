package com.aroldev.messagingnetwork;

import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;

import static junit.framework.Assert.*;

class MessageTest {

    @Test
    public void testMessageTypeEnum() {
        try {
            Class<?> enumClass = Class.forName("com.aroldev.messagingnetwork.Enum.MessageType");
            assertTrue("Enum class should be an enum", enumClass.isEnum());

            Object[] enumConstants = enumClass.getEnumConstants();
            boolean hasText = false;
            boolean hasSystem = false;

            for (Object constant : enumConstants) {
                if (constant.toString().equals("TEXT")) {
                    hasText = true;
                } else if (constant.toString().equals("SYSTEM")) {
                    hasSystem = true;
                }
            }

            assertTrue("Enum should contain TEXT constant", hasText);
            assertTrue("Enum should contain SYSTEM constant", hasSystem);

        } catch (ClassNotFoundException e) {
            fail("Enum class not found: " + e.getMessage());
        }
    }

    @Nested
    class WithMessageInstance {
        Object message;
        Class<?> messageClass;
        Class<?> messageTypeClass;
        String content = "Hola";
        long timestamp = 1719950000000L;
        String sender = "Juan";
        Object type;

        @BeforeEach
        void setUp() {
            try {
                messageTypeClass = Class.forName("com.aroldev.messagingnetwork.Enum.MessageType");
                type = Enum.valueOf((Class<Enum>) messageTypeClass, "TEXT");

                messageClass = Class.forName("com.aroldev.messagingnetwork.Message");

                Constructor<?> constructor = messageClass.getConstructor(
                        String.class, long.class, messageTypeClass, String.class
                );

                message = constructor.newInstance(content, timestamp, type, sender);
            } catch (ClassNotFoundException e) {
                fail("Message or MessageType class not found: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                fail("Constructor not implemented: " + e.getMessage());
            } catch (Exception e) {
                fail("Unexpected error during setup: " + e.getMessage());
            }
        }

        @Test
        public void testConstructorAndGetters() {
            try {
                Method getContent = messageClass.getMethod("getContent");
                Method getType = messageClass.getMethod("getType");
                Method getSender = messageClass.getMethod("getSender");

                assertEquals(content, getContent.invoke(message));
                assertEquals(type, getType.invoke(message));
                assertEquals(sender, getSender.invoke(message));
            } catch (NoSuchMethodException e) {
                fail("Expected method or constructor not found: " + e.getMessage());
            } catch (Exception e) {
                fail("Unexpected error during reflection test: " + e.getMessage());
            }
        }

        @Test
        public void testSetType() {
            try {
                Method setType = messageClass.getMethod("setType", messageTypeClass);
                Method getType = messageClass.getMethod("getType");
                setType.invoke(message, Enum.valueOf((Class<Enum>) messageTypeClass, "IMAGE"));
                assertEquals("IMAGE", getType.invoke(message).toString());
            } catch (NoSuchMethodException e) {
                fail("Expected method not found: " + e.getMessage());
            } catch (InvocationTargetException e) {
                fail("Error invoking method: " + e.getCause().getMessage());
            } catch (IllegalAccessException e) {
                fail("Method access error: " + e.getMessage());
            }

        }

        @Test
        public void testFormat() {
            try {
                Method formatMethod = messageClass.getMethod("formatDate");
                String formatted = (String) formatMethod.invoke(message);
                assertNotNull(formatted);

                System.out.println("Formatted message: " + formatted);
                assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));

            } catch (NoSuchMethodException e) {
                fail("Expected method not found: " + e.getMessage());
            } catch (Exception e) {
                fail("Unexpected error during reflection test: " + e.getMessage());
            }
        }
    }
}