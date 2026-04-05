package com.example.contactmanagerapi.dto;

import com.example.contactmanagerapi.enums.PhoneType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PhoneCreateDto(

        @NotBlank(message = "Número é obrigatório")
        @Pattern(
                regexp = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$",
                message = "Telefone inválido. Use o formato (51) 91234-5678"
        )
        String number,

        @NotNull(message = "Tipo é obrigatório")
        PhoneType type
) {}