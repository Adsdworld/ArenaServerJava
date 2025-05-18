package com.arena;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MainTest extends UTF8Base {
    @Test
    void mainRunWithoutException() {
        assertDoesNotThrow(() -> Main.main(new String[]{}), "Main method should run without throwing an exception");
    }
}
