package com.danielmorales.validatorx.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class that caches reflection-based metadata about fields and their annotations.
 * This helps avoid repeated expensive reflection operations by storing metadata in a cache.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * List<ReflectionCache.FieldAnnotations> annotations = ReflectionCache.getFieldAnnotations(User.class);
 * for (ReflectionCache.FieldAnnotations fieldAnnotations : annotations) {
 *     System.out.println(fieldAnnotations.getField().getName());
 * }
 * }
 * </pre>
 *
 * @author Daniel Morales
 */
public class ReflectionCache {

    private static final Map<Class<?>, List<FieldAnnotations>> cache = new ConcurrentHashMap<>();

    /**
     * Retrieves the list of field annotations for a given class, computing them if necessary.
     *
     * @param clazz the class to analyze
     * @return a list of {@code FieldAnnotations} representing fields and their annotations
     */
    public static List<FieldAnnotations> getFieldAnnotations(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, ReflectionCache::scanClass);
    }

    /**
     * Scans a class for fields and their annotations.
     *
     * @param clazz the class to scan
     * @return a list of {@code FieldAnnotations} representing fields and their annotations
     */
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

    /**
     * Represents metadata about a field and its associated annotations.
     */
    public static class FieldAnnotations {
        private final Field field;
        private final Annotation[] annotations;

        /**
         * Constructs a {@code FieldAnnotations} object.
         *
         * @param field the field being described
         * @param annotations the annotations present on the field
         */
        public FieldAnnotations(Field field, Annotation[] annotations) {
            this.field = field;
            this.annotations = annotations;
        }

        /**
         * @return the field associated with this metadata
         */
        public Field getField() {
            return field;
        }

        /**
         * @return the annotations present on the field
         */
        public Annotation[] getAnnotations() {
            return annotations;
        }
    }
}