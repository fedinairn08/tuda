package com.tuda.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuda.AbstractIntegrationTest;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Organization;
import com.tuda.dto.request.OrganizationRequestDTO;
import com.tuda.repository.OrganizationRepository;
import com.tuda.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrganizationControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    private Organization testOrganization;
    private AppUser testUser;

    @BeforeEach
    void setup() {
        testUser = new AppUser();
        testUser.setLogin("testuser");
        testUser.setName("Test User");
        userRepository.save(testUser);

        testOrganization = new Organization();
        testOrganization.setName("Test Org");
        organizationRepository.save(testOrganization);
    }

    @AfterEach
    void cleanup() {
        organizationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser")
    void addOrganization_shouldCreateOrganization() throws Exception {
        OrganizationRequestDTO requestDTO = new OrganizationRequestDTO();
        requestDTO.setName("New Organization");

        mockMvc.perform(post("/organization/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value("New Organization"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateOrganization_shouldUpdateOrganization() throws Exception {
        OrganizationRequestDTO requestDTO = new OrganizationRequestDTO();
        requestDTO.setName("Updated Org");

        mockMvc.perform(post("/organization/update")
                        .param("organizationId", String.valueOf(testOrganization.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value("Updated Org"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateOrganization_notFound_shouldReturn404() throws Exception {
        OrganizationRequestDTO requestDTO = new OrganizationRequestDTO();
        requestDTO.setName("Non-existent Org");

        mockMvc.perform(post("/organization/update")
                        .param("organizationId", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }
}
