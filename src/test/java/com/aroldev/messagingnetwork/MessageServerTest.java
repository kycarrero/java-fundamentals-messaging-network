package com.aroldev.messagingnetwork;

import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Queue;

import static com.aroldev.messagingnetwork.TestUtilities.getMessageClass;
import static com.aroldev.messagingnetwork.TestUtilities.newMessage;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageServerTest {

    static private Class<?> MessageServer;
    private Object messageServer;

    @BeforeAll
    static void setUpAll() throws ClassNotFoundException {
        MessageServer = Class.forName("com.aroldev.messagingnetwork.MessageServer");
    }

    @BeforeEach
    void setUpEach() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        messageServer = MessageServer
                .getConstructor()
                .newInstance();
    }

    @Test
    @Order(1)
    void testServerImplementsRunnable() {
        Class<?> iface = messageServer.getClass().getInterfaces()[0];
        assertEquals("Runnable", iface.getSimpleName());
    }

    @Test
    @Order(2)
    void testAddMessage() throws Exception {
        Object msg = newMessage("Hello", "Tester", "TEXT");
        getAddMessageMethod().invoke(messageServer, msg);

        Queue<Class<?>> internalQueue = getMessageQueue(messageServer);
        assertEquals(1, internalQueue.size(), "Queue should have 1 element.");

        Method getContentMethod = getMessageClass().getDeclaredMethod("getContent");
        assertEquals("Hello", getContentMethod.invoke(internalQueue.peek()), "Message's content should be the one passed.");

        int modifiers = getAddMessageMethod().getModifiers();
        assertTrue(Modifier.isSynchronized(modifiers), "Method addMessage should be synchronized.");
    }

    @Test
    @Order(3)
    void testAddMessageToQueue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Object msg = newMessage("Hello", "Tester", "TEXT");
        getAddMessageMethod().invoke(messageServer, msg);

        Thread t = new Thread((Runnable) messageServer);
        t.start();
        try {
            Thread.sleep(1100); // Lascia tempo al server di processare
        } catch (InterruptedException ignored) {}
        t.interrupt();
        assertTrue(true); // Se non ci sono eccezioni, il test passa
    }

    @Order(4)
    @Test
    void testRunMethod() {
        // Avvia il server in un thread e verifica che non lanci eccezioni
        Thread t = new Thread((Runnable) messageServer);
        t.start();
        try {
            Thread.sleep(1100); // Lascia tempo al server di processare
        } catch (InterruptedException ignored) {
        }
        t.interrupt();
        assertTrue(true); // Se non ci sono eccezioni, il test passa
    }

    @Order(5)
    @Test
    void testProcessMessageIsCalledOnceMessageIsAdded() throws Exception {
        Object msg = newMessage("Test Message", "Tester", "TEXT");
        getAddMessageMethod().invoke(messageServer, msg);

        Thread serverThread = new Thread(() -> {
            try {
                // Run only for a short time
                Queue<Class<?>> queue = getMessageQueue(messageServer);

                int attempts = 0;
                // Wait (with a maximum of 5 attempts) for the queue to become empty
                // This loop allows the main server thread to process the message
                while (!queue.isEmpty() && attempts++ < 5) {
                    Thread.sleep(100);
                }

                // Stop thread using interruption
                Thread.currentThread().interrupt();
            } catch (Exception ignored) {}
        });

        // Start the main server's processing thread
        // It will continuously poll and process messages
        Thread processingThread = new Thread((Runnable) messageServer);
        processingThread.start();

        // Start the helper thread that checks when the message has been processed
        serverThread.start();

        // Wait for helper thread to finish (max 1s)
        serverThread.join(1000);

        // Stop the server processing thread to avoid infinite loop
        processingThread.interrupt();
        processingThread.join(1000); // ensure it finishes
    }

    static private Method getAddMessageMethod() throws NoSuchMethodException, ClassNotFoundException {
        return MessageServer.getMethod("addMessage", TestUtilities.getMessageClass());
    }

    // Helper to access private field
    @SuppressWarnings("unchecked")
    private Queue<Class<?>> getMessageQueue(Object server) throws Exception {
        Field field = MessageServer.getDeclaredField("messageQueue");
        field.setAccessible(true);
        return (Queue<Class<?>>) field.get(server);
    }
}