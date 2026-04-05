package com.example.contactmanagerapi.dto;

import com.example.contactmanagerapi.enums.PhoneType;

import java.util.UUID;

public record PhoneResponseDto(
        UUID id,
        String number,
        PhoneType type
) {}