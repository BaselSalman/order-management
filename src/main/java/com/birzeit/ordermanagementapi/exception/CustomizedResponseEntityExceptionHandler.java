package com.birzeit.ordermanagementapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler /*extends ResponseEntityExceptionHandler*/{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public final ResponseEntity<ErrorDetails> handleNotFoundException(Exception ex, WebRequest request) throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler({BadCredentialsException.class, IOException.class})
//    public final ResponseEntity<ErrorDetails> invalidUser(Exception ex, WebRequest request) throws Exception {
//        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
//                ex.getMessage(), request.getDescription(false), HttpStatus.FORBIDDEN.value());
//
//        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.FORBIDDEN);
//    }


    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                     WebRequest request) throws Exception {
        Map<String, String> errors = new HashMap<>();
        int i = 1;
        for(FieldError error : ex.getFieldErrors()) {
            errors.put("Error " + (i++), error.getDefaultMessage());
        }

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                errors.toString(), request.getDescription(false), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}