package com.example.fundraisingmanagersii.services;

import com.example.fundraisingmanagersii.dtos.CollectionBoxGetDto;
import com.example.fundraisingmanagersii.dtos.MoneyAddRequestDto;
import com.example.fundraisingmanagersii.exceptions.InvalidOperationException;
import com.example.fundraisingmanagersii.exceptions.NotFoundException;
import com.example.fundraisingmanagersii.models.CollectionBox;
import com.example.fundraisingmanagersii.models.FundraisingEvent;
import com.example.fundraisingmanagersii.repositories.CollectionBoxRepository;
import com.example.fundraisingmanagersii.repositories.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {
    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository fundraisingEventRepository;

    public CollectionBoxGetDto registerNewCollectionBox(){
        CollectionBox box = new CollectionBox();
        box.setIsEmpty(true);
        box.setMoneyInside(new HashMap<>());
        box.setFundraisingEvent(null);
        collectionBoxRepository.save(box);

        return new CollectionBoxGetDto(box.getId(), (box.getFundraisingEvent() != null), box.getIsEmpty());
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

    public CollectionBoxGetDto assignToCollectionBox(long id, long eventId){
        CollectionBox box = collectionBoxRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Collection box with id %s not found", id)));
        FundraisingEvent event = fundraisingEventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Fundraising event with id %s not found", eventId)));

        if (!box.getIsEmpty()){
            throw new InvalidOperationException(String.format("Collection box with id %s is not empty", id));
        }

        box.setFundraisingEvent(event);
        collectionBoxRepository.save(box);

        return new CollectionBoxGetDto(box.getId(), (box.getFundraisingEvent() != null), box.getIsEmpty());
    }

    public CollectionBoxGetDto addMoneyToCollectionBox(long id, MoneyAddRequestDto moneyAddRequestDto){
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

    // to remove -- Debug Only
    public List<CollectionBox> debugAllBoxes(){
        return collectionBoxRepository.findAll().stream().toList();
    }
    
    public Long clearMap(Long id){
        CollectionBox box = collectionBoxRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Collection box with id %s not found", id)));
        box.getMoneyInside().values().forEach(a -> a = BigDecimal.ZERO);
        collectionBoxRepository.save(box);
        return box.getId();
    }
}
