package com.example.contactmanagerapi.mapper;

import com.example.contactmanagerapi.dto.AddressResponseDto;
import com.example.contactmanagerapi.dto.PersonCreateDto;
import com.example.contactmanagerapi.dto.PersonResponseDto;
import com.example.contactmanagerapi.dto.PhoneResponseDto;
import com.example.contactmanagerapi.entity.Address;
import com.example.contactmanagerapi.entity.Person;
import com.example.contactmanagerapi.entity.Phone;
import com.example.contactmanagerapi.enums.PersonStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonMapper {

    public Person toEntity(PersonCreateDto dto) {
        Person person = Person.builder()
                .name(dto.name())
                .email(dto.email())
                .status(PersonStatus.ATIVO)
                .build();

        List<Phone> phones = dto.phones().stream()
                .map(p -> Phone.builder()
                        .number(p.number())
                        .type(p.type())
                        .person(person)
                        .build())
                .toList();

        List<Address> addresses = dto.addresses().stream()
                .map(a -> Address.builder()
                        .street(a.street())
                        .number(a.number())
                        .neighborhood(a.neighborhood())
                        .city(a.city())
                        .state(a.state())
                        .zipCode(a.zipCode())
                        .person(person)
                        .build())
                .toList();

        person.setPhones(phones);
        person.setAddresses(addresses);

        return person;
    }

    public PersonResponseDto toDto(Person entity) {
        List<PhoneResponseDto> phones = entity.getPhones().stream()
                .map(p -> new PhoneResponseDto(p.getId(), p.getNumber(), p.getType()))
                .toList();

        List<AddressResponseDto> addresses = entity.getAddresses().stream()
                .map(a -> new AddressResponseDto(
                        a.getId(), a.getStreet(), a.getNumber(),
                        a.getNeighborhood(), a.getCity(), a.getState(), a.getZipCode()))
                .toList();

        return new PersonResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getStatus(),
                entity.getCreatedAt(),
                phones,
                addresses
        );
    }
}