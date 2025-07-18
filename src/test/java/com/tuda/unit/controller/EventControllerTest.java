package com.tuda.unit.controller;

import com.tuda.controller.EventController;
import com.tuda.data.entity.Event;
import com.tuda.data.entity.Organization;
import com.tuda.data.entity.Photo;
import com.tuda.data.enums.EventStatus;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import com.tuda.dto.response.PhotoResponseDTO;
import com.tuda.service.EventService;
import com.tuda.unit.preparer.EventPreparer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class EventControllerTest {
    @Mock
    private EventService eventService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenGetAllEvents_thenReturnEventList() throws Exception {
        Event testEvent = EventPreparer.getTestEvent();
        EventResponseDTO testEventResponseDTO = EventPreparer.getTestEventResponseDTO(testEvent, testEvent.getPhoto());
        List<Event> expectedEvents = List.of(testEvent);
        List<EventResponseDTO> expectedEventResponseDTOs = List.of(testEventResponseDTO);

        when(eventService.getAllEvents()).thenReturn(expectedEvents);
        when(ReflectionTestUtils.invokeMethod(eventController, "serialize", expectedEvents, EventResponseDTO.class))
                .thenReturn(expectedEventResponseDTOs);

        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        mockMvc.perform(get("/event/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.errorMassage").doesNotExist())
                .andExpect(jsonPath("$.result[0][0].id").value(testEvent.getId()))
                .andExpect(jsonPath("$.result[0][0].organization.id").value(testEvent.getOrganization().getId()))
                .andExpect(jsonPath("$.result[0][0].organization.name").value(testEvent.getOrganization().getName()))
                .andExpect(jsonPath("$.result[0][0].organization.phoneNumber").value(testEvent.getOrganization().getPhoneNumber()))
                .andExpect(jsonPath("$.result[0][0].city").value(testEvent.getCity()))
                .andExpect(jsonPath("$.result[0][0].date[0]").value(testEvent.getDate().getYear()))
                .andExpect(jsonPath("$.result[0][0].date[1]").value(testEvent.getDate().getMonthValue()))
                .andExpect(jsonPath("$.result[0][0].date[2]").value(testEvent.getDate().getDayOfMonth()))
                .andExpect(jsonPath("$.result[0][0].date[3]").value(testEvent.getDate().getHour()))
                .andExpect(jsonPath("$.result[0][0].date[4]").value(testEvent.getDate().getMinute()))
                .andExpect(jsonPath("$.result[0][0].date[5]").value(testEvent.getDate().getSecond()))
                .andExpect(jsonPath("$.result[0][0].title").value(testEvent.getTitle()))
                .andExpect(jsonPath("$.result[0][0].description").value(testEvent.getDescription()))
                .andExpect(jsonPath("$.result[0][0].participantsNumber").value(testEvent.getParticipantsNumber()))
                .andExpect(jsonPath("$.result[0][0].volunteersNumber").value(testEvent.getVolunteersNumber()))
                .andExpect(jsonPath("$.result[0][0].eventStatus").value(testEvent.getEventStatus().toString()))
                .andExpect(jsonPath("$.result[0][0].photo.uploadId").value(testEvent.getPhoto().getUploadId().toString()))
                .andExpect(jsonPath("$.result[0][0].photo.filename").value(testEvent.getPhoto().getFilename()));
    }

}
