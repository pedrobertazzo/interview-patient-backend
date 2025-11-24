package com.healthcare.patient.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.healthcare.patient.BaseIntegrationTest
import com.healthcare.patient.dto.PatientRequest
import com.healthcare.patient.repository.PatientRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@AutoConfigureMockMvc
@Transactional
class PatientControllerTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var patientRepository: PatientRepository

    @BeforeEach
    fun setUp() {
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

        mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
    }

    @Test
    fun testGetPatient() {
        val request = PatientRequest(
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            phone = "555-5678",
            dateOfBirth = LocalDate.of(1985, 3, 20)
        )

        val createResponse = mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andReturn()
            .response
            .contentAsString

        val id = objectMapper.readTree(createResponse).get("id").asText()

        mockMvc.perform(get("/api/patients/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.firstName").value("Jane"))
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

        mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1))
        )
            .andExpect(status().isCreated)

        mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2))
        )
            .andExpect(status().isCreated)

        mockMvc.perform(get("/api/patients"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
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

        val createResponse = mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andExpect(status().isCreated)
            .andReturn()
            .response
            .contentAsString

        val id = objectMapper.readTree(createResponse).get("id").asText()

        val updateRequest = PatientRequest(
            firstName = "Charles",
            lastName = "Brown",
            email = "charles@example.com",
            phone = "555-9999",
            dateOfBirth = LocalDate.of(1992, 12, 25)
        )

        mockMvc.perform(
            put("/api/patients/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value("Charles"))
            .andExpect(jsonPath("$.email").value("charles@example.com"))
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

        val createResponse = mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andReturn()
            .response
            .contentAsString

        val id = objectMapper.readTree(createResponse).get("id").asText()

        mockMvc.perform(delete("/api/patients/$id"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/patients/$id"))
            .andExpect(status().isNotFound)
    }
}

