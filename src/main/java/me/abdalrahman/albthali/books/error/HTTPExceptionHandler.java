package me.abdalrahman.albthali.books.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public class HTTPExceptionHandler {
    final static private Log logger = LogFactory.getLog(HTTPExceptionHandler.class);
    public static Map<String,String> handleArgumentException(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;

    }
    public static Map<String,String> handleStatusException(ResponseStatusException ex){

        Map<String, String> errors = new HashMap<>();
        errors.put("reason",ex.getReason());
        errors.put("message",ex.getMessage());
        errors.put("status",String.valueOf(ex.getRawStatusCode()));

        return errors;

    }
    public static Map<String,String> handleInternalErrorException(ResponseStatusException ex){
        HTTPExceptionHandler.logger.error(ex);
        Map<String, String> errors = new HashMap<>();
        errors.put("reason",ex.getReason());
        errors.put("message",ex.getMessage());
        errors.put("status",String.valueOf(ex.getRawStatusCode()));

        return errors;

    }
}
