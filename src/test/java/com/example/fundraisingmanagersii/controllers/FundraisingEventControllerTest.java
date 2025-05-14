package com.example.fundraisingmanagersii.controllers;

import com.example.fundraisingmanagersii.dtos.FundraisingEventCreateDto;
import com.example.fundraisingmanagersii.models.Currency;
import com.example.fundraisingmanagersii.services.CollectionBoxService;
import com.example.fundraisingmanagersii.services.FundraisingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class FundraisingEventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FundraisingEventService fundraisingEventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createNewFundraisingEvent_shouldReturnCreated() throws Exception{
        FundraisingEventCreateDto dto = new FundraisingEventCreateDto();
        dto.setName("Test");
        dto.setCurrency(Currency.PLN);

        mockMvc.perform(post("/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createNewFundraisingEvent_shouldReturnBadRequest() throws Exception{
        mockMvc.perform(post("/events/create"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createNewFundraisingEvent_shouldReturnNotFound() throws Exception{
        FundraisingEventCreateDto dto = new FundraisingEventCreateDto();
        dto.setName(null);
        dto.setCurrency(null);

        mockMvc.perform(post("/events/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        dto.setCurrency(Currency.PLN);

        mockMvc.perform(post("/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        dto.setCurrency(null);
        dto.setName("Test");

        mockMvc.perform(post("/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getEventsReport_shouldReturnOk() throws Exception{
        mockMvc.perform(get("/events/report"))
                .andExpect(status().isOk());
    }
}
