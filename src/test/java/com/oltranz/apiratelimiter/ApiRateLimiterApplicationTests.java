package com.oltranz.apiratelimiter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiRateLimiterApplicationTests {

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        ApiRateLimiterApplicationTests.redisTemplate = redisTemplate;
    }

    @BeforeAll
    public void cleanRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }


    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testSuccessfulRegistration() throws Exception {
        String jsonPayload = "{\"name\": \"OLTRANZ LIMITED\"}";

        mockMvc.perform(post("/clients/register")
                        .contentType("application/json")
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.clientId").exists())
                .andExpect(jsonPath("$.data.name").value("OLTRANZ LIMITED"))
                .andExpect(jsonPath("$.data.plan").value("SUBSCRIPTION_BASIC"))
                .andExpect(jsonPath("$.message").value("Registration was done successfully"));
    }

    @Test
    @Order(2)
    public void testRateLimits() throws Exception {
        // Register User 1 and get clientId
        MvcResult resultUser1 = mockMvc.perform(post("/clients/register")
                        .contentType("application/json")
                        .content("{\"name\": \"User One\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String responseUser1 = resultUser1.getResponse().getContentAsString();
        String clientIdUser1 = getClientIdFromResponse(responseUser1);

        // Register User 2 and get clientId
        MvcResult resultUser2 = mockMvc.perform(post("/clients/register")
                        .contentType("application/json")
                        .content("{\"name\": \"User Two\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String responseUser2 = resultUser2.getResponse().getContentAsString();
        String clientIdUser2 = getClientIdFromResponse(responseUser2);

        // Upgrade User 2 to Basic Plan
        mockMvc.perform(post("/rate/upgrade")
                .contentType("application/json")
                .content("{\"clientId\": \"" + clientIdUser2 + "\", \"plan\": \"SUBSCRIPTION_PRO\"}"));

        // Test Rate Limit for User 1 (Free Plan)
        for (int i = 0; i < 6; i++) {
            mockMvc.perform(post("/notifications/notify")
                    .header("client-id", clientIdUser1)
                    .contentType("application/json")
                    .content("{\"title\":\"Test\",\"body\":\"Message\"}"));
        }
        mockMvc.perform(post("/notifications/notify")
                        .header("client-id", clientIdUser1)
                        .contentType("application/json")
                        .content("{\"title\":\"Test\",\"body\":\"Message\"}"))
                .andExpect(status().isTooManyRequests());

        // Test Rate Limit for User 2 (Basic Plan)
        for (int i = 0; i < 16; i++) {
            mockMvc.perform(post("/notifications/notify")
                    .header("client-id", clientIdUser2)
                    .contentType("application/json")
                    .content("{\"title\":\"Test\",\"body\":\"Message\"}"));
        }
        mockMvc.perform(post("/notifications/notify")
                        .header("client-id", clientIdUser2)
                        .contentType("application/json")
                        .content("{\"title\":\"Test\",\"body\":\"Message\"}"))
                .andExpect(status().isTooManyRequests());
    }

    private String getClientIdFromResponse(String jsonResponse) throws Exception {
        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
        Map<String, String> data = (Map<String, String>) responseMap.get("data");
        return data.get("clientId");
    }

    @Test
    @Order(3)
    public void testRateLimiting() throws Exception {
        String jsonPayload = "{\"name\": \"OLTRANZ LIMITED\"}";
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(post("/clients/register")
                    .contentType("application/json")
                    .content(jsonPayload));
        }

        mockMvc.perform(post("/clients/register")
                        .contentType("application/json")
                        .content(jsonPayload))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.data").value("maximum of requests per minute is reached. please retry after 1 minute"))
                .andExpect(jsonPath("$.message").value("maximum of requests per minute is reached. please retry after 1 minute"));
    }


}
