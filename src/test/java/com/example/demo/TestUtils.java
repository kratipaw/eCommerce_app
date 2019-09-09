package com.example.demo;

import org.mockito.internal.util.reflection.FieldSetter;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        try {
            FieldSetter.setField(target, target.getClass().getDeclaredField(fieldName), toInject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
