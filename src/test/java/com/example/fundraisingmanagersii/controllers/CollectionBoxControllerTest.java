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
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllCollectionBoxes_shouldReturnOk() throws Exception {

        mockMvc.perform(get("/collections/list"))
                .andExpect(status().isOk());
    }

    @Test
    public void unregisterCollectionBox_shouldReturnNotFound() throws Exception {

        mockMvc.perform(delete("/collections/2/unregister"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void unregisterCollectionBox_shouldThrowNotFoundException() throws Exception {

        mockMvc.perform(delete("/collections/3/unregister"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void assignToCollectionBox_shouldReturnNotFound() throws Exception {

        mockMvc.perform(patch("/collections/2/assign_to/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void assignToCollectionBox_shouldThrowNotFoundException() throws Exception {
        mockMvc.perform(patch("/collections/4/assign_to/1"))
                .andExpect(status().isNotFound());

        mockMvc.perform(patch("/collections/2/assign_to/5"))
                .andExpect(status().isNotFound());
    }
}
