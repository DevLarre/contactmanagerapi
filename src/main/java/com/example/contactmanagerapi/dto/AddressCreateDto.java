package com.example.contactmanagerapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressCreateDto(

        @NotBlank(message = "Rua é obrigatória")
        String street,

        @NotBlank(message = "Número é obrigatório")
        String number,

        @NotBlank(message = "Bairro é obrigatório")
        String neighborhood,

        @NotBlank(message = "Cidade é obrigatório")
        @Size(max = 100)
        String city,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
        String state,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(
                regexp = "^\\\\d{5}-\\\\d{3}$",
                message = "CEP inválido. Use o formato 00000-000"
        )
        String zipCode
) {}