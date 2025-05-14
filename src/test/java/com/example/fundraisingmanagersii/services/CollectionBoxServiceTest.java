package com.example.fundraisingmanagersii.services;

import com.example.fundraisingmanagersii.dtos.CollectionBoxGetDto;
import com.example.fundraisingmanagersii.dtos.MoneyAddRequestDto;
import com.example.fundraisingmanagersii.exceptions.InvalidOperationException;
import com.example.fundraisingmanagersii.exceptions.NotFoundException;
import com.example.fundraisingmanagersii.models.CollectionBox;
import com.example.fundraisingmanagersii.models.Currency;
import com.example.fundraisingmanagersii.models.FundraisingEvent;
import com.example.fundraisingmanagersii.repositories.CollectionBoxRepository;
import com.example.fundraisingmanagersii.repositories.FundraisingEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectionBoxServiceTest {

    @Mock
    private CollectionBoxRepository collectionBoxRepository;
    @Mock
    private FundraisingEventRepository fundraisingEventRepository;
    @Mock
    private CurrencyConversionService currencyConversionService;

    @InjectMocks
    private CollectionBoxService collectionBoxService;

    @Test
    public void registerNewCollectionBox_shouldCreateNewCollectionBox() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(null);

        when(collectionBoxRepository.save(any(CollectionBox.class))).thenReturn(box);

        CollectionBoxGetDto result = collectionBoxService.registerNewCollectionBox();

        assertNotNull(result);
        assertEquals(box.getId(), result.getId());
        assertEquals(box.getFundraisingEvent()!=null , result.getIsAssigned());
        assertEquals(box.getIsEmpty(), result.getIsEmpty());

        verify(collectionBoxRepository, times(1)).save(any(CollectionBox.class));
    }

    @Test
    public void listAllCollectionBoxes_shouldReturnDtoList(){
        CollectionBox c1 = new CollectionBox();
        CollectionBox c2 = new CollectionBox();
        c1.setId(1L);
        c2.setId(2L);
        c1.setFundraisingEvent(null);
        c2.setFundraisingEvent(null);

        when(collectionBoxRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CollectionBoxGetDto> result = collectionBoxService.listAllCollectionBoxes();

        assertEquals(2, result.size());
        assertEquals(c1.getId(), result.get(0).getId());
        assertEquals(c2.getId(), result.get(1).getId());
        assertEquals(c1.getFundraisingEvent()!=null , result.get(0).getIsAssigned());
        assertEquals(c2.getFundraisingEvent()!=null , result.get(1).getIsAssigned());
        assertEquals(c1.getIsEmpty(), result.get(0).getIsEmpty());
        assertEquals(c2.getIsEmpty(), result.get(1).getIsEmpty());
    }

    @Test
    public void unregisterCollectionBox_shouldReturnDeletedCollectionBoxId(){
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(null);

        when(collectionBoxRepository.findById(any(Long.class))).thenReturn(Optional.of(box));
        doNothing().when(collectionBoxRepository).delete(any(CollectionBox.class));

        Long deletedId = collectionBoxService.unregisterCollectionBox(box.getId());

        assertNotNull(deletedId);
        assertEquals(box.getId(), deletedId);

        verify(collectionBoxRepository, times(1)).delete(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void unregisterCollectionBox_shouldThrowExceptionWhenIdDoesNotExist(){
        assertThrows(NotFoundException.class, () -> {
            collectionBoxService.unregisterCollectionBox(1L);
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, never()).delete(any(CollectionBox.class));
    }

    @Test
    public void assignToCollectionBox_shouldAssignToCollectionBox(){
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(null);
        FundraisingEvent event = new FundraisingEvent();
        event.setId(1L);
        event.setCurrency(Currency.USD);
        event.setBalance(BigDecimal.ZERO);

        when(collectionBoxRepository.findById(any(Long.class))).thenReturn(Optional.of(box));
        when(fundraisingEventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));

        when(collectionBoxRepository.save(any(CollectionBox.class))).thenReturn(box);

        CollectionBoxGetDto result = collectionBoxService.assignToCollectionBox(box.getId(), event.getId());

        assertNotNull(result);
        assertEquals(box.getId(), result.getId());
        assertEquals(box.getFundraisingEvent()!=null, result.getIsAssigned());
        assertEquals(box.getIsEmpty(), result.getIsEmpty());

        verify(collectionBoxRepository, times(1)).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
        verify(fundraisingEventRepository, times(1)).findById(any(Long.class));
    }


    @Test
    public void assignToCollectionBox_shouldThrowExceptionWhenIdsDoesNotExist(){
        assertThrows(NotFoundException.class, () -> {
            collectionBoxService.assignToCollectionBox(1L, 1L);
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void assignToCollectionBox_shouldThrowExceptionWhenFundraisingEventIdDoesNotExist(){
        FundraisingEvent event = new FundraisingEvent();
        event.setId(1L);
        event.setCurrency(Currency.USD);
        event.setBalance(BigDecimal.ZERO);

        assertThrows(NotFoundException.class, () -> {
            collectionBoxService.assignToCollectionBox(1L, event.getId());
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void assignToCollectionBox_shouldThrowExceptionWhenCollectionIdDoesNotExist(){
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(null);

        when(collectionBoxRepository.findById(any(Long.class))).thenReturn(Optional.of(box));
        assertThrows(NotFoundException.class, () -> {
            collectionBoxService.assignToCollectionBox(box.getId(), 1L);
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
        verify(fundraisingEventRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void assignToCollectionBox_shouldThrowExceptionWhenCollectionBoxIsNotEmpty(){
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(null);
        box.getMoneyInside().merge(Currency.EUR, BigDecimal.TEN, BigDecimal::add);
        FundraisingEvent event = new FundraisingEvent();
        event.setId(1L);
        event.setCurrency(Currency.USD);
        event.setBalance(BigDecimal.ZERO);

        when(collectionBoxRepository.findById(any(Long.class))).thenReturn(Optional.of(box));
        when(fundraisingEventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));

        assertThrows(InvalidOperationException.class, () ->{
            collectionBoxService.assignToCollectionBox(box.getId(), event.getId());
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
        verify(fundraisingEventRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void addMoneyToCollectionBox_shouldAddMoneyToCollectionBox(){
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(new FundraisingEvent());
        MoneyAddRequestDto moneyAddRequestDto = new MoneyAddRequestDto();
        moneyAddRequestDto.setAmount(BigDecimal.TEN);
        moneyAddRequestDto.setCurrency(Currency.EUR);

        when(collectionBoxRepository.findById(any(Long.class))).thenReturn(Optional.of(box));
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenReturn(box);

        CollectionBoxGetDto dto = collectionBoxService.addMoneyToCollectionBox(box.getId(), moneyAddRequestDto);

        assertNotNull(dto);
        assertEquals(box.getId(), dto.getId());
        assertEquals(box.getFundraisingEvent() != null, dto.getIsAssigned());
        assertTrue(box.getMoneyInside().containsValue(moneyAddRequestDto.getAmount()));

        verify(collectionBoxRepository, times(1)).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void addMoneyToCollectionBox_shouldThrowExceptionWhenRequestedMoneyIsEmpty(){
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(new FundraisingEvent());
        MoneyAddRequestDto moneyAddRequestDto = new MoneyAddRequestDto();
        moneyAddRequestDto.setAmount(BigDecimal.ZERO);
        moneyAddRequestDto.setCurrency(Currency.EUR);

        assertThrows(InvalidOperationException.class, () ->{
            collectionBoxService.addMoneyToCollectionBox(box.getId(), moneyAddRequestDto);
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
    }

    @Test
    public void addMoneyToCollectionBox_shouldThrowExceptionWhenBoxDoesNotExist(){
        MoneyAddRequestDto moneyAddRequestDto = new MoneyAddRequestDto();
        moneyAddRequestDto.setAmount(BigDecimal.TEN);
        moneyAddRequestDto.setCurrency(Currency.EUR);
        assertThrows(NotFoundException.class, () ->{
            collectionBoxService.addMoneyToCollectionBox(1L, moneyAddRequestDto);
        });
    }

    @Test
    public void addMoneyToCollectionBox_shouldThrowExceptionWhenBoxIsNull(){
        MoneyAddRequestDto moneyAddRequestDto = new MoneyAddRequestDto();
        moneyAddRequestDto.setAmount(BigDecimal.TEN);
        moneyAddRequestDto.setCurrency(Currency.EUR);
        assertThrows(NotFoundException.class, () ->{
            collectionBoxService.addMoneyToCollectionBox(null, moneyAddRequestDto);
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, never()).findById(any(Long.class));
    }

    @Test
    public void addMoneyToCollectionBox_shouldThrowExceptionWhenBoxDoesNotHaveAssignFundraisingEvent(){
        MoneyAddRequestDto moneyAddRequestDto = new MoneyAddRequestDto();
        moneyAddRequestDto.setAmount(BigDecimal.TEN);
        moneyAddRequestDto.setCurrency(Currency.EUR);

        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(null);

        when(collectionBoxRepository.findById(any(Long.class))).thenReturn(Optional.of(box));

        assertThrows(InvalidOperationException.class, () ->{
            collectionBoxService.addMoneyToCollectionBox(box.getId(), moneyAddRequestDto);
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void transferMoneyFromCollectionBox_shouldIncreaseFundraisingEventBalance_shouldEmptyCollectionBoxBalance(){
        FundraisingEvent event = new FundraisingEvent();
        event.setId(1L);
        event.setCurrency(Currency.EUR);
        event.setBalance(BigDecimal.ZERO);

        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(event);
        for (Map.Entry<Currency, BigDecimal> entry : box.getMoneyInside().entrySet()) {
            if (entry.getKey().equals(Currency.EUR)) {
                entry.setValue(entry.getValue().add(BigDecimal.TEN));
            }
        }

        when(collectionBoxRepository.findById(box.getId())).thenReturn(Optional.of(box));
        when(fundraisingEventRepository.save(any(FundraisingEvent.class))).thenReturn(event);
        when(currencyConversionService.convert(any(Currency.class), any(Currency.class), any(BigDecimal.class)))
                .thenReturn(BigDecimal.valueOf(2.5));

        Long eventID = collectionBoxService.transferMoneyFromCollectionBox(box.getId());

        assertEquals(eventID, event.getId());
        assertTrue(box.getIsEmpty());
        System.out.println(event.getBalance());
        assertNotEquals(0, BigDecimal.ZERO.compareTo(event.getBalance()));

        assertEquals(0, BigDecimal.TEN.compareTo(event.getBalance()));
        assertNotNull(box.getMoneyInside());

        verify(collectionBoxRepository, times(1)).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
        verify(fundraisingEventRepository, times(1)).save(any(FundraisingEvent.class));
    }

    @Test
    public void transferMoneyFromCollectionBox_shouldThrowExceptionWhenBoxDoesNotExist(){
        assertThrows(NotFoundException.class, () ->{
            collectionBoxService.transferMoneyFromCollectionBox(1L);
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void transferMoneyFromCollectionBox_shouldThrowExceptionWhenBoxIsEmpty(){
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEvent(new FundraisingEvent());

        assertThrows(NotFoundException.class, () ->{
            collectionBoxService.transferMoneyFromCollectionBox(box.getId());
        });

        verify(collectionBoxRepository, never()).save(any(CollectionBox.class));
        verify(collectionBoxRepository, times(1)).findById(any(Long.class));
    }
}
