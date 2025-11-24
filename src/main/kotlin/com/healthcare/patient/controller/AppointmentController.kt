package com.healthcare.patient.controller

import com.healthcare.patient.dto.AppointmentRequest
import com.healthcare.patient.dto.AppointmentResponse
import com.healthcare.patient.model.AppointmentStatus
import com.healthcare.patient.service.AppointmentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Appointment management endpoints")
class AppointmentController(private val appointmentService: AppointmentService) {

    @PostMapping
    @Operation(summary = "Create a new appointment", description = "Creates a new appointment for a patient")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Appointment created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input data"),
        ApiResponse(responseCode = "404", description = "Patient not found")
    ])
    fun createAppointment(@Valid @RequestBody request: AppointmentRequest): ResponseEntity<AppointmentResponse> {
        val response = appointmentService.createAppointment(request)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieves an appointment by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Appointment found"),
        ApiResponse(responseCode = "404", description = "Appointment not found")
    ])
    fun getAppointment(@PathVariable id: Long): ResponseEntity<AppointmentResponse> {
        val response = appointmentService.getAppointmentById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieves all appointments, optionally filtered by patient")
    @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully")
    fun getAllAppointments(@RequestParam(required = false) patientId: Long?): ResponseEntity<List<AppointmentResponse>> {
        val appointments = if (patientId != null) {
            appointmentService.getAppointmentsByPatientId(patientId)
        } else {
            appointmentService.getAllAppointments()
        }
        return ResponseEntity.ok(appointments)
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update appointment status", description = "Updates the status of an appointment")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Appointment status updated successfully"),
        ApiResponse(responseCode = "404", description = "Appointment not found")
    ])
    fun updateAppointmentStatus(
        @PathVariable id: Long,
        @RequestParam status: AppointmentStatus
    ): ResponseEntity<AppointmentResponse> {
        val response = appointmentService.updateAppointmentStatus(id, status)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete appointment", description = "Deletes an appointment from the system")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Appointment deleted successfully"),
        ApiResponse(responseCode = "404", description = "Appointment not found")
    ])
    fun deleteAppointment(@PathVariable id: Long): ResponseEntity<Void> {
        appointmentService.deleteAppointment(id)
        return ResponseEntity.noContent().build()
    }
}

