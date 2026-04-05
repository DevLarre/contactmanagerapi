package com.example.contactmanagerapi.dto;

import com.example.contactmanagerapi.enums.PersonStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PersonResponseDto(
   UUID id,
   String name,
   String email,
   PersonStatus status,
   LocalDateTime createAt,
   List<PhoneResponseDto> phones,
   List<AddressResponseDto> address
) {}