package com.healthcare.patient.repository

import com.healthcare.patient.model.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PatientRepository : JpaRepository<Patient, UUID> {
    fun findByEmail(email: String): Patient?
}

