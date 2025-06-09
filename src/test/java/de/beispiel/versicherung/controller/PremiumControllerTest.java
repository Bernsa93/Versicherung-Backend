package de.beispiel.versicherung.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.beispiel.versicherung.dto.PremiumCalculationRequest;
import de.beispiel.versicherung.dto.PremiumCalculationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Integration tests for {@link de.beispiel.versicherung.controller.PremiumController}.
 *
 * Tests the /api/premium/calculate endpoint with valid and fallback inputs,
 * verifying correct calculation of premium factors and total premium.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PremiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCalculate() throws Exception {
        PremiumCalculationRequest request = new PremiumCalculationRequest("suv", 10000, "50226");

        MvcResult result =  mockMvc.perform(post("/api/premium/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        String json = result.getResponse().getContentAsString();

        PremiumCalculationResponse response = objectMapper.readValue(json, PremiumCalculationResponse.class);

        assertEquals(1.0, response.kmFactor(), 0.01);
        assertEquals(1.5, response.vehicleFactor(), 0.01);
        assertEquals(1.05, response.regionFactor(), 0.01);
        assertEquals(1.0 * 1.5 * 1.05, response.calculatedPremium(), 0.001);
    }

    @Test
    void testCalculateFallback() throws Exception {
        PremiumCalculationRequest request = new PremiumCalculationRequest("lkw", -1, "Fehler");

        MvcResult result =  mockMvc.perform(post("/api/premium/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        String json = result.getResponse().getContentAsString();

        PremiumCalculationResponse response = objectMapper.readValue(json, PremiumCalculationResponse.class);

        assertEquals(0, response.kmFactor(), 0.01);
        assertEquals(0, response.vehicleFactor(), 0.01);
        assertEquals(0, response.regionFactor(), 0.01);
        assertEquals(0, response.calculatedPremium(), 0.01);
    }
}
