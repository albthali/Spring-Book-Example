package me.abdalrahman.albthali.books.rest.controller;

import me.abdalrahman.albthali.books.entity.BookEntity;
import me.abdalrahman.albthali.books.error.HTTPExceptionHandler;
import me.abdalrahman.albthali.books.error.InternalErrorException;
import me.abdalrahman.albthali.books.model.BookModel;
import me.abdalrahman.albthali.books.schema.BookRequest;
import me.abdalrahman.albthali.books.schema.PatchBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class UserBooksController {
    @Autowired
    private BookModel bookModel;
    @GetMapping("/users/{username}/books")
    public List<BookEntity> getUserBooks(Principal principal,  @PathVariable String username){
        if(!principal.getName().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect username");
        }
       return bookModel.getUserBooks(principal.getName());
    }
    @PostMapping("/users/{username}/books")
    public BookEntity postBook(Principal principal, @PathVariable String username, @Valid @RequestBody BookRequest bookRequest){
        if(!principal.getName().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect username");
        }
        return bookModel.insertBook(bookRequest,principal.getName());
    }
    @PatchMapping("/users/{username}/books/{bookId}")
    public BookEntity patchBook(Principal principal, @PathVariable String username, @PathVariable int bookId, @Valid @RequestBody PatchBookRequest bookRequest){
        if(!principal.getName().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect username");
        }
        BookEntity bookEntity = bookModel.getUserBook(bookId,username);
        if(bookEntity == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,String.format("User:%s does not own book:%d",username,bookId));
        }
        return bookModel.patchBook(bookId,username,bookRequest,bookEntity);
    }
    @DeleteMapping("/users/{username}/books/{bookId}")
    public ResponseEntity<?> deleteBook(Principal principal, @PathVariable String username, @PathVariable int bookId, @Valid @RequestBody PatchBookRequest bookRequest){
        if(!principal.getName().equals(username)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect username");
        }
        BookEntity bookEntity = bookModel.getUserBook(bookId,username);
        if(bookEntity == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,String.format("User:%s does not own book:%d",username,bookId));
        }
        int delete = bookModel.deleteBook(bookId, principal.getName());
        if(delete != 1) throw new InternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("Failed to delete book:%d. Delete status:%d",bookId,delete));
        return ResponseEntity.ok().build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return HTTPExceptionHandler.handleArgumentException(ex);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ResponseStatusException.class)
    public Map<String, String> handleResponseStatusExceptions(
            ResponseStatusException ex) {
        return HTTPExceptionHandler.handleStatusException(ex);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalErrorException.class)
    public Map<String, String> handleInternalServerError(
            ResponseStatusException ex) {
        return HTTPExceptionHandler.handleStatusException(ex);
    }
}
