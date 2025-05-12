package com.example.fundraisingmanagersii.controller;

import com.example.fundraisingmanagersii.dtos.FundraisingEventCreateDto;
import com.example.fundraisingmanagersii.dtos.FundraisingEventGetDto;
import com.example.fundraisingmanagersii.services.FundraisingEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class FundraisingEventController {
    private final FundraisingEventService fundraisingEventService;

    //  1. Create a new fundraising event
    @PostMapping
    public ResponseEntity<Object> createNewFundraisingEvent(@RequestBody FundraisingEventCreateDto dto){
        FundraisingEventGetDto created = fundraisingEventService.createNewFundraisingEvent(dto);
        return new ResponseEntity<>(String.format("Fundraising event created: %s", created.getName()), HttpStatus.CREATED);
    }
}
