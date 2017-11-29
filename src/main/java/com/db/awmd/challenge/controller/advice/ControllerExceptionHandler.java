package com.db.awmd.challenge.controller.advice;

import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.InvalidAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity onInvalidAmountExceptionHandler(InvalidAmountException iAE){
        return new ResponseEntity<String>(iAE.getMessage(),HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler
    public ResponseEntity onAccountNotFoundException(AccountNotFoundException anE){
        return new ResponseEntity<String>(anE.getMessage(),HttpStatus.PRECONDITION_FAILED);
    }
}
