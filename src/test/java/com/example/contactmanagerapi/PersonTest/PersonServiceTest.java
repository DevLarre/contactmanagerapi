package com.example.contactmanagerapi.PersonTest;

import com.example.contactmanagerapi.dto.*;
import com.example.contactmanagerapi.entity.*;
import com.example.contactmanagerapi.enums.PersonStatus;
import com.example.contactmanagerapi.enums.PhoneType;
import com.example.contactmanagerapi.exception.BusinessException;
import com.example.contactmanagerapi.exception.ResourceNotFoundException;
import com.example.contactmanagerapi.mapper.PersonMapper;
import com.example.contactmanagerapi.repository.PersonRepository;
import com.example.contactmanagerapi.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock private PersonRepository repository;
    @Mock private PersonMapper mapper;
    @InjectMocks private PersonService service;

    private Person person;
    private PersonResponseDto responseDto;
    private PersonCreateDto createDto;

    @BeforeEach
    void setUp() {
        createDto = new PersonCreateDto(
                "João Silva", "joao@email.com",
                List.of(new PhoneCreateDto("(11) 91234-5678", PhoneType.CELULAR)),
                List.of(new AddressCreateDto("Rua A", "10", "Centro",
                        "São Paulo", "SP", "01001-000"))
        );

        person = Person.builder()
                .id(UUID.randomUUID())
                .name("João Silva")
                .email("joao@email.com")
                .status(PersonStatus.ATIVO)
                .createdAt(LocalDateTime.now())
                .phones(new ArrayList<>())
                .addresses(new ArrayList<>())
                .build();

        responseDto = new PersonResponseDto(
                person.getId(), "João Silva", "joao@email.com",
                PersonStatus.ATIVO, person.getCreatedAt(),
                List.of(new PhoneResponseDto(UUID.randomUUID(), "(11) 91234-5678", PhoneType.CELULAR)),
                List.of(new AddressResponseDto(UUID.randomUUID(), "Rua A", "10",
                        "Centro", "São Paulo", "SP", "01001-000"))
        );
    }

    @Test
    @DisplayName("Deve cadastrar contato com sucesso")
    void create_success() {
        when(repository.existsByEmail(createDto.email())).thenReturn(false);
        when(mapper.toEntity(createDto)).thenReturn(person);
        when(repository.save(person)).thenReturn(person);
        when(mapper.toDto(person)).thenReturn(responseDto);

        PersonResponseDto result = service.create(createDto);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("joao@email.com");
        verify(repository).save(person);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando email já cadastrado")
    void create_emailDuplicado() {
        when(repository.existsByEmail(createDto.email())).thenReturn(true);

        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email já cadastrado");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar contato por ID")
    void findById_success() {
        when(repository.findById(person.getId())).thenReturn(Optional.of(person));
        when(mapper.toDto(person)).thenReturn(responseDto);

        PersonResponseDto result = service.findById(person.getId());

        assertThat(result.id()).isEqualTo(person.getId());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException para ID inexistente")
    void findById_notFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Contato não encontrado");
    }

    @Test
    @DisplayName("Deve retornar página de contatos")
    void findAll_success() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(person)));
        when(mapper.toDto(person)).thenReturn(responseDto);

        Page<PersonResponseDto> result = service.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Deve alternar status de ATIVO para INATIVO")
    void toggleStatus_ativoParaInativo() {
        person.setStatus(PersonStatus.ATIVO);
        when(repository.findById(person.getId())).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(person);
        when(mapper.toDto(person)).thenReturn(responseDto);

        service.toggleStatus(person.getId());

        assertThat(person.getStatus()).isEqualTo(PersonStatus.INATIVO);
        verify(repository).save(person);
    }

    @Test
    @DisplayName("Deve alternar status de INATIVO para ATIVO")
    void toggleStatus_inativoParaAtivo() {
        person.setStatus(PersonStatus.INATIVO);
        when(repository.findById(person.getId())).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(person);
        when(mapper.toDto(person)).thenReturn(responseDto);

        service.toggleStatus(person.getId());

        assertThat(person.getStatus()).isEqualTo(PersonStatus.ATIVO);
    }

    @Test
    @DisplayName("Deve deletar contato com sucesso")
    void delete_success() {
        when(repository.findById(person.getId())).thenReturn(Optional.of(person));

        service.delete(person.getId());

        verify(repository).delete(person);
    }
}