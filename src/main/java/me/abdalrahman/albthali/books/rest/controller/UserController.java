package me.abdalrahman.albthali.books.rest.controller;

import me.abdalrahman.albthali.books.model.UserModel;
import me.abdalrahman.albthali.books.schema.AuthenticateRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
public class UserController {
    @Autowired
    private UserModel userModel;
    @PostMapping("/user")
    public ResponseEntity<?> newUser(@RequestBody  AuthenticateRequest request){
        try {
            int insert = userModel.insertUser(request.getUsername(),request.getPassword());
            System.out.println("insert:"+insert);
            return ResponseEntity.status(201).build();

        } catch (Exception e){
            throw new ResponseStatusException(500,"Internal Server Error",e);

        }
    }
}
