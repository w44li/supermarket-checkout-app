package com.supermarket.checkout.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 * Converts common exceptions into HTTP error responses.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, List<String>> validationErrors = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors
                    .computeIfAbsent(error.getField(), key -> new java.util.ArrayList<>())
                    .add(error.getDefaultMessage());
        }

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more request fields are invalid."
        );

        problem.setProperty("errors", validationErrors);
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidRequestBody(HttpMessageNotReadableException ex) {
        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Malformed request body",
                "The request body is missing, invalid, or malformed JSON."
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Invalid parameter type",
                "Request parameter '%s' has an invalid value '%s'."
                        .formatted(ex.getName(), ex.getValue())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "Constraint violation",
                "One or more request parameters are invalid."
        );

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                ex.getMessage()
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class, DataAccessException.class, DbActionExecutionException.class})
    public ProblemDetail handleDataIntegrityViolation(Exception ex) {
        String causeMessage = extractRootCauseMessage(ex).toLowerCase();

        if (causeMessage.contains("uniq_offer_period") || causeMessage.contains("unique")) {
            return buildProblem(
                    HttpStatus.CONFLICT,
                    "Duplicate offer",
                    "An offer already exists for this product and date range."
            );
        }

        if (causeMessage.contains("fk_offers_product") || causeMessage.contains("foreign key")) {
            return buildProblem(
                    HttpStatus.BAD_REQUEST,
                    "Invalid product reference",
                    "The provided productId does not exist."
            );
        }

        if (causeMessage.contains("chk_offer_dates") || causeMessage.contains("check constraint")) {
            return buildProblem(
                    HttpStatus.BAD_REQUEST,
                    "Invalid offer dates",
                    "endDate must be on or after startDate."
            );
        }

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Data integrity violation",
                "The request violates a database constraint."
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {

        log.error("Unexpected error occurred", ex);

        return buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred."
        );
    }

    private ProblemDetail buildProblem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        return problem;
    }

    private String extractRootCauseMessage(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root.getMessage() == null ? "" : root.getMessage();
    }
}
