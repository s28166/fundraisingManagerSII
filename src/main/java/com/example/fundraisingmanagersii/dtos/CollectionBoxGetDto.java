package com.example.fundraisingmanagersii.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CollectionBoxGetDto {
    private Long id;
    private Boolean isAssigned;
    private Boolean isEmpty;
}
