package me.abdalrahman.albthali.books.security;

import me.abdalrahman.albthali.books.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class BooksUserDetailsService implements UserDetailsService {

    private final UserModel userModel;
    @Autowired
    public BooksUserDetailsService(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userModel.getUserByUsername(s);
    }


}
