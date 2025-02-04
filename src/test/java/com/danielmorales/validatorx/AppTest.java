package com.danielmorales.validatorx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    public void testAddition() {
        int expected = 4;
        int actual = 2 + 2;
        assertEquals(expected, actual, "2 + 2 should equal 4");
    }
}
