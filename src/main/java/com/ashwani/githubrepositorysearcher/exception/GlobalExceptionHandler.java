package com.ashwani.githubrepositorysearcher.exception;

import com.ashwani.githubrepositorysearcher.dto.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValid (
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));
        ExceptionResponseDto responseDto =
                new ExceptionResponseDto(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation Failed",
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        errors
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDto);

    }

    @ExceptionHandler(GithubApiException.class)
    public ResponseEntity<ExceptionResponseDto> handleGithubApiException (
            GithubApiException exception,
            HttpServletRequest request) {
        ExceptionResponseDto responseDto =
                new ExceptionResponseDto(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        exception.getMessage(),
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        new HashMap<>()
                );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(responseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleException (
            Exception exception,
            HttpServletRequest request) {
        ExceptionResponseDto responseDto =
                new ExceptionResponseDto(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Something went wrong, please try again later",
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        new HashMap<>()
                );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentTypeMismatch (
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        ExceptionResponseDto responseDto =
                new ExceptionResponseDto(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid value for request parameter: " + exception.getName(),
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        new HashMap<>()
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDto> handleHttpMessageNotReadable (
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        ExceptionResponseDto responseDto =
                new ExceptionResponseDto(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid request body. Please check the provided values.",
                        LocalDateTime.now(),
                        request.getRequestURI(),
                        new HashMap<>()
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }
}