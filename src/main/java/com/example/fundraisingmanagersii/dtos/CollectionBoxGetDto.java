package com.example.fundraisingmanagersii.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CollectionBoxGetDto {
    private Long id;
    private Boolean isAssigned;
    private Boolean isEmpty;
}
