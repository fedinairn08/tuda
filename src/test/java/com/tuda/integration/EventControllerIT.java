package com.tuda.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuda.AbstractIntegrationTest;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Event;
import com.tuda.data.entity.Organization;
import com.tuda.data.entity.Photo;
import com.tuda.data.enums.EventStatus;
import com.tuda.data.enums.UserRole;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.repository.EventRepository;
import com.tuda.repository.OrganizationRepository;
import com.tuda.repository.PhotoRepository;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PhotoRepository photoRepository;

    private AppUser testUser;

    private Event testEvent;

    private Photo testPhoto;

    private Organization testOrganization;

    @BeforeEach
    void setup() {
        testUser = new AppUser();
        testUser.setLogin("testuser");
        testUser.setName("User");
        userRepository.save(testUser);

        testOrganization = new Organization();
        testOrganization.setName("Org");
        organizationRepository.save(testOrganization);
        testUser.setOrganization(testOrganization);

        testPhoto = new Photo(UUID.randomUUID(), "event_photo.jpg");
        photoRepository.save(testPhoto);

        testEvent = Event.builder()
                .title("Initial Event")
                .description("Test Description")
                .eventStatus(EventStatus.WILL)
                .organization(testOrganization)
                .photo(testPhoto)
                .build();
        eventRepository.save(testEvent);
    }

    @AfterEach
    void cleanup() {
        eventRepository.deleteAll();
        organizationRepository.deleteAll();
        userRepository.deleteAll();
        photoRepository.deleteAll();
    }

    @Test
    void getAllEvents_shouldReturnEvents() throws Exception {
        mockMvc.perform(get("/event/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].title").value("Initial Event"));
    }

    @Test
    void getEventById_shouldReturnEvent() throws Exception {
        mockMvc.perform(get("/event/getById/{id}", testEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.title").value("Initial Event"));
    }

    @Test
    void getEventById_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/event/getById/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getEventsByUserId_shouldReturnEvents() throws Exception {
        mockMvc.perform(get("/event/getByUserId/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateEvent_shouldUpdateEvent() throws Exception {
        EventRequestDTO requestDTO = new EventRequestDTO();
        requestDTO.setTitle("Updated Event");
        requestDTO.setDescription("Updated Description");
        requestDTO.setEventStatus(String.valueOf(EventStatus.WILL));
        requestDTO.setOrganizationId(testOrganization.getId());
        requestDTO.setFilename(testPhoto.getFilename());
        requestDTO.setUploadId(testPhoto.getUploadId());

        mockMvc.perform(put("/event/update")
                        .param("id", String.valueOf(testEvent.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.title").value("Updated Event"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateEvent_notFound_shouldReturn404() throws Exception {
        EventRequestDTO requestDTO = new EventRequestDTO();
        mockMvc.perform(put("/event/update")
                        .param("id", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser")
    void addEvent_shouldCreateEvent() throws Exception {
        EventRequestDTO requestDTO = new EventRequestDTO();
        requestDTO.setTitle("New Event");
        requestDTO.setDescription("New Description");
        requestDTO.setEventStatus(String.valueOf(EventStatus.WILL));
        requestDTO.setOrganizationId(testOrganization.getId());
        requestDTO.setFilename(testPhoto.getFilename());
        requestDTO.setUploadId(testPhoto.getUploadId());

        mockMvc.perform(post("/event/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.title").value("New Event"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getParticipantsByEventId_shouldReturnParticipants() throws Exception {
        mockMvc.perform(get("/event/{id}/participants", testEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getEventsByStatusAndUserId_shouldReturnFilteredEvents() throws Exception {
        mockMvc.perform(get("/event/filterByStatusAndAppUserIdForUser")
                        .param("status", EventStatus.WILL.name())
                        .param("appUserId", String.valueOf(testUser.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getEventsByOrganizerId_shouldReturnOrganizerEvents() throws Exception {
        mockMvc.perform(get("/event/getEventsByOrganizerId")
                        .param("organizerId", String.valueOf(testUser.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getEventsByNeededRole_shouldReturnEvents() throws Exception {
        mockMvc.perform(get("/event/getEventsByNeededRole")
                        .param("role", UserRole.VOLUNTEER.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }
}
