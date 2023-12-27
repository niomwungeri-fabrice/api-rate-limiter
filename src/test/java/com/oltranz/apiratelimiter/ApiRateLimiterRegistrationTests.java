package com.oltranz.apiratelimiter;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiRateLimiterRegistrationTests {

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        ApiRateLimiterRegistrationTests.redisTemplate = redisTemplate;
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
                .andExpect(jsonPath("$.data.plan").value("SUBSCRIPTION_FREE"))
                .andExpect(jsonPath("$.message").value("Registration was done successfully"));
    }

    @Test
    @Order(2)
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
                .andExpect(jsonPath("$.data").value("maximum of 100 requests per minute is reached. please retry after 1 minute"))
                .andExpect(jsonPath("$.message").value("maximum of 100 requests per minute is reached. please retry after 1 minute"));
    }

}
