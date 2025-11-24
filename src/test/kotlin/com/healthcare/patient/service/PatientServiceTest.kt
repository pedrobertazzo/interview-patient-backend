package com.healthcare.patient.service

import com.healthcare.patient.BaseIntegrationTest
import com.healthcare.patient.dto.PatientRequest
import com.healthcare.patient.exception.ResourceNotFoundException
import com.healthcare.patient.repository.AppointmentRepository
import com.healthcare.patient.repository.PatientRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
class PatientServiceTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var patientService: PatientService

    @Autowired
    private lateinit var patientRepository: PatientRepository

    @Autowired
    private lateinit var appointmentRepository: AppointmentRepository

    @BeforeEach
    fun setUp() {
        appointmentRepository.deleteAll()
        patientRepository.deleteAll()
    }

    @Test
    fun testCreatePatient() {
        val request = PatientRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            phone = "555-1234",
            dateOfBirth = LocalDate.of(1990, 5, 15)
        )

        val response = patientService.createPatient(request)

        assertNotNull(response.id)
        assertEquals("John", response.firstName)
        assertEquals("Doe", response.lastName)
        assertEquals("john.doe@example.com", response.email)
    }

    @Test
    fun testGetPatientById() {
        val request = PatientRequest(
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            phone = "555-5678",
            dateOfBirth = LocalDate.of(1985, 3, 20)
        )

        val created = patientService.createPatient(request)
        val retrieved = patientService.getPatientById(created.id)

        assertEquals(created.id, retrieved.id)
        assertEquals("Jane", retrieved.firstName)
    }

    @Test
    fun testGetAllPatients() {
        val request1 = PatientRequest(
            firstName = "Alice",
            lastName = "Johnson",
            email = "alice@example.com",
            phone = "555-1111",
            dateOfBirth = LocalDate.of(1995, 1, 1)
        )
        val request2 = PatientRequest(
            firstName = "Bob",
            lastName = "Williams",
            email = "bob@example.com",
            phone = "555-2222",
            dateOfBirth = LocalDate.of(1988, 6, 10)
        )

        patientService.createPatient(request1)
        patientService.createPatient(request2)

        val patients = patientService.getAllPatients()

        assertEquals(2, patients.size)
    }

    @Test
    fun testUpdatePatient() {
        val createRequest = PatientRequest(
            firstName = "Charlie",
            lastName = "Brown",
            email = "charlie@example.com",
            phone = "555-3333",
            dateOfBirth = LocalDate.of(1992, 12, 25)
        )

        val created = patientService.createPatient(createRequest)

        val updateRequest = PatientRequest(
            firstName = "Charles",
            lastName = "Brown",
            email = "charles@example.com",
            phone = "555-9999",
            dateOfBirth = LocalDate.of(1992, 12, 25)
        )

        val updated = patientService.updatePatient(created.id, updateRequest)

        assertEquals("Charles", updated.firstName)
        assertEquals("charles@example.com", updated.email)
        assertEquals("555-9999", updated.phone)
    }

    @Test
    fun testDeletePatient() {
        val request = PatientRequest(
            firstName = "David",
            lastName = "Wilson",
            email = "david@example.com",
            phone = "555-4444",
            dateOfBirth = LocalDate.of(1980, 8, 8)
        )

        val created = patientService.createPatient(request)
        val id = created.id

        patientService.deletePatient(id)

        assertThrows(ResourceNotFoundException::class.java) {
            patientService.getPatientById(id)
        }
    }
}

