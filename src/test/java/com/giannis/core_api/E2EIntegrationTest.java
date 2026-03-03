package com.giannis.core_api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class E2EIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    // Φτιάχνουμε τον ObjectMapper χειροκίνητα
    private ObjectMapper objectMapper = new ObjectMapper();

    private static String jwtToken;
    private static Long productId;
    private static Long userId;
    private static final String TEST_EMAIL = "admin_test@example.com";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // Ενεργοποιούμε το Spring Security στα tests!
                .build();
    }

    @Test
    @Order(1)
    void shouldRegisterNewUser() throws Exception {
        Map<String, String> registerDto = new HashMap<>();
        registerDto.put("email", TEST_EMAIL);
        registerDto.put("password", "123456");
        registerDto.put("firstName", "Giannis");
        registerDto.put("lastName", "Test");
        registerDto.put("role", "ADMIN");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Order(2)
    void shouldLoginAndGetToken() throws Exception {
        Map<String, String> loginDto = new HashMap<>();
        loginDto.put("email", TEST_EMAIL);
        loginDto.put("password", "123456");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").exists())
               .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Map<String, String> responseMap = objectMapper.readValue(
                responseString, 
                new TypeReference<Map<String, String>>() {}
        );
        jwtToken = responseMap.get("token");
    }

    @Test
    @Order(3)
    void shouldCreateProduct() throws Exception {
        Map<String, Object> productDto = new HashMap<>();
        productDto.put("name", "Test Laptop");
        productDto.put("description", "A powerful laptop");
        productDto.put("price", 1200.50);
        productDto.put("active", true);

        MvcResult result = mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
               .andExpect(status().isCreated()) // Διορθώθηκε σε 201 Created
               .andExpect(jsonPath("$.id").exists())
               .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(
                responseString, 
                new TypeReference<Map<String, Object>>() {}
        );
        productId = ((Number) responseMap.get("id")).longValue();
    }

    @Test
    @Order(4)
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products")
                .header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @Order(5)
    void shouldGetActiveProducts() throws Exception {
        mockMvc.perform(get("/api/products/active")
                .header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(6)
    void shouldUpdateProduct() throws Exception {
        Map<String, Object> updateDto = new HashMap<>();
        updateDto.put("name", "Test Laptop Updated");
        updateDto.put("price", 999.99);
        updateDto.put("active", false);

        mockMvc.perform(put("/api/products/" + productId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Test Laptop Updated"));
    }

    @Test
    @Order(7)
    void shouldGetUserByEmail() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/email/" + TEST_EMAIL)
                .header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(
                responseString, 
                new TypeReference<Map<String, Object>>() {}
        );
        userId = ((Number) responseMap.get("id")).longValue();
    }

    @Test
    @Order(8)
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(9)
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/" + productId)
                .header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isNoContent()); 
    }

    @Test
    @Order(10)
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/" + userId)
                .header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isNoContent()); 
    }

    @Test
    @Order(11)
    void shouldFailToAccessProtectedEndpointWithoutToken() throws Exception {
        // Χτυπάμε το /api/users που είναι κλειδωμένο (απαιτεί token)
        mockMvc.perform(get("/api/users"))
               .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(12)
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        Map<String, String> loginDto = new HashMap<>();
        loginDto.put("email", "nonexistent_user@example.com");
        loginDto.put("password", "wrong_password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
               .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(13)
    void shouldFailRegisterWithMissingData() throws Exception {
        Map<String, String> registerDto = new HashMap<>();
        registerDto.put("firstName", "Giannis");
        registerDto.put("lastName", "Test");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
               .andExpect(status().is4xxClientError());
    }
}