package com.devsuperior.DSCatalog.controllers;

import com.devsuperior.DSCatalog.dto.*;
import com.devsuperior.DSCatalog.services.AuthService;
import com.devsuperior.DSCatalog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken(@RequestBody @Valid EmailDTO body){
        service.createRecoverToken(body);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/new-password")
    public ResponseEntity<Void> saveNewPassword(@RequestBody @Valid NewPasswordDto body){
        service.saveNewPassword(body);
        return ResponseEntity.noContent().build();
    }
}
