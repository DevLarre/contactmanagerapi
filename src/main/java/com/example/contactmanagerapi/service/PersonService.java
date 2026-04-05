package com.example.contactmanagerapi.service;

import com.example.contactmanagerapi.dto.PersonCreateDto;
import com.example.contactmanagerapi.dto.PersonResponseDto;
import com.example.contactmanagerapi.dto.PersonUpdateDto;
import com.example.contactmanagerapi.entity.Person;
import com.example.contactmanagerapi.enums.PersonStatus;
import com.example.contactmanagerapi.exception.BusinessException;
import com.example.contactmanagerapi.exception.ResourceNotFoundException;
import com.example.contactmanagerapi.mapper.PersonMapper;
import com.example.contactmanagerapi.repository.AdressRepository;
import com.example.contactmanagerapi.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository repository;
    private final PersonMapper mapper;

    @Transactional
    public PersonResponseDto create(PersonCreateDto dto) {
        log.info("Cadastrando contato com email: {}", dto.email());

        if (repository.existsByEmail(dto.email())){
            throw new BusinessException("Email já cadastrado");
        }

        Person saved = repository.save(mapper.toEntity(dto));
        log.info("Contato cadastrado com ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public PersonResponseDto findById(UUID id) {
        log.info("Buscando contato ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<PersonResponseDto> findAll(Pageable pageable) {
        log.info("Listando contatos - página: {}", pageable.getPageNumber());
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PersonResponseDto> findByName(String name, Pageable pageable) {
        log.info("Filtrando contatos por nome: {}", name);
        return repository.findByNameContainingIgnoreCase(name, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PersonResponseDto> findByCity(String city, Pageable pageable) {
        log.info("Filtrando contatos por cidade: {}", city);
        return repository.findByAddresses_CityIgnoreCase(city, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public PersonResponseDto findByEmail(String email) {
        log.info("Buscando contato por email: {}", email);
        return repository.findByEmail(email)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));
    }

    @Transactional
    public PersonResponseDto update(UUID id, PersonUpdateDto dto) {
        log.info("Atualizando contato ID: {}", id);

        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        if (repository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new BusinessException("Email já cadastrado para outro contato");
        }

        person.setName(dto.name());
        person.setEmail(dto.email());

        Person updated = repository.save(person);
        log.info("Contato atualizado ID: {}", updated.getId());
        return mapper.toDto(updated);
    }

    @Transactional
    public PersonResponseDto toggleStatus(UUID id) {
        log.info("Alternando status do contato ID: {}", id);

        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        PersonStatus novoStatus = person.getStatus() == PersonStatus.ATIVO
                ? PersonStatus.INATIVO
                : PersonStatus.ATIVO;

        person.setStatus(novoStatus);
        log.info("Status alterado para {} - ID: {}", novoStatus, id);
        return mapper.toDto(repository.save(person));
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deletando contato ID: {}", id);
        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));
        repository.delete(person);
        log.info("Contato deletado ID: {}", id);
    }
}