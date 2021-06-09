package me.abdalrahman.albthali.books.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalErrorException extends ResponseStatusException {
    public InternalErrorException(HttpStatus status) {
        super(status);
    }

    public InternalErrorException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public InternalErrorException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public InternalErrorException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
