package com.healthcare.patient.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "appointments")
class Appointment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotNull
    var appointmentDateTime: LocalDateTime,

    @NotBlank
    var reason: String,

    @Enumerated(EnumType.STRING)
    var status: AppointmentStatus = AppointmentStatus.SCHEDULED,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    var patient: Patient
)

