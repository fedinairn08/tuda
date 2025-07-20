package com.tuda.unit.service;

import com.tuda.data.entity.Event;
import com.tuda.data.entity.Guest;
import com.tuda.data.entity.Organization;
import com.tuda.data.entity.Photo;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.exception.BadRequestException;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.GuestRepository;
import com.tuda.service.KeyService;
import com.tuda.service.impl.EmailServiceImpl;
import com.tuda.service.impl.GuestServiceImpl;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tuda.unit.preparer.EntityPreparer;
import com.tuda.unit.preparer.RequestDTOPreparer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private KeyService keyService;

    @InjectMocks
    private GuestServiceImpl guestService;

    @Test
    void addGuest_shouldMapSaveAndSendEmail() {
        // given
        GuestRequestDTO dto = RequestDTOPreparer.getGuestRequestDTO();

        Organization organization = EntityPreparer.getTestOrganization();
        Photo photo = EntityPreparer.getTestPhoto();
        Event event = EntityPreparer.getTestEvent(photo, organization);
        Guest guest = EntityPreparer.getTestGuest(event);

        when(modelMapper.map(dto, Guest.class)).thenReturn(guest);
        when(keyService.generateKey()).thenReturn("1");
        when(guestRepository.save(any(Guest.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        Guest result = guestService.addGuest(dto);

        // then
        assertNotNull(result);
        assertEquals("1", result.getKeyId());
        verify(emailService).sendQrEmail(
                eq(dto.getMail()),
                contains("QR-код для участия на мероприятии FunFest"),
                contains(guest.getFullName()),
                eq("1")
        );
        verify(guestRepository).save(guest);
    }

    @Test
    void addGuest_shouldThrowBadRequestException_onMappingError() {
        // given
        GuestRequestDTO dto = RequestDTOPreparer.getGuestRequestDTO();
        when(modelMapper.map(any(), eq(Guest.class)))
                .thenThrow(new RuntimeException("Mapping failed"));

        // when / then
        BadRequestException ex = assertThrows(BadRequestException.class, () -> guestService.addGuest(dto));
        assertTrue(ex.getMessage().contains("Ошибка: Mapping failed"));
        verifyNoInteractions(emailService, keyService, guestRepository);
    }

    @Test
    void markPresence_shouldSetStatusTrueAndSave() {
        // given
        Organization organization = EntityPreparer.getTestOrganization();
        Photo photo = EntityPreparer.getTestPhoto();
        Event event = EntityPreparer.getTestEvent(photo, organization);
        Guest guest = EntityPreparer.getTestGuest(event);
        guest.setStatus(false);

        when(guestRepository.findById(guest.getId())).thenReturn(Optional.of(guest));
        when(guestRepository.save(guest)).thenReturn(guest);

        // when
        Guest result = guestService.markPresence(guest.getId());

        // then
        assertTrue(result.getStatus());
        verify(guestRepository).save(guest);
    }

    @Test
    void markPresence_shouldThrowNotFoundException_ifGuestNotFound() {
        // given
        Long guestId = 100L;
        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        // when / then
        NotFoundException ex = assertThrows(NotFoundException.class, () -> guestService.markPresence(guestId));
        assertEquals("Guest with id: 100 -- is not found", ex.getMessage());
        verify(guestRepository).findById(guestId);
        verify(guestRepository, never()).save(any());
    }
}
