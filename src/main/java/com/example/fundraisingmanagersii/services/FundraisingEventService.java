package com.example.fundraisingmanagersii.services;

import com.example.fundraisingmanagersii.dtos.FundraisingEventCreateDto;
import com.example.fundraisingmanagersii.dtos.FundraisingEventGetDto;
import com.example.fundraisingmanagersii.exceptions.InvalidOperationException;
import com.example.fundraisingmanagersii.models.FundraisingEvent;
import com.example.fundraisingmanagersii.repositories.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;

    public FundraisingEventGetDto createNewFundraisingEvent(FundraisingEventCreateDto dto){
        if (dto.getName() == null || dto.getCurrency() == null){
            throw new InvalidOperationException("Name and currency are required");
        }
        FundraisingEvent fundraisingEvent = new FundraisingEvent();
        fundraisingEvent.setName(dto.getName());
        fundraisingEvent.setCurrency(dto.getCurrency());
        fundraisingEvent.setBalance(BigDecimal.ZERO);
        fundraisingEventRepository.save(fundraisingEvent);

        return new FundraisingEventGetDto(fundraisingEvent.getName(), fundraisingEvent.getBalance(), fundraisingEvent.getCurrency());
    }

    public List<FundraisingEventGetDto> getFundraisingEventsReport(){
        return fundraisingEventRepository.findAll().stream().map(
                e -> new FundraisingEventGetDto(
                        e.getName(),
                        e.getBalance(),
                        e.getCurrency()
                )
        ).toList();
    }
}
