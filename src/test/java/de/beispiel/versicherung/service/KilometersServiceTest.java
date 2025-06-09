package de.beispiel.versicherung.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link KilometersService}.
 *
 * Verifies the factor calculation based on different kilometer ranges,
 * including lower, mid, high ranges, values above the maximum,
 * and error cases such as negative or out-of-bound inputs.
 */
class KilometersServiceTest {

    private final KilometersService service = new KilometersService();

    @Test
    void testLowerRangeKilometers() {
        assertEquals(0.5, service.getFactor(0));
        assertEquals(0.5, service.getFactor(5000));
    }

    @Test
    void testMidRangeKilometers() {
        assertEquals(1.0, service.getFactor(5001));
        assertEquals(1.0, service.getFactor(10000));
    }

    @Test
    void testHighRangeKilometers() {
        assertEquals(1.5, service.getFactor(10001));
        assertEquals(1.5, service.getFactor(20000));
    }

    @Test
    void testAboveMax() {
        assertEquals(2.0, service.getFactor(20001));
        assertEquals(2.0, service.getFactor(Integer.MAX_VALUE));
    }

    @Test
    void testError() {
        assertEquals(0.0, service.getFactor(-1));
        assertEquals(0.0, service.getFactor(Integer.MAX_VALUE + 1));
    }

}
