package com.example.contactmanagerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PersonUpdateDto(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 150)
        String email
) {}