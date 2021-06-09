package me.abdalrahman.albthali.books.rest.controller;

import me.abdalrahman.albthali.books.error.HTTPExceptionHandler;
import me.abdalrahman.albthali.books.error.InternalErrorException;
import me.abdalrahman.albthali.books.model.UserModel;
import me.abdalrahman.albthali.books.schema.AuthenticateRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserModel userModel;
    @PostMapping("/users")
    public ResponseEntity<?> newUser(@RequestBody  AuthenticateRequest request){
        try {
            userModel.getUserByUsername(request.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT,String.format("User:%s already exists",request.getUsername()));

        } catch (UsernameNotFoundException e) {
            // if user doesnot exist do nothing
        }
        try {

            int insert = userModel.insertUser(request.getUsername(),request.getPassword());
            System.out.println("insert:"+insert);
            return ResponseEntity.status(201).build();

        } catch (Exception e){
            throw new InternalErrorException(500,"Internal Server Error",e);

        }
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalErrorException.class)
    public Map<String, String> handleInternalServerError(
            ResponseStatusException ex) {
        return HTTPExceptionHandler.handleStatusException(ex);
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResponseStatusException.class)
    public Map<String, String> handleResponseStatusException(
            ResponseStatusException ex) {
        return HTTPExceptionHandler.handleStatusException(ex);
    }
}
