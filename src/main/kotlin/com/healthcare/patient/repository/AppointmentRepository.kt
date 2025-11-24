package com.healthcare.patient.repository

import com.healthcare.patient.model.Appointment
import com.healthcare.patient.model.AppointmentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface AppointmentRepository : JpaRepository<Appointment, UUID> {
    fun findByPatientId(patientId: UUID): List<Appointment>
    fun findByStatus(status: AppointmentStatus): List<Appointment>
    fun findByAppointmentDateTimeBetween(start: LocalDateTime, end: LocalDateTime): List<Appointment>
}

