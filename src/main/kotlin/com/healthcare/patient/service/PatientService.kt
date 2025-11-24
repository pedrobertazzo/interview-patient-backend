package com.healthcare.patient.service

import com.healthcare.patient.dto.PatientRequest
import com.healthcare.patient.dto.PatientResponse
import com.healthcare.patient.model.Patient
import com.healthcare.patient.repository.PatientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PatientService(private val patientRepository: PatientRepository) {

    @Transactional
    fun createPatient(request: PatientRequest): PatientResponse {
        val patient = Patient(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            phone = request.phone,
            dateOfBirth = request.dateOfBirth
        )
        val saved = patientRepository.save(patient)
        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun getPatientById(id: Long): PatientResponse {
        val patient = patientRepository.findById(id)
            .orElseThrow { RuntimeException("Patient not found with id: $id") }
        return patient.toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllPatients(): List<PatientResponse> {
        return patientRepository.findAll().map { it.toResponse() }
    }

    @Transactional
    fun updatePatient(id: Long, request: PatientRequest): PatientResponse {
        val patient = patientRepository.findById(id)
            .orElseThrow { RuntimeException("Patient not found with id: $id") }

        patient.firstName = request.firstName
        patient.lastName = request.lastName
        patient.email = request.email
        patient.phone = request.phone
        patient.dateOfBirth = request.dateOfBirth

        val updated = patientRepository.save(patient)
        return updated.toResponse()
    }

    @Transactional
    fun deletePatient(id: Long) {
        if (!patientRepository.existsById(id)) {
            throw RuntimeException("Patient not found with id: $id")
        }
        patientRepository.deleteById(id)
    }

    private fun Patient.toResponse() = PatientResponse(
        id = id!!,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        dateOfBirth = dateOfBirth
    )
}

