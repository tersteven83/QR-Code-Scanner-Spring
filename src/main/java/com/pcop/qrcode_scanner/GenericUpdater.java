package com.pcop.qrcode_scanner;

import java.lang.reflect.Field;

public class GenericUpdater {

    public static <T> boolean updateIfChanged(T target, T source) {

        boolean updated = false;

        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target objects must not be null");
        }

        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object sourceValue = field.get(source);
                Object targetValue = field.get(target);

                if (sourceValue != null && !sourceValue.equals(targetValue)) {
                    field.set(target, sourceValue);
                    updated = true;
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return updated;

    }

}
