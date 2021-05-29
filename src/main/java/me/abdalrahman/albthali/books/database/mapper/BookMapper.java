package me.abdalrahman.albthali.books.database.mapper;

import me.abdalrahman.albthali.books.entity.BookEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper  implements RowMapper<BookEntity>{

    @Override
    public BookEntity mapRow(ResultSet rs, int i) throws SQLException {
        return new BookEntity(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("author"),
                rs.getString("description"),
                rs.getString("owner"),
                rs.getString("visibility"));
    }
}
