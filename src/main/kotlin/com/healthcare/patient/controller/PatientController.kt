package com.healthcare.patient.controller

import com.healthcare.patient.dto.PatientRequest
import com.healthcare.patient.dto.PatientResponse
import com.healthcare.patient.service.PatientService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Patient management endpoints")
class PatientController(private val patientService: PatientService) {

    @PostMapping
    @Operation(summary = "Create a new patient", description = "Creates a new patient record in the system")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Patient created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data")
    ])
    fun createPatient(@Valid @RequestBody request: PatientRequest): ResponseEntity<PatientResponse> {
        val response = patientService.createPatient(request)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieves a patient's information by their ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Patient found"),
        ApiResponse(responseCode = "404", description = "Patient not found")
    ])
    fun getPatient(@PathVariable id: Long): ResponseEntity<PatientResponse> {
        val response = patientService.getPatientById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieves a list of all patients in the system")
    @ApiResponse(responseCode = "200", description = "List of patients retrieved successfully")
    fun getAllPatients(): ResponseEntity<List<PatientResponse>> {
        val patients = patientService.getAllPatients()
        return ResponseEntity.ok(patients)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Updates an existing patient's information")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Patient updated successfully"),
        ApiResponse(responseCode = "404", description = "Patient not found"),
        ApiResponse(responseCode = "400", description = "Invalid input data")
    ])
    fun updatePatient(
        @PathVariable id: Long,
        @Valid @RequestBody request: PatientRequest
    ): ResponseEntity<PatientResponse> {
        val response = patientService.updatePatient(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Deletes a patient from the system")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
        ApiResponse(responseCode = "404", description = "Patient not found")
    ])
    fun deletePatient(@PathVariable id: Long): ResponseEntity<Void> {
        patientService.deletePatient(id)
        return ResponseEntity.noContent().build()
    }
}

