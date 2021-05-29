package me.abdalrahman.albthali.books.model;

import me.abdalrahman.albthali.books.database.SpringJDBCConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
@Service
public class UserModel {
    @Value("${sql.user.getUsername}")
    private String getUserByUsername;
    @Value("${sql.user.new}")
    private String newUser;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    Logger logger = LoggerFactory.getLogger(UserModel.class);
    @Autowired
    public UserModel(SpringJDBCConfiguration jdbcConnection) {
        this.jdbcTemplate = new JdbcTemplate(jdbcConnection.mysqlDataSource());
    }
    public UserDetails getUserByUsername(String username){
        try{

            Map<String, Object> userQuery = jdbcTemplate.queryForMap(getUserByUsername, username);
            return new User((String)userQuery.get("username") ,(String)userQuery.get("password"), Collections.emptyList());
        } catch (Exception e ){
            System.out.println(e);
            throw new UsernameNotFoundException(String.format("Username:%s does not exist",username));
        }

    }
    public int insertUser(String username, String password){
        return insertUser(username,passwordEncoder.encode(password),true);
    }
    public int insertUser(String username, String password, boolean enabled){

        return jdbcTemplate.update(newUser,username,password,enabled);
    }
}
