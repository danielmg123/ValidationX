package com.danielmorales.validatorx.core;

import com.danielmorales.validatorx.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionCacheTest {

    /**
     * Test class with one field annotated for demonstration.
     */
    static class TestModel {
        @NotNull
        private String annotatedField;
        @SuppressWarnings("unused")
        private int nonAnnotatedField;
    }

    @Test
    void testGetFieldAnnotations_cachesAndReturnsCorrectData() {
        // First call should load from reflection
        List<ReflectionCache.FieldAnnotations> fieldAnnotations1 = 
                ReflectionCache.getFieldAnnotations(TestModel.class);
        // Second call should come from cache
        List<ReflectionCache.FieldAnnotations> fieldAnnotations2 = 
                ReflectionCache.getFieldAnnotations(TestModel.class);

        assertSame(fieldAnnotations1, fieldAnnotations2, 
                "Subsequent calls should return the same cached list instance");

        // Verify we have the right number of fields
        // (TestModel has 2 declared fields)
        assertEquals(2, fieldAnnotations1.size(), 
                "Expected 2 FieldAnnotations entries for TestModel");

        // Check which field has the @NotNull annotation
        boolean foundAnnotatedField = false;
        boolean foundNonAnnotatedField = false;

        for (ReflectionCache.FieldAnnotations fa : fieldAnnotations1) {
            FieldReflectionChecker: {
                if ("annotatedField".equals(fa.getField().getName())) {
                    foundAnnotatedField = true;
                    Annotation[] annotations = fa.getAnnotations();
                    assertEquals(1, annotations.length, 
                            "annotatedField should have exactly one annotation");
                    assertTrue(annotations[0] instanceof NotNull, 
                            "annotatedField should be annotated with @NotNull");
                    break FieldReflectionChecker;
                }
                if ("nonAnnotatedField".equals(fa.getField().getName())) {
                    foundNonAnnotatedField = true;
                    Annotation[] annotations = fa.getAnnotations();
                    assertEquals(0, annotations.length, 
                            "nonAnnotatedField should have no annotations");
                }
            }
        }

        assertTrue(foundAnnotatedField, "Should have found the annotatedField");
        assertTrue(foundNonAnnotatedField, "Should have found the nonAnnotatedField");
    }
}