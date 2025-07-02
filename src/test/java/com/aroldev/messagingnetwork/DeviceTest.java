package com.aroldev.messagingnetwork;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeviceTest {

  final static String DEVICE_CLASS = "com.aroldev.messagingnetwork.Device";
  static Class<?> deviceClass;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    deviceClass = Class.forName(DEVICE_CLASS);
  }

  @Order(1)
  @Test
  void constructorExists() {
    try {
      deviceClass.getConstructor(String.class, int.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(2)
  @Test
  void testDeviceFieldsExist() throws Exception {
    Field name = deviceClass.getDeclaredField("name");
    assertEquals(String.class, name.getType());

    Field id = deviceClass.getDeclaredField("id");
    assertEquals(int.class, id.getType());

    Field darkMode = deviceClass.getDeclaredField("darkMode");
    assertEquals(boolean.class, darkMode.getType());
  }

  @Order(3)
  @Test
  void testAbstractMethodsDeclared() throws Exception {
    Class<?> messageClass = Class.forName("com.aroldev.messagingnetwork.Message");

    Method sendMessageMethod = deviceClass.getDeclaredMethod("sendMessage", messageClass);
    assertTrue(Modifier.isAbstract(sendMessageMethod.getModifiers()), "sendMessage method should be abstract");

    Method receiveMessageMethod = deviceClass.getDeclaredMethod("receiveMessage", messageClass);
    assertTrue(Modifier.isAbstract(receiveMessageMethod.getModifiers()), "receiveMessage method should be abstract");
  }

  @Order(4)
  @Test
  void testDeviceGettersAndSetters() {
    try {
      // Dynamically create a subclass
      Class<?> dynamicSubclass = new ByteBuddy()
          .subclass(deviceClass)
          .method(ElementMatchers.named("getName"))
          .intercept(SuperMethodCall.INSTANCE)
          .method(ElementMatchers.named("setName"))
          .intercept(SuperMethodCall.INSTANCE)
          .make()
          .load(getClass().getClassLoader())
          .getLoaded();

      // Instantiate the subclass
      Object deviceInstance = dynamicSubclass
          .getDeclaredConstructor(String.class, int.class)
          .newInstance("Phone", 1);

      // Call a concrete method
      Method getNameMethod = dynamicSubclass.getMethod("getName");
      assertEquals("Phone", getNameMethod.invoke(deviceInstance));

      Method setNameMethod = dynamicSubclass.getDeclaredMethod("setName", String.class);
      setNameMethod.invoke(deviceInstance, "Not a phone");
      assertEquals("Not a phone", getNameMethod.invoke(deviceInstance));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}