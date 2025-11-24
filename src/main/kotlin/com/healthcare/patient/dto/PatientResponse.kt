package com.healthcare.patient.dto

import java.time.LocalDate

data class PatientResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val dateOfBirth: LocalDate?
)

