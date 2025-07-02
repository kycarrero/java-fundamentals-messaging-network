package com.aroldev.messagingnetwork;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.aroldev.messagingnetwork.TestUtilities.newMessage;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PhoneTest {

  final static String PHONE_CLASS_NAME = "com.aroldev.messagingnetwork.Phone";
  static Class<?> phoneClass;
  Object phone;

  @BeforeAll
  static void setUpBeforeAll() {
    try {
      phoneClass = Class.forName(PHONE_CLASS_NAME);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeEach
  void setUp() {
    try {
      Constructor<?> constructor = phoneClass.getConstructor(String.class, int.class, int.class);
      phone = constructor.newInstance("Mario", 1, 5);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(1)
  @Test
  void testPhoneCreation() {
    try {
      Method getName = phone.getClass().getMethod("getName");
      assertEquals("Mario", getName.invoke(phone));

      Method getId = phone.getClass().getMethod("getId");
      assertEquals(1, getId.invoke(phone));

      Method getCapacity = phone.getClass().getMethod("getCapacity");
      assertEquals(5, getCapacity.invoke(phone));
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(2)
  @Test
  void testSendAndStoreMessageInHistory()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
    Class<?> messageClass = Class.forName("com.aroldev.messagingnetwork.Message");

    Object msg = newMessage("Ciao, Luigi!");
    Method sendMessage = phoneClass.getMethod("sendMessage", messageClass);
    sendMessage.invoke(phone, msg);

    Method getMessages = phoneClass.getMethod("getMessages");
    Object[] messages = (Object[]) getMessages.invoke(phone);

    Method getContent = messageClass.getMethod("getContent");
    String messageText = getContent.invoke(messages[0]).toString();

    assertEquals("Ciao, Luigi!", messageText);
  }

  @Order(3)
  @Test
  void testCyclicMessageHistory() throws NoSuchMethodException, InvocationTargetException, InstantiationException,
      IllegalAccessException, ClassNotFoundException {
    Class<?> messageClass = Class.forName("com.aroldev.messagingnetwork.Message");

    Constructor<?> constructor = phoneClass.getConstructor(String.class, int.class, int.class);
    Method sendMessage = phoneClass.getMethod("sendMessage", messageClass);

    phone = constructor.newInstance("Mario", 1, 2);

    Object msg;
    msg = newMessage("Ciao, Luigi!");
    sendMessage.invoke(phone, msg);

    msg = newMessage("It's me, Mario!");
    sendMessage.invoke(phone, msg);

    msg = newMessage("Wahoo!");
    sendMessage.invoke(phone, msg);

    Method getMessages = phoneClass.getMethod("getMessages");
    Method getContent = messageClass.getMethod("getContent");
    Object[] messages = (Object[]) getMessages.invoke(phone);

    assertEquals("Wahoo!", getContent.invoke(messages[0]).toString(), "El historial de mensajes debe ser cíclico.");
    assertEquals("It's me, Mario!", getContent.invoke(messages[1]).toString(),
        "El historial de mensajes debe ser cíclico.");

    assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      System.out.println(messages[2]);
    }, "El array de mensajes no debe ser mayor a la capacidad asignada.");
  }

  @Order(4)
  @Test
  void testIgnoreEmptyMessages()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    try {
      Class<?> messageClass = Class.forName("com.aroldev.messagingnetwork.Message");

      Object msg = newMessage(" ");
      Method sendMessage = phoneClass.getMethod("sendMessage", messageClass);

      sendMessage.invoke(phone, msg);

      Method getMessages = phoneClass.getMethod("getMessages");
      Object[] messages = (Object[]) getMessages.invoke(phone);

      assertNull(messages[0], "El mensaje con texto vacio debe ser ignorado.");
    } catch (Exception e) {
      Throwable cause = e.getCause();
      if (cause == null) {
        throw e;
      }
      String exceptionClassName = cause.getClass().getName();
      if (exceptionClassName.contains("InvalidMessageException")) {
        return;
      }
      throw e;
    }
  }

  @Order(5)
  @Test
  void testCheckForSystemMessage()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> messageClass = Class.forName("com.aroldev.messagingnetwork.Message");

    Object msg;
    Method sendMessage = phoneClass.getMethod("sendMessage", messageClass);
    Method getMessages = phoneClass.getMethod("getMessages");
    Object[] messages;

    msg = newMessage("ERROR", "Mario", "SYSTEM");
    sendMessage.invoke(phone, msg);

    messages = (Object[]) getMessages.invoke(phone);
    assertNull(messages[0], "El mensaje tipo SYSTEM que no es enviado por SYSTEM debe ser ignorado.");

    msg = newMessage("ERROR", "SYSTEM", "SYSTEM");
    sendMessage.invoke(phone, msg);

    messages = (Object[]) getMessages.invoke(phone);
    assertNotNull(messages[0], "El mensaje tipo SYSTEM que es enviado por SYSTEM debe ser añadido.");
  }

}