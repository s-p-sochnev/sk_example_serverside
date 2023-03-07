package org.spsochnev.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test requires Docker environment
 * Reading properties file in static initializer block,
 * since @Value is not available in static context
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {RestIntegrationTests.Initializer.class})
public class RestIntegrationTests {

    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            ClassLoader classLoader = RestIntegrationTests.class.getClassLoader();
            InputStream applicationPropertiesStream = classLoader.getResourceAsStream("application.properties");
            properties.load(applicationPropertiesStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse(properties.getProperty("docker.postgresql.image.name")).
                    withTag(properties.getProperty("docker.postgresql.image.tag")))
            .withDatabaseName(properties.getProperty("sk.example.database.name"))
            .withUsername(properties.getProperty("sk.example.user.name"))
            .withPassword(properties.getProperty("sk.example.user.password"))
            .withInitScript(properties.getProperty("docker.postgresql.init.script"));

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresContainer.getUsername(),
                    "spring.datasource.password=" + postgresContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Increment current value")
    public void testModify() throws Exception {
        mockMvc.perform(post("/modify").
                        contentType(MediaType.APPLICATION_JSON).
                        content("{\"id\": 1, \"add\": 2}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"current\":2}"));
    }

    @Test
    @DisplayName("Return error when current value not found")
    public void testModifyFails() throws Exception {
        mockMvc.perform(post("/modify").
                        contentType(MediaType.APPLICATION_JSON).
                        content("{\"id\": 2, \"add\": 2}"))
                .andDo(print())
                .andExpect(status().isIAmATeapot());
    }
}
