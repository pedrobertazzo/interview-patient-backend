package com.healthcare.patient.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

data class AppointmentRequest(
    @field:NotNull
    val patientId: UUID,

    @field:NotNull
    val appointmentDateTime: LocalDateTime,

    @field:NotBlank
    val reason: String
)

