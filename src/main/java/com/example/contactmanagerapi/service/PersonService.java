package com.example.contactmanagerapi.service;

import com.example.contactmanagerapi.repository.AdressRepository;
import com.example.contactmanagerapi.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository repository;
    private final AdressRepository adressRepository;
}
