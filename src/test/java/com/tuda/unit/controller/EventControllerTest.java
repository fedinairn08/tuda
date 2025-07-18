package com.tuda.unit.controller;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tuda.controller.EventController;
import com.tuda.data.entity.Event;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.service.EventService;
import com.tuda.unit.preparer.EventPreparer;
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

        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
    }

    @Test
    void whenGetAllEvents_thenReturnEventList() throws Exception {
        Event testEvent = EventPreparer.getTestEvent();
        EventResponseDTO testEventResponseDTO = EventPreparer.getTestEventResponseDTO(testEvent, testEvent.getPhoto());
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
                .andExpect(jsonPath("$.result[0][0]").value(objectMapper.readValue(expectedJson, Map.class)));
    }
}