package com.healthcare.patient.service

import com.healthcare.patient.BaseIntegrationTest
import com.healthcare.patient.dto.AppointmentRequest
import com.healthcare.patient.dto.PatientRequest
import com.healthcare.patient.model.AppointmentStatus
import com.healthcare.patient.repository.AppointmentRepository
import com.healthcare.patient.repository.PatientRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Transactional
class AppointmentServiceTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var appointmentService: AppointmentService

    @Autowired
    private lateinit var patientService: PatientService

    @Autowired
    private lateinit var appointmentRepository: AppointmentRepository

    @Autowired
    private lateinit var patientRepository: PatientRepository

    private lateinit var testPatientId: UUID

    @BeforeEach
    fun setUp() {
        appointmentRepository.deleteAll()
        patientRepository.deleteAll()

        val patientRequest = PatientRequest(
            firstName = "Test",
            lastName = "Patient",
            email = "test.patient@example.com",
            phone = "555-0000",
            dateOfBirth = LocalDate.of(1990, 1, 1)
        )

        val testPatient = patientService.createPatient(patientRequest)
        testPatientId = testPatient.id
    }

    @Test
    fun testCreateAppointment() {
        val request = AppointmentRequest(
            patientId = testPatientId,
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Regular checkup"
        )

        val response = appointmentService.createAppointment(request)

        assertNotNull(response.id)
        assertEquals(testPatientId, response.patientId)
        assertEquals("Regular checkup", response.reason)
        assertEquals(AppointmentStatus.SCHEDULED, response.status)
    }

    @Test
    fun testGetAppointmentById() {
        val request = AppointmentRequest(
            patientId = testPatientId,
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Checkup"
        )

        val created = appointmentService.createAppointment(request)
        val retrieved = appointmentService.getAppointmentById(created.id)

        assertEquals(created.id, retrieved.id)
        assertEquals("Checkup", retrieved.reason)
    }

    @Test
    fun testGetAppointmentsByPatientId() {
        val request1 = AppointmentRequest(
            patientId = testPatientId,
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Checkup"
        )
        val request2 = AppointmentRequest(
            patientId = testPatientId,
            appointmentDateTime = LocalDateTime.of(2024, 12, 20, 14, 0),
            reason = "Follow-up"
        )

        appointmentService.createAppointment(request1)
        appointmentService.createAppointment(request2)

        val appointments = appointmentService.getAppointmentsByPatientId(testPatientId)

        assertEquals(2, appointments.size)
    }

    @Test
    fun testUpdateAppointmentStatus() {
        val request = AppointmentRequest(
            patientId = testPatientId,
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Checkup"
        )

        val created = appointmentService.createAppointment(request)
        val updated = appointmentService.updateAppointmentStatus(created.id, AppointmentStatus.COMPLETED)

        assertEquals(AppointmentStatus.COMPLETED, updated.status)
    }

    @Test
    fun testDeleteAppointment() {
        val request = AppointmentRequest(
            patientId = testPatientId,
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Checkup"
        )

        val created = appointmentService.createAppointment(request)
        val id = created.id

        appointmentService.deleteAppointment(id)

        assertThrows(RuntimeException::class.java) {
            appointmentService.getAppointmentById(id)
        }
    }
}

