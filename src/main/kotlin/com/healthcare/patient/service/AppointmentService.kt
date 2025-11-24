package com.healthcare.patient.service

import com.healthcare.patient.dto.AppointmentRequest
import com.healthcare.patient.dto.AppointmentResponse
import com.healthcare.patient.model.Appointment
import com.healthcare.patient.model.AppointmentStatus
import com.healthcare.patient.repository.AppointmentRepository
import com.healthcare.patient.repository.PatientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AppointmentService(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository
) {

    @Transactional
    fun createAppointment(request: AppointmentRequest): AppointmentResponse {
        val patient = patientRepository.findById(request.patientId)
            .orElseThrow { RuntimeException("Patient not found with id: ${request.patientId}") }

        val appointment = Appointment(
            appointmentDateTime = request.appointmentDateTime,
            reason = request.reason,
            patient = patient
        )

        val saved = appointmentRepository.save(appointment)
        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun getAppointmentById(id: Long): AppointmentResponse {
        val appointment = appointmentRepository.findById(id)
            .orElseThrow { RuntimeException("Appointment not found with id: $id") }
        return appointment.toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllAppointments(): List<AppointmentResponse> {
        return appointmentRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getAppointmentsByPatientId(patientId: Long): List<AppointmentResponse> {
        return appointmentRepository.findByPatientId(patientId).map { it.toResponse() }
    }

    @Transactional
    fun updateAppointmentStatus(id: Long, status: AppointmentStatus): AppointmentResponse {
        val appointment = appointmentRepository.findById(id)
            .orElseThrow { RuntimeException("Appointment not found with id: $id") }

        appointment.status = status
        val updated = appointmentRepository.save(appointment)
        return updated.toResponse()
    }

    @Transactional
    fun deleteAppointment(id: Long) {
        if (!appointmentRepository.existsById(id)) {
            throw RuntimeException("Appointment not found with id: $id")
        }
        appointmentRepository.deleteById(id)
    }

    private fun Appointment.toResponse() = AppointmentResponse(
        id = id!!,
        patientId = patient.id!!,
        appointmentDateTime = appointmentDateTime,
        reason = reason,
        status = status
    )
}

