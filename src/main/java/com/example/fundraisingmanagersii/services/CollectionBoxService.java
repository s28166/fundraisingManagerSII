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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {
    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository fundraisingEventRepository;
    private final CurrencyConversionService currencyConversionService;

    public CollectionBoxGetDto registerNewCollectionBox(){
        CollectionBox box = new CollectionBox();
        box.setFundraisingEvent(null);

        CollectionBox saved = collectionBoxRepository.save(box);

        return new CollectionBoxGetDto(saved.getId(), (saved.getFundraisingEvent() != null), saved.getIsEmpty());
    }

    public List<CollectionBoxGetDto> listAllCollectionBoxes(){
        return collectionBoxRepository.findAll().stream().map(
                box -> new CollectionBoxGetDto(
                        box.getId(),
                        box.getFundraisingEvent() != null,
                        box.getIsEmpty()
                )
        ).toList();
    }

    public Long unregisterCollectionBox(Long id){
        CollectionBox box = collectionBoxRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Collection box with id %s not found", id)));
        collectionBoxRepository.delete(box);
        return box.getId();
    }

    public CollectionBoxGetDto assignToCollectionBox(Long id, Long eventId){
        CollectionBox box = collectionBoxRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Collection box with id %s not found", id)));
        FundraisingEvent event = fundraisingEventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Fundraising event with id %s not found", eventId)));

        if (!box.getIsEmpty()){
            throw new InvalidOperationException(String.format("Collection box with id %s is not empty", id));
        }

        box.setFundraisingEvent(event);
        collectionBoxRepository.save(box);

        return new CollectionBoxGetDto(box.getId(), (box.getFundraisingEvent() != null), box.getIsEmpty());
    }

    public CollectionBoxGetDto addMoneyToCollectionBox(Long id, MoneyAddRequestDto moneyAddRequestDto){
        if (moneyAddRequestDto.getAmount() == null || moneyAddRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidOperationException("Money add amount must be greater than zero");
        }

        CollectionBox box = collectionBoxRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Collection box with id %s not found", id)));
        if (box.getFundraisingEvent() == null){
            throw new InvalidOperationException("Fundraising event not found, unable to add money to collection box");
        }

        box.getMoneyInside().merge(moneyAddRequestDto.getCurrency(), moneyAddRequestDto.getAmount(), BigDecimal::add);
        collectionBoxRepository.save(box);
        return new CollectionBoxGetDto(box.getId(), (box.getFundraisingEvent() != null), box.getIsEmpty());
    }

    public Long transferMoneyFromCollectionBox(Long id){
        CollectionBox box = collectionBoxRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Collection box with id %s not found", id)));

        if (box.getIsEmpty()){
            throw new InvalidOperationException("Cannot transfer money from empty collection box");
        }

        FundraisingEvent event = box.getFundraisingEvent();
        BigDecimal amountToTransfer = BigDecimal.ZERO;

        for(Map.Entry<Currency, BigDecimal> entry : box.getMoneyInside().entrySet()){
            BigDecimal converted = currencyConversionService.convert(
                    entry.getKey(), event.getCurrency(), entry.getValue()
            );
            amountToTransfer = amountToTransfer.add(converted);
        }

        event.setBalance(event.getBalance().add(amountToTransfer));
        fundraisingEventRepository.save(event);

        for(Map.Entry<Currency, BigDecimal> entry : box.getMoneyInside().entrySet()){
            entry.setValue(BigDecimal.ZERO);
        }

        collectionBoxRepository.save(box);

        return event.getId();
    }

    // DEBUG method
/*    public List<CollectionBox> debugAllBoxes(){
        return collectionBoxRepository.findAll().stream().toList();
    }*/
}
