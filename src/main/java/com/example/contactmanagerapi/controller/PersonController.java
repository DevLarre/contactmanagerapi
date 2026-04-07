package com.example.contactmanagerapi.controller;

import com.example.contactmanagerapi.dto.*;
import com.example.contactmanagerapi.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
@Tag(name = "Persons", description = "Gerenciamento de contatos")
public class PersonController {

    private final PersonService service;

    @Operation(summary = "Cadastrar contato")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contato criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping
    public ResponseEntity<PersonResponseDto> create(
            @Valid @RequestBody PersonCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Buscar contato por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contato encontrado"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar contatos paginado")
    @GetMapping
    public ResponseEntity<Page<PersonResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Filtrar por nome")
    @GetMapping("/search/name")
    public ResponseEntity<Page<PersonResponseDto>> findByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByName(name, pageable));
    }

    @Operation(summary = "Filtrar por cidade")
    @GetMapping("/search/city")
    public ResponseEntity<Page<PersonResponseDto>> findByCity(
            @RequestParam String city,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByCity(city, pageable));
    }

    @Operation(summary = "Buscar por email")
    @GetMapping("/search/email")
    public ResponseEntity<PersonResponseDto> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @Operation(summary = "Atualizar contato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contato atualizado"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
            @ApiResponse(responseCode = "409", description = "Email já em uso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody PersonUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Ativar ou inativar contato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PersonResponseDto> toggleStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(service.toggleStatus(id));
    }

    @Operation(summary = "Deletar contato")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contato deletado"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}