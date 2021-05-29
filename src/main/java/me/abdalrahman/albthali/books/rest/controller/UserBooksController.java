package me.abdalrahman.albthali.books.rest.controller;

import me.abdalrahman.albthali.books.entity.BookEntity;
import me.abdalrahman.albthali.books.model.BookModel;
import me.abdalrahman.albthali.books.schema.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
@RestController
public class UserBooksController {
    @Autowired
    private BookModel bookModel;
    @GetMapping("/user/{username}/books")
    public List<BookEntity> getUserBooks(Principal principal,  @PathVariable String username){
        if(!principal.getName().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect username");
        }
       return bookModel.getUserBooks(principal.getName());
    }
    @PostMapping("/user/{username}/books")
    public BookEntity postBook(Principal principal, @PathVariable String username, @RequestBody BookRequest bookRequest){
        if(!principal.getName().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect username");
        }
        return bookModel.insertBook(bookRequest,principal.getName());
    }
}
