package com.example.fundraisingmanagersii.services;

import com.example.fundraisingmanagersii.dtos.FundraisingEventCreateDto;
import com.example.fundraisingmanagersii.dtos.FundraisingEventGetDto;
import com.example.fundraisingmanagersii.exceptions.InvalidOperationException;
import com.example.fundraisingmanagersii.models.Currency;
import com.example.fundraisingmanagersii.models.FundraisingEvent;
import com.example.fundraisingmanagersii.repositories.FundraisingEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundraisingEventServiceTest {

    @Mock
    private FundraisingEventRepository fundraisingEventRepository;

    @InjectMocks
    private FundraisingEventService fundraisingEventService;

    @Test
    public void createNewFundraisingEvent_shouldCreateAndReturnDto(){
        FundraisingEventCreateDto dto = new FundraisingEventCreateDto();
        dto.setName("TestEvent");
        dto.setCurrency(Currency.EUR);

        FundraisingEvent fundraisingEvent = new FundraisingEvent();
        fundraisingEvent.setName(dto.getName());
        fundraisingEvent.setCurrency(dto.getCurrency());
        fundraisingEvent.setBalance(BigDecimal.ZERO);

        when(fundraisingEventRepository.save(any(FundraisingEvent.class))).thenReturn(fundraisingEvent);

        FundraisingEventGetDto result = fundraisingEventService.createNewFundraisingEvent(dto);
        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getCurrency(), result.getCurrency());
        assertEquals(BigDecimal.ZERO, result.getBalance());

        verify(fundraisingEventRepository, times(1)).save(any(FundraisingEvent.class));
    }

    @Test
    public void createNewFundraisingEvent_shouldThrowExceptionWhenNameIsNull(){
        FundraisingEventCreateDto dto = new FundraisingEventCreateDto();
        dto.setCurrency(Currency.EUR);

        assertThrows(InvalidOperationException.class, () -> {
           fundraisingEventService.createNewFundraisingEvent(dto);
        });

        verify(fundraisingEventRepository, never()).save(any());
    }

    @Test
    public void createNewFundraisingEvent_shouldThrowExceptionWhenNameIsEmpty(){
        FundraisingEventCreateDto dto = new FundraisingEventCreateDto();
        dto.setCurrency(Currency.EUR);
        dto.setName("");

        assertThrows(InvalidOperationException.class, () -> {
            fundraisingEventService.createNewFundraisingEvent(dto);
        });

        verify(fundraisingEventRepository, never()).save(any());
    }


    @Test
    public void createNewFundraisingEvent_shouldThrowExceptionWhenCurrencyIsNull(){
        FundraisingEventCreateDto dto = new FundraisingEventCreateDto();
        dto.setName("TestEvent");

        assertThrows(InvalidOperationException.class, () -> {
            fundraisingEventService.createNewFundraisingEvent(dto);
        });

        verify(fundraisingEventRepository, never()).save(any());
    }

    @Test
    public void getFundraisingEventsReport_shouldReturnLDtoList(){
        FundraisingEvent e1 = new FundraisingEvent();
        FundraisingEvent e2 = new FundraisingEvent();
        e1.setName("TestEvent");
        e2.setName("TestEvent2");
        e1.setCurrency(Currency.EUR);
        e2.setCurrency(Currency.PLN);
        e1.setBalance(BigDecimal.valueOf(100));
        e2.setBalance(BigDecimal.ZERO);

        when(fundraisingEventRepository.findAll()).thenReturn(List.of(e1, e2));

        List<FundraisingEventGetDto> result = fundraisingEventService.getFundraisingEventsReport();

        assertEquals(2, result.size());
        assertEquals(e1.getName(), result.get(0).getName());
        assertEquals(e2.getName(), result.get(1).getName());
        assertEquals(e1.getCurrency(), result.get(0).getCurrency());
        assertEquals(e2.getCurrency(), result.get(1).getCurrency());

        verify(fundraisingEventRepository, times(1)).findAll();
    }
}
