package me.abdalrahman.albthali.books.rest.controller;

import me.abdalrahman.albthali.books.entity.BookEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class BookController {
    private final Log logger = LogFactory.getLog(BookController.class);
    @GetMapping("/books")
    public List<BookEntity> getBooks(Principal user){
        logger.warn(user.getName());
        return Collections.emptyList();
    }
}
