package com.healthcare.patient.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Entity
@Table(name = "patients")
class Patient(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotBlank
    var firstName: String,

    @NotBlank
    var lastName: String,

    @Email
    @NotBlank
    var email: String,

    var phone: String? = null,

    var dateOfBirth: LocalDate? = null,

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true)
    var appointments: MutableList<Appointment> = mutableListOf()
)

