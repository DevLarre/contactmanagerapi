package com.example.contactmanagerapi.dto;

import com.example.contactmanagerapi.enums.PersonStatus;

import java.util.UUID;

public record AddressResponseDto(
   UUID id,
   String address,
   String number,
   String neighborhood,
   String city,
   String state,
   String zipCode
) {}