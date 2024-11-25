package it.gov.pagopa.wispconverter.technicalsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.wispconverter.technicalsupport.repository.RTRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventDataExplorerRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventExperimentalRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OpenApiGenerationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReEventRepository reEventRepository;

    @MockBean
    private ReEventExperimentalRepository reEventExperimentalRepository;

    @MockBean
    private ReEventDataExplorerRepository reEventDataExplorerRepository;

    @MockBean
    private RTRepository rtRepository;

    @Test
    void swaggerSpringPlugin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andDo(
                        (result) -> {
                            assertNotNull(result);
                            assertNotNull(result.getResponse());
                            final String content = result.getResponse().getContentAsString();
                            assertFalse(content.isBlank());
                            assertFalse(content.contains("${"), "Generated swagger contains placeholders");
                            Object swagger =
                                    objectMapper.readValue(result.getResponse().getContentAsString(), Object.class);
                            String formatted =
                                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(swagger);
                            Path basePath = Paths.get("openapi/");
                            Files.createDirectories(basePath);
                            Files.write(basePath.resolve("openapi.json"), formatted.getBytes());
                        });
    }
}
