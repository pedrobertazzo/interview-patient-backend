package com.healthcare.patient.dto

import com.healthcare.patient.model.AppointmentStatus
import java.time.LocalDateTime
import java.util.UUID

data class AppointmentResponse(
    val id: UUID,
    val patientId: UUID,
    val appointmentDateTime: LocalDateTime,
    val reason: String,
    val status: AppointmentStatus
)

