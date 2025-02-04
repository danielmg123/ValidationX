package com.danielmorales.validatorx.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class ReflectionCache {

    // Map<Class, List<FieldAnnotations>>
    private static final Map<Class<?>, List<FieldAnnotations>> cache = new HashMap<>();

    public static List<FieldAnnotations> getFieldAnnotations(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, ReflectionCache::scanClass);
    }

    private static List<FieldAnnotations> scanClass(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<FieldAnnotations> result = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            result.add(new FieldAnnotations(field, annotations));
        }
        return result;
    }

    public static class FieldAnnotations {
        private final Field field;
        private final Annotation[] annotations;

        public FieldAnnotations(Field field, Annotation[] annotations) {
            this.field = field;
            this.annotations = annotations;
        }

        public Field getField() {
            return field;
        }

        public Annotation[] getAnnotations() {
            return annotations;
        }
    }
}