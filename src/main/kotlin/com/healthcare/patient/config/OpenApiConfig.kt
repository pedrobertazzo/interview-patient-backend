package com.healthcare.patient.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun patientManagementAPI(): OpenAPI {
        val localServer = Server().apply {
            url = "http://localhost:8080"
            description = "Local Development Server"
        }

        val contact = Contact().apply {
            name = "Healthcare API"
            email = "support@healthcare.com"
        }

        val info = Info()
            .title("Patient Management API")
            .version("1.0.0")
            .description("REST API for managing patient information and appointments")
            .contact(contact)

        return OpenAPI()
            .info(info)
            .servers(listOf(localServer))
    }
}

