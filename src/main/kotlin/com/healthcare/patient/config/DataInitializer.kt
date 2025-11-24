package com.healthcare.patient.config

import com.healthcare.patient.model.Appointment
import com.healthcare.patient.model.AppointmentStatus
import com.healthcare.patient.model.Patient
import com.healthcare.patient.repository.PatientRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DataInitializer {

    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    @Bean
    fun initDatabase(patientRepository: PatientRepository) = CommandLineRunner {
        // Check if data already exists
        if (patientRepository.count() > 0) {
            logger.info("Database already contains data. Skipping initialization.")
            return@CommandLineRunner
        }

        logger.info("Initializing database with sample data...")

        // Create 10 patients with realistic names
        val patients = listOf(
            Patient(
                firstName = "Emma",
                lastName = "Johnson",
                email = "emma.johnson@email.com",
                phone = "555-0101",
                dateOfBirth = LocalDate.of(1985, 3, 15)
            ),
            Patient(
                firstName = "Michael",
                lastName = "Williams",
                email = "michael.williams@email.com",
                phone = "555-0102",
                dateOfBirth = LocalDate.of(1978, 7, 22)
            ),
            Patient(
                firstName = "Sophia",
                lastName = "Brown",
                email = "sophia.brown@email.com",
                phone = "555-0103",
                dateOfBirth = LocalDate.of(1992, 11, 8)
            ),
            Patient(
                firstName = "James",
                lastName = "Davis",
                email = "james.davis@email.com",
                phone = "555-0104",
                dateOfBirth = LocalDate.of(1965, 5, 30)
            ),
            Patient(
                firstName = "Olivia",
                lastName = "Miller",
                email = "olivia.miller@email.com",
                phone = "555-0105",
                dateOfBirth = LocalDate.of(1988, 9, 12)
            ),
            Patient(
                firstName = "William",
                lastName = "Wilson",
                email = "william.wilson@email.com",
                phone = "555-0106",
                dateOfBirth = LocalDate.of(1970, 2, 18)
            ),
            Patient(
                firstName = "Ava",
                lastName = "Moore",
                email = "ava.moore@email.com",
                phone = "555-0107",
                dateOfBirth = LocalDate.of(1995, 6, 25)
            ),
            Patient(
                firstName = "Benjamin",
                lastName = "Taylor",
                email = "benjamin.taylor@email.com",
                phone = "555-0108",
                dateOfBirth = LocalDate.of(1982, 12, 3)
            ),
            Patient(
                firstName = "Isabella",
                lastName = "Anderson",
                email = "isabella.anderson@email.com",
                phone = "555-0109",
                dateOfBirth = LocalDate.of(1990, 4, 17)
            ),
            Patient(
                firstName = "Lucas",
                lastName = "Thomas",
                email = "lucas.thomas@email.com",
                phone = "555-0110",
                dateOfBirth = LocalDate.of(1975, 8, 28)
            )
        )

        val savedPatients = patientRepository.saveAll(patients)
        logger.info("Created ${savedPatients.size} patients")

        // Create 20 appointments with realistic reasons
        val appointmentReasons = listOf(
            "Annual physical examination",
            "Follow-up consultation",
            "Blood pressure check",
            "Diabetes management",
            "Flu vaccination",
            "General health check-up",
            "Chronic pain management",
            "Skin rash evaluation",
            "Respiratory infection",
            "Allergy consultation",
            "Prescription refill",
            "Mental health consultation",
            "Cardiovascular screening",
            "Post-surgery follow-up",
            "Nutrition counseling",
            "Physical therapy session",
            "Lab results review",
            "Medication adjustment",
            "Preventive care visit",
            "Minor injury treatment"
        )

        val appointmentStatuses = listOf(
            AppointmentStatus.SCHEDULED,
            AppointmentStatus.COMPLETED,
            AppointmentStatus.CANCELLED,
            AppointmentStatus.NO_SHOW
        )

        val now = LocalDateTime.now()
        val appointments = mutableListOf<Appointment>()

        // Create appointments distributed among patients
        for (i in 0 until 20) {
            val patient = savedPatients[i % savedPatients.size]

            // Mix of past, current, and future appointments
            val daysOffset = when {
                i < 7 -> -(1..30).random().toLong() // Past appointments
                i < 14 -> (1..30).random().toLong() // Future appointments
                else -> (31..90).random().toLong() // Farther future appointments
            }

            val appointmentDateTime = now.plusDays(daysOffset).withHour((9..17).random()).withMinute(listOf(0, 15, 30, 45).random())

            // Past appointments have varied statuses, future ones are mostly scheduled
            val status = if (daysOffset < 0) {
                appointmentStatuses[(0..3).random()]
            } else {
                AppointmentStatus.SCHEDULED
            }

            appointments.add(
                Appointment(
                    appointmentDateTime = appointmentDateTime,
                    reason = appointmentReasons[i],
                    status = status,
                    patient = patient
                )
            )
        }

        // Add appointments to patients
        appointments.forEach { appointment ->
            appointment.patient.appointments.add(appointment)
        }

        patientRepository.saveAll(savedPatients)
        logger.info("Created ${appointments.size} appointments")
        logger.info("Database initialization completed successfully!")
    }
}

