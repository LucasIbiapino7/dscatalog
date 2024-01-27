package com.devsuperior.DSCatalog.controllers.exceptions;

import com.devsuperior.DSCatalog.services.exception.DatabaseException;
import com.devsuperior.DSCatalog.services.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> ResourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        CustomErrorDTO err = new CustomErrorDTO();
        HttpStatus status = HttpStatus.NOT_FOUND;
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomErrorDTO> Database(DatabaseException e, HttpServletRequest request){
        CustomErrorDTO err = new CustomErrorDTO();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}
