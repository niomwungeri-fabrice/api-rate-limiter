package com.oltranz.apiratelimiter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientPlanIntegrationTests {
    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        ClientPlanIntegrationTests.redisTemplate = redisTemplate;
    }

    @BeforeAll
    public void cleanRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
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
                .content("{\"clientId\": \"" + clientIdUser2 + "\", \"plan\": \"SUBSCRIPTION_BASIC\"}"));

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
}
