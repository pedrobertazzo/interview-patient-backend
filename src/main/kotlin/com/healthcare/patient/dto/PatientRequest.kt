package com.healthcare.patient.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class PatientRequest(
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:Email
    @field:NotBlank
    val email: String,
    val phone: String? = null,
    val dateOfBirth: LocalDate? = null
)

