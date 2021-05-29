package me.abdalrahman.albthali.books.security;

import me.abdalrahman.albthali.books.database.SpringJDBCConfiguration;
import me.abdalrahman.albthali.books.filters.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private BooksUserDetailsService booksUserDetailsService;

    private final PasswordEncoder passwordEncoder;
    private final SpringJDBCConfiguration springJDBCConfiguration;

    final private JWTRequestFilter jwtRequestFilter;
    @Autowired
    public SecurityConfigurer(PasswordEncoder passwordEncoder, SpringJDBCConfiguration springJDBCConfiguration, JWTRequestFilter jwtRequestFilter) {
        this.passwordEncoder = passwordEncoder;
        this.springJDBCConfiguration = springJDBCConfiguration;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(booksUserDetailsService);
        auth.jdbcAuthentication().dataSource(springJDBCConfiguration.mysqlDataSource());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/auth","/user")
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                http.csrf()
                        .disable()
                        .authorizeRequests()
                .mvcMatchers("/books")
                .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);;




    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
