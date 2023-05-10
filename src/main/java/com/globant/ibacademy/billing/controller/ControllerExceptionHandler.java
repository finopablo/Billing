package com.globant.ibacademy.billing.controller;

import com.globant.ibacademy.billing.exceptions.EntityAlreadyExistsException;
import com.globant.ibacademy.billing.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFound(EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity handleEntityExits(EntityAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity handleBadRequestValidation(ConstraintViolationException ex) {
       return ResponseEntity.badRequest().body(ex.getConstraintViolations());
   }

}
