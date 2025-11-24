package com.healthcare.patient.repository

import com.healthcare.patient.model.Appointment
import com.healthcare.patient.model.AppointmentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {
    fun findByPatientId(patientId: Long): List<Appointment>
    fun findByStatus(status: AppointmentStatus): List<Appointment>
    fun findByAppointmentDateTimeBetween(start: LocalDateTime, end: LocalDateTime): List<Appointment>
}

