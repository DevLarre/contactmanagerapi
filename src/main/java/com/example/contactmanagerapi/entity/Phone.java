package com.example.contactmanagerapi.entity;

import com.example.contactmanagerapi.enums.PhoneType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "phones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 11)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhoneType phone;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}