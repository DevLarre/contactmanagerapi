package com.example.contactmanagerapi.repository;

import com.example.contactmanagerapi.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository <Phone, UUID> {
}