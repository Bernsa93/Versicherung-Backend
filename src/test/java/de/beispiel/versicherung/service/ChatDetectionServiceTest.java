package de.beispiel.versicherung.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link ChatDetectionService}.
 *
 * Tests the vehicle class detection for known types (SUV, limousine, etc.)
 * and unknown types (fallback).
 */
class ChatDetectionServiceTest {

    private static final String[] EXPECTED_CLASSES = {
        "SUV", "LIMOUSINE", "KOMBI", "SPORTWAGEN", "KLEINVAN",
        "CROSSOVER", "MIKROAUTO", "ANDERS"
    };

    private final ChatDetectionService service = new ChatDetectionService("test-api-key");

    @Test
    void testLimousine() {
        assertEquals("LIMOUSINE", service.sanitizeClass(" LIMOUSINE "));
    }

    @Test
    void testKombi() {
        assertEquals("KOMBI", service.sanitizeClass(" KOMBI "));
    }

    @Test
    void testSportwagen() {
        assertEquals("SPORTWAGEN", service.sanitizeClass(" SPORTWAGEN "));
    }

    @Test
    void testKleinwagen() {
        assertEquals("KLEINVAN", service.sanitizeClass(" KLEINVAN "));
    }

    @Test
    void testCrossover() {
        assertEquals("CROSSOVER", service.sanitizeClass(" CROSSOVER "));
    }

    @Test
    void testMikroauto() {
        assertEquals("MIKROAUTO", service.sanitizeClass(" MIKROAUTO "));
    }

    @Test
    void testFehler() {
        assertEquals("ANDERS", service.sanitizeClass(" UNKNOWN "));
    }

    @Test
    void testZahl() {
        assertEquals("ANDERS", service.sanitizeClass(" 123 "));
    }

    @Test
    void testNullInput() {
        assertEquals("ANDERS", service.sanitizeClass(null));
    }

    @Test
    void testEmptyInput() {
        assertEquals("ANDERS", service.sanitizeClass(""));
    }
}
