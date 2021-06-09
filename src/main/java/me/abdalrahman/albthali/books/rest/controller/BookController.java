package me.abdalrahman.albthali.books.rest.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import me.abdalrahman.albthali.books.entity.BookEntity;
import me.abdalrahman.albthali.books.model.BookModel;
import me.abdalrahman.albthali.books.schema.PDFResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.Principal;
import java.util.*;

@RestController
public class BookController {
    private final Log logger = LogFactory.getLog(BookController.class);
    @Autowired
    private BookModel bookModel;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @GetMapping("/books")
    public List<BookEntity> getBooks(@RequestParam(required = false, name = "query") String query ){

        return getBooksQuery(query);
    }
    @GetMapping("/books/pdf")
    public PDFResponse getBooksPDF(@RequestParam(required = false, name = "query") String query ){

        List<BookEntity> books = getBooksQuery(query);
        StringWriter writer = new StringWriter();
        Context context = new Context();
        context.setVariable("books", books);
        templateEngine.process("books", context, writer);
        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(writer.toString(),pdf);
        return new PDFResponse(Base64.getEncoder().encodeToString(pdf.toByteArray()));
    }
    private List<BookEntity> getBooksQuery(String query){
        if(query != null) return bookModel.queryBooks(query);
        return bookModel.getAllBooks();
    }
    @GetMapping("/books/users/{username}")
    public List<BookEntity> getByUsername(@PathVariable String username){
       return bookModel.getByUsernamePublic(username);
    }
}
