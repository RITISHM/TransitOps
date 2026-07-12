package com.transitops.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to create a resource that would violate
 * a uniqueness constraint (e.g., duplicate registration number, email, or license).
 *
 * <p>Mapped to HTTP 409 Conflict by {@link GlobalExceptionHandler} so that clients
 * receive a friendly error message instead of a raw SQL constraint violation trace.</p>
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     *
     * @param message description of which resource field is duplicated
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
