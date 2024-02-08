package com.devsuperior.DSCatalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {

    @NotBlank(message = "campo requerido")
    @Email(message = "email inválido")
    private String email;

    public EmailDTO() {
    }

    public EmailDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
