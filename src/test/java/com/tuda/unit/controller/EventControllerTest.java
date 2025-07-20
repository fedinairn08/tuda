package com.tuda.unit.controller;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tuda.controller.EventController;
import com.tuda.data.entity.Event;
import com.tuda.data.entity.Organization;
import com.tuda.data.entity.Photo;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.exception.GlobalExceptionHandler;
import com.tuda.exception.NotFoundException;
import com.tuda.service.EventService;
import com.tuda.unit.preparer.EntityPreparer;
import com.tuda.unit.preparer.ResponseDTOPreparer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(exceptionHandler)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
    }

    @Test
    void whenNotGetAllEvents_thenReturnEmptyEventList() throws Exception {
        List<Event> expectedEvents = new ArrayList<>();
        when(eventService.getAllEvents()).thenReturn(expectedEvents);
        mockMvc.perform(get("/event/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.result.length()").value(0));
    }

    @Test
    void whenGetAllEvents_thenReturnEventList() throws Exception {
        Photo photo = new Photo();
        Organization organization = new Organization();
        Event testEvent = EntityPreparer.getTestEvent(photo, organization);
        EventResponseDTO testEventResponseDTO = ResponseDTOPreparer.getTestEventResponseDTO(testEvent);
        List<Event> expectedEvents = List.of(testEvent);
        List<EventResponseDTO> expectedEventResponseDTOs = List.of(testEventResponseDTO);

        when(eventService.getAllEvents()).thenReturn(expectedEvents);
        when(ReflectionTestUtils.invokeMethod(eventController, "serialize", expectedEvents, EventResponseDTO.class))
                .thenReturn(expectedEventResponseDTOs);

        Map expectedMap = objectMapper.convertValue(testEventResponseDTO, Map.class);
        String expectedJson = objectMapper.writeValueAsString(expectedMap);

        mockMvc.perform(get("/event/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.result[0]").value(objectMapper.readValue(expectedJson, Map.class)));
    }

    @Test
    void whenGetEventId_thenReturnEvent() throws Exception {
        Photo photo = new Photo();
        Organization organization = new Organization();
        Event testEvent = EntityPreparer.getTestEvent(photo, organization);
        EventResponseDTO testEventResponseDTO = ResponseDTOPreparer.getTestEventResponseDTO(testEvent);

        when(eventService.getEventById(testEvent.getId())).thenReturn(testEvent);
        when(ReflectionTestUtils.invokeMethod(eventController, "serialize", testEvent, EventResponseDTO.class)).
                thenReturn(testEventResponseDTO);

        Map expectedMap = objectMapper.convertValue(testEventResponseDTO, Map.class);
        String expectedJson = objectMapper.writeValueAsString(expectedMap);

        mockMvc.perform(get("/event/getById/{id}",  testEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.result").value(objectMapper.readValue(expectedJson, Map.class)));
    }

    @Test
    void whenEventIdNotExisted_thenReturnNotFound() throws Exception {
        long notExistedEventId = 999L;
        when(eventService.getEventById(notExistedEventId)).
                thenThrow(new NotFoundException("Event not found with id: " + notExistedEventId));
        mockMvc.perform(get("/event/getById/{id}",  notExistedEventId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetExistedUserId_thenGetUserEventList() throws Exception {
        Long expectedUserId = 1L;
        Photo photo = new Photo();
        Organization organization = new Organization();
        Event testEvent = EntityPreparer.getTestEvent(photo, organization);
        EventResponseDTO testEventResponseDTO = ResponseDTOPreparer.getTestEventResponseDTO(testEvent);

        List<Event> expectedEvents = List.of(testEvent);
        List<EventResponseDTO> expectedEventResponseDTOs = List.of(testEventResponseDTO);

        when(eventService.getEventsByUserId(expectedUserId)).thenReturn(expectedEvents);
        when(ReflectionTestUtils.invokeMethod(eventController, "serialize", expectedEvents, EventResponseDTO.class)).
                thenReturn(expectedEventResponseDTOs);

        Map expectedMap = objectMapper.convertValue(testEventResponseDTO, Map.class);
        String expectedJson = objectMapper.writeValueAsString(expectedMap);

        mockMvc.perform(get("/event/getByUserId/{id}", expectedUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.result[0]").value(objectMapper.readValue(expectedJson, Map.class)));
    }

    @Test
    void whenGetExistedUserId_thenReturnEmptyUserEventList() throws Exception {
        Long expectedUserId = 1L;
        List<Event> expectedEvents = new ArrayList<>();
        when(eventService.getEventsByUserId(expectedUserId)).thenReturn(expectedEvents);
        mockMvc.perform(get("/event/getByUserId/{id}", expectedUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.result.length()").value(0));
    }

    @Test
    void whenGetNotExistedUserId_thenReturnNotFoundUserEventList() throws Exception {
        long notExistedUserId = 999L;
        when(eventService.getEventsByUserId(notExistedUserId)).
                thenThrow(new NotFoundException(String.format("User not found for id %d", notExistedUserId)));
        mockMvc.perform(get("/event/getByUserId/{id}", notExistedUserId))
                .andExpect(status().isNotFound());

    }

}