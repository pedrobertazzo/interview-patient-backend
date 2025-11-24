package com.healthcare.patient.dto

import java.time.LocalDate
import java.time.Period

data class PatientResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val dateOfBirth: LocalDate?
) {
    val fullName: String
        get() = "$firstName $lastName"

    val age: Int?
        get() = dateOfBirth?.let {
            Period.between(it, LocalDate.now()).years
        }
}

