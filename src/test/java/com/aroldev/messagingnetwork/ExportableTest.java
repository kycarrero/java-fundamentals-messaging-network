package com.aroldev.messagingnetwork;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

class ExportableTests {
    static Class<?> exportable;

    @BeforeAll
    static void setupAll() throws ClassNotFoundException {
        exportable = Class.forName("com.aroldev.messagingnetwork.Interfaces.Exportable");
    }

    @Test
    public void testInterface() {
        assertTrue(exportable.isInterface(), "Exportable should be an interface");
    }

    @Test
    public void testDeclaredExport() throws NoSuchMethodException {
        Method exportMethod = exportable.getMethod("export");
        int modifiers = exportMethod.getModifiers();

        assertTrue(Modifier.isPublic(modifiers), "Exportable should be public");
        assertFalse(Modifier.isStatic(modifiers), "Export method should not be static");
    }

    @Test
    public void testDeviceImplementsExportableWithListOfString() throws Exception {
        Class<?> phoneClass = Class.forName("com.aroldev.messagingnetwork.Phone");
        Class<?> exportableClass = Class.forName("com.aroldev.messagingnetwork.Interfaces.Exportable");

        boolean found = false;
        for (Type iface : phoneClass.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) iface;
                if (pt.getRawType().equals(exportableClass)) {
                    Type typeArg = pt.getActualTypeArguments()[0];
                    assertEquals("java.util.List<java.lang.String>", typeArg.getTypeName(), "Phone debe implementar Exports con el tipo genérico List<String>.");
                    found = true;
                    break;
                }
            }
        }

        assertTrue(found, "Phone should implement Exportable<List<String>>");

    }
}
