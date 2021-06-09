package me.abdalrahman.albthali.books.model;

import me.abdalrahman.albthali.books.database.SpringJDBCConfiguration;
import me.abdalrahman.albthali.books.database.mapper.BookMapper;
import me.abdalrahman.albthali.books.entity.BookEntity;
import me.abdalrahman.albthali.books.schema.BookRequest;
import me.abdalrahman.albthali.books.schema.PatchBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Service
public class BookModel {
    @Value("${sql.book.new}")
    private String newBook;
    @Value("${sql.book.getByUsername}")
    private String userBooks;
    @Value("${sql.book.patchVisibility}")
    private String patchBook;
    @Value("${sql.book.getUserBookById}")
    private String getUserBookById;
    @Value("${sql.book.getAll}")
    private String getAllBooks;
    @Value("${sql.book.getByUsernamePublic}")
    private String getByUsernamePublic;
    @Value("${sql.book.queryBooks}")
    private String queryBooks;
    @Value("${sql.book.delete}")
    private String deleteBook;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public BookModel(SpringJDBCConfiguration springJDBCConfiguration){
        this.jdbcTemplate = new JdbcTemplate(springJDBCConfiguration.mysqlDataSource());
    }
    public BookEntity insertBook(BookRequest bookRequest, String username){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(newBook, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,bookRequest.getName());
            ps.setString(2,bookRequest.getAuthor());
            ps.setString(3,bookRequest.getDescription());
            ps.setString(4,username);
            ps.setString(5,bookRequest.getVisibility());
            return ps;
        },keyHolder);
        return new BookEntity(keyHolder.getKey().intValue(), username, bookRequest);

    }
    public List<BookEntity> getUserBooks(String username){
        Object[] params = {username};
        return jdbcTemplate.query(userBooks,new BookMapper(),params);
    }
    public BookEntity patchBook(int id, String username, PatchBookRequest patchBookRequest, BookEntity bookEntity){
        Object[] params = {patchBookRequest.getVisibility(),id, username};
        int insert = jdbcTemplate.update(patchBook,params);
        bookEntity.setVisibility(patchBookRequest.getVisibility());
        return bookEntity;
    }
    public int deleteBook(int id, String username){
        Object[] params = {id,username};
        return jdbcTemplate.update(deleteBook,params);

    }
    public BookEntity getUserBook(int id, String username){
        Object[] params = {username,id};
        List<BookEntity>  books = jdbcTemplate.query(getUserBookById,new BookMapper(),params);
        return books.isEmpty() ? null : books.get(0);
    }
    public List<BookEntity> getAllBooks(){
        return jdbcTemplate.query(getAllBooks,new BookMapper());
    }
    public List<BookEntity> queryBooks(String query ) {
        return jdbcTemplate.query(queryBooks,new BookMapper(), query,query,query);
    }
    public List<BookEntity> getByUsernamePublic(String username){
        Object[] params = {username};
        return jdbcTemplate.query(getByUsernamePublic,new BookMapper(),params);
    }

}
