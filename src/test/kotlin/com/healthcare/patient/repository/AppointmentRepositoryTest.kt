package com.healthcare.patient.repository

import com.healthcare.patient.BaseIntegrationTest
import com.healthcare.patient.model.Appointment
import com.healthcare.patient.model.AppointmentStatus
import com.healthcare.patient.model.Patient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Transactional
class AppointmentRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var appointmentRepository: AppointmentRepository

    @Autowired
    private lateinit var patientRepository: PatientRepository

    private lateinit var testPatient: Patient

    @BeforeEach
    fun setUp() {
        appointmentRepository.deleteAll()
        patientRepository.deleteAll()

        testPatient = Patient(
            firstName = "Test",
            lastName = "Patient",
            email = "test.patient@example.com",
            phone = "555-0000",
            dateOfBirth = LocalDate.of(1990, 1, 1)
        )
        testPatient = patientRepository.save(testPatient)
    }

    @Test
    fun testSaveAndFindAppointment() {
        val appointment = Appointment(
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Regular checkup",
            patient = testPatient
        )

        val saved = appointmentRepository.save(appointment)

        assertNotNull(saved.id)
        assertEquals("Regular checkup", saved.reason)
        assertEquals(AppointmentStatus.SCHEDULED, saved.status)
        assertEquals(testPatient.id, saved.patient.id)
    }

    @Test
    fun testFindByPatientId() {
        val appointment1 = Appointment(
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Checkup",
            patient = testPatient
        )
        val appointment2 = Appointment(
            appointmentDateTime = LocalDateTime.of(2024, 12, 20, 14, 0),
            reason = "Follow-up",
            patient = testPatient
        )

        appointmentRepository.save(appointment1)
        appointmentRepository.save(appointment2)

        val appointments = appointmentRepository.findByPatientId(testPatient.id!!)

        assertEquals(2, appointments.size)
    }

    @Test
    fun testFindByStatus() {
        val appointment1 = Appointment(
            appointmentDateTime = LocalDateTime.of(2024, 12, 15, 10, 0),
            reason = "Checkup",
            patient = testPatient,
            status = AppointmentStatus.COMPLETED
        )
        val appointment2 = Appointment(
            appointmentDateTime = LocalDateTime.of(2024, 12, 20, 14, 0),
            reason = "Follow-up",
            patient = testPatient
        )

        appointmentRepository.save(appointment1)
        appointmentRepository.save(appointment2)

        val completedAppointments = appointmentRepository.findByStatus(AppointmentStatus.COMPLETED)
        val scheduledAppointments = appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED)

        assertEquals(1, completedAppointments.size)
        assertEquals(1, scheduledAppointments.size)
    }

    @Test
    fun testFindByAppointmentDateTimeBetween() {
        val appointment1 = Appointment(
            appointmentDateTime = LocalDateTime.of(2024, 12, 10, 10, 0),
            reason = "Early appointment",
            patient = testPatient
        )
        val appointment2 = Appointment(
            appointmentDateTime = LocalDateTime.of(2024, 12, 20, 14, 0),
            reason = "Late appointment",
            patient = testPatient
        )

        appointmentRepository.save(appointment1)
        appointmentRepository.save(appointment2)

        val start = LocalDateTime.of(2024, 12, 1, 0, 0)
        val end = LocalDateTime.of(2024, 12, 15, 23, 59)

        val appointments = appointmentRepository.findByAppointmentDateTimeBetween(start, end)

        assertEquals(1, appointments.size)
        assertEquals("Early appointment", appointments[0].reason)
    }
}

