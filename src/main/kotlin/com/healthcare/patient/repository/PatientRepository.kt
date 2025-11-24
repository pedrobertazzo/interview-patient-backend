package com.healthcare.patient.repository

import com.healthcare.patient.model.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PatientRepository : JpaRepository<Patient, Long> {
    fun findByEmail(email: String): Patient?
}

