package com.tuda.integration;

import com.tuda.AbstractIntegrationTest;
import com.tuda.data.entity.*;
import com.tuda.data.enums.EventStatus;
import com.tuda.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private RequestRepository requestRepository;

    private AppUser testUser;
    private Event testEvent;
    private Organization testOrganization;
    private Photo testPhoto;
    private Request testRequest;

    @BeforeEach
    void setup() {
        testUser = new AppUser();
        testUser.setLogin("testuser");
        testUser.setName("Test User");
        userRepository.save(testUser);

        testOrganization = new Organization();
        testOrganization.setName("Test Org");
        organizationRepository.save(testOrganization);

        testPhoto = new Photo(UUID.randomUUID(), "test_photo.jpg");
        photoRepository.save(testPhoto);

        testEvent = Event.builder()
                .title("Test Event")
                .description("Test Description")
                .eventStatus(EventStatus.WILL)
                .organization(testOrganization)
                .photo(testPhoto)
                .build();
        eventRepository.save(testEvent);

        testRequest = new Request();
        testRequest.setEvent(testEvent);
        testRequest.setAppUser(testUser);
        testRequest.setStatus(false);
        requestRepository.save(testRequest);
    }

    @AfterEach
    void cleanup() {
        requestRepository.deleteAll();
        eventRepository.deleteAll();
        photoRepository.deleteAll();
        organizationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser")
    void getAllEventRequests_shouldReturnRequests() throws Exception {
        mockMvc.perform(get("/request/getAll")
                        .param("eventId", String.valueOf(testEvent.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].status").value(false));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getAllEventRequests_emptyResult_shouldReturnEmptyArray() throws Exception {
        requestRepository.deleteAll();

        mockMvc.perform(get("/request/getAll")
                        .param("eventId", String.valueOf(testEvent.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result").isEmpty());
    }

    @Test
    @WithMockUser(username = "testuser")
    void addRequest_shouldCreateRequest() throws Exception {
        mockMvc.perform(post("/request/add")
                        .param("eventId", String.valueOf(testEvent.getId()))
                        .param("userLogin", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.status").value(false));
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteRequest_shouldDeleteRequest() throws Exception {
        mockMvc.perform(delete("/request/refuseToVolunteering")
                        .param("eventId", String.valueOf(testEvent.getId()))
                        .param("userLogin", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Volunteering request successfully deletes"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void rejectRequest_shouldUpdateRequestStatus() throws Exception {
        mockMvc.perform(post("/request/rejectVolunteerForEventById")
                        .param("eventId", String.valueOf(testEvent.getId()))
                        .param("userLogin", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.status").value(false));
    }
}
