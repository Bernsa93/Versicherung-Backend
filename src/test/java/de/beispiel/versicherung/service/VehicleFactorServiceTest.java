package de.beispiel.versicherung.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link VehicleFactorService}.
 *
 * Tests the factor calculation for different vehicle types,
 * including known types (SUV, sportscar, small car) and unknown types (fallback).
 */
class VehicleFactorServiceTest {

    private final VehicleFactorService service = new VehicleFactorService();

    @Test
    void testSuv() {
        assertEquals(1.5, service.getFactor("suv"));
    }

    @Test
    void testSportwagen() {
        assertEquals(2.0, service.getFactor("sportwagen"));
    }

    @Test
    void testSmallCar() {
        assertEquals(1.0, service.getFactor("kleinwagen"));
    }

    @Test
    void testFallback() {
        assertEquals(0.0, service.getFactor("lkw"));
        assertEquals(0.0, service.getFactor("123"));
    }

}