package com.healthcare.patient.dto

import com.healthcare.patient.model.AppointmentStatus
import java.time.LocalDateTime

data class AppointmentResponse(
    val id: Long,
    val patientId: Long,
    val appointmentDateTime: LocalDateTime,
    val reason: String,
    val status: AppointmentStatus
)

