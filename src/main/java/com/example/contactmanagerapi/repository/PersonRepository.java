package com.example.contactmanagerapi.repository;

import com.example.contactmanagerapi.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    Optional<Person> findByEmail(String email);

    Page<Person> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Person> findByAddresses_CityIgnoreCase(String city, Pageable pageable);
}