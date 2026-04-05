package com.example.contactmanagerapi.dto;

import com.example.contactmanagerapi.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PersonCreateDto(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String name,

        @NotBlank(message = "email é obrigatório")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
        String email,

        @NotEmpty(message = "Pelo menos um telefone é obrigatório")
        @Valid
        List<PhoneCreateDto> phones,

        @NotEmpty(message = "Pelo menos um endereço é obrigatóio")
        @Valid
        List<AddressCreateDto> addresses
) {}