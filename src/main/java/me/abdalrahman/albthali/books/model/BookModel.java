package me.abdalrahman.albthali.books.model;

import me.abdalrahman.albthali.books.database.SpringJDBCConfiguration;
import me.abdalrahman.albthali.books.database.mapper.BookMapper;
import me.abdalrahman.albthali.books.entity.BookEntity;
import me.abdalrahman.albthali.books.schema.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookModel {
    @Value("${sql.book.new}")
    private String newBook;
    @Value("${sql.book.getByUsername}")
    private String userBooks;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public BookModel(SpringJDBCConfiguration springJDBCConfiguration){
        this.jdbcTemplate = new JdbcTemplate(springJDBCConfiguration.mysqlDataSource());
    }
    public BookEntity insertBook(BookRequest bookRequest, String username){
        int id = jdbcTemplate.update(newBook,bookRequest.getName(),bookRequest.getAuthor(),bookRequest.getDescription(),username,bookRequest.getVisibility());
        return new BookEntity(id, username, bookRequest);

    }
    public List<BookEntity> getUserBooks(String username){
        Object[] params = {username};
        return jdbcTemplate.query(userBooks,new BookMapper(),params);
    }
}
