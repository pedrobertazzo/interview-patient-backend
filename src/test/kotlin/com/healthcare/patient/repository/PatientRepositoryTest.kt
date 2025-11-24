package com.healthcare.patient.repository

import com.healthcare.patient.BaseIntegrationTest
import com.healthcare.patient.model.Patient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
class PatientRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var patientRepository: PatientRepository

    @BeforeEach
    fun setUp() {
        patientRepository.deleteAll()
    }

    @Test
    fun testSaveAndFindPatient() {
        val patient = Patient(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            phone = "555-1234",
            dateOfBirth = LocalDate.of(1990, 5, 15)
        )

        val saved = patientRepository.save(patient)

        assertNotNull(saved.id)
        assertEquals("John", saved.firstName)
        assertEquals("Doe", saved.lastName)
        assertEquals("john.doe@example.com", saved.email)
    }

    @Test
    fun testFindByEmail() {
        val patient = Patient(
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            phone = "555-5678",
            dateOfBirth = LocalDate.of(1985, 3, 20)
        )

        patientRepository.save(patient)

        val found = patientRepository.findByEmail("jane.smith@example.com")

        assertNotNull(found)
        assertEquals("Jane", found?.firstName)
    }

    @Test
    fun testFindAllPatients() {
        val patient1 = Patient(
            firstName = "Alice",
            lastName = "Johnson",
            email = "alice@example.com",
            phone = "555-1111",
            dateOfBirth = LocalDate.of(1995, 1, 1)
        )
        val patient2 = Patient(
            firstName = "Bob",
            lastName = "Williams",
            email = "bob@example.com",
            phone = "555-2222",
            dateOfBirth = LocalDate.of(1988, 6, 10)
        )

        patientRepository.save(patient1)
        patientRepository.save(patient2)

        val patients = patientRepository.findAll()

        assertEquals(2, patients.size)
    }

    @Test
    fun testDeletePatient() {
        val patient = Patient(
            firstName = "Charlie",
            lastName = "Brown",
            email = "charlie@example.com",
            phone = "555-3333",
            dateOfBirth = LocalDate.of(1992, 12, 25)
        )
        val saved = patientRepository.save(patient)
        val id = saved.id!!

        patientRepository.deleteById(id)

        val found = patientRepository.findById(id)
        assertFalse(found.isPresent)
    }
}

