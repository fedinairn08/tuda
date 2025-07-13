package com.tuda.integration;

import com.tuda.AbstractIntegrationTest;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Event;
import com.tuda.data.enums.EventStatus;
import com.tuda.repository.EventRepository;
import com.tuda.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private AppUser testUser;
    private Event testEvent;

    @BeforeEach
    void setup() {
        testUser = new AppUser();
        testUser.setLogin("testuser");
        testUser.setName("Test User");
        userRepository.save(testUser);

        testEvent = Event.builder()
                .title("Test Event")
                .description("Test Description")
                .eventStatus(EventStatus.WILL)
                .build();
        eventRepository.save(testEvent);
    }

    @AfterEach
    void cleanup() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserById_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/user/getById/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value("Test User"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserById_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/user/getById/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "testuser")
    void getUserEventStatus_shouldReturnStatus() throws Exception {
        mockMvc.perform(get("/user/getEventStatus")
                        .param("appUserId", String.valueOf(testUser.getId()))
                        .param("eventId", String.valueOf(testEvent.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserEventStatus_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/user/getEventStatus")
                        .param("appUserId", "999")
                        .param("eventId", "999"))
                .andExpect(status().isNotFound());
    }
}
