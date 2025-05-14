package com.example.fundraisingmanagersii.controllers;


import com.example.fundraisingmanagersii.dtos.CollectionBoxGetDto;
import com.example.fundraisingmanagersii.services.CollectionBoxService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class CollectionBoxControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CollectionBoxService collectionBoxService;

    @Test
    public void registerNewCollectionBox_shouldReturnCreated() throws Exception {
        CollectionBoxGetDto dto = new CollectionBoxGetDto(3L, false, true);
        when(collectionBoxService.registerNewCollectionBox()).thenReturn(dto);

        mockMvc.perform(post("/collections/register"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.isAssigned").value(false))
                .andExpect(jsonPath("$.isEmpty").value(true));
    }

    @Test
    public void getAllCollectionBoxes_shouldReturnOk() throws Exception {

        mockMvc.perform(get("/collections/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id").value(1L))
                .andExpect(jsonPath("$[1].isAssigned").value(true))
                .andExpect(jsonPath("$[1].isEmpty").value(true));
    }

    @Test
    public void unregisterCollectionBox_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/collections/1/unregister"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void unregisterCollectionBox_shouldThrowNotFoundException() throws Exception {

        mockMvc.perform(delete("/collections/3/unregister"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void assignToCollectionBox_shouldReturnOk() throws Exception {
        CollectionBoxGetDto dto = new CollectionBoxGetDto(3L, true, true);

        mockMvc.perform(patch("/collections/2/assign_to/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void assignToCollectionBox_shouldThrowNotFoundException() throws Exception {
        mockMvc.perform(patch("/collections/4/assign_to/1"))
                .andExpect(status().isNotFound());

        mockMvc.perform(patch("/collections/2/assign_to/5"))
                .andExpect(status().isNotFound());
    }
}
