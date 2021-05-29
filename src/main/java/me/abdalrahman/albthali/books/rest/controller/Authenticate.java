package me.abdalrahman.albthali.books.rest.controller;

import io.jsonwebtoken.ExpiredJwtException;
import me.abdalrahman.albthali.books.entity.JWTEntity;
import me.abdalrahman.albthali.books.repository.JWTRepository;
import me.abdalrahman.albthali.books.schema.AuthenticateRequest;
import me.abdalrahman.albthali.books.schema.AuthenticateResponse;
import me.abdalrahman.albthali.books.schema.TokenRequest;
import me.abdalrahman.albthali.books.security.BooksUserDetailsService;
import me.abdalrahman.albthali.books.security.JWToken;
import me.abdalrahman.albthali.books.security.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Authenticate {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BooksUserDetailsService userDetailsService;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private JWTRepository jwtRepository;
    @PostMapping(value = "/auth")
    public AuthenticateResponse authenticateResponse(@RequestBody AuthenticateRequest authenticateRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(),authenticateRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticateRequest.getUsername());
        return generateTokens(userDetails);

    }
    @PostMapping(value = "/auth/token")
    public AuthenticateResponse token(@RequestBody TokenRequest tokenRequest){
        String refreshToken = tokenRequest.getRefreshToken();
        JWToken jwToken = null;
        try{
            jwToken =  new JWToken(refreshToken,jwtUtils);
        } catch (ExpiredJwtException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired token",e);
        }
        if(!jwToken.verify()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Malformed token");
        }
        if (!jwToken.isRefresh()){

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is not an access token");
        }
        if(!jwtUtils.sessionExist(jwToken.getSession())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged out token");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwToken.getSubject());
        jwtUtils.removeSession(jwToken.getSession());
        return generateTokens(userDetails);

    }
    private AuthenticateResponse generateTokens(UserDetails userDetails){
        String session = jwtUtils.generateSession();
        JWTEntity jwtEntity = new JWTEntity();
        jwtEntity.setId(session);
        jwtEntity.setUsername(userDetails.getUsername());
        jwtEntity.setRefreshTokenExpiration(new Date(System.currentTimeMillis() + JWTUtils.Expiration.TEN_MINUTES.getTime()).getTime());
        jwtEntity.setAccessTokenExpiration(new Date(System.currentTimeMillis() + JWTUtils.Expiration.FIVE_MINUTES.getTime()).getTime());
        jwtRepository.save(jwtEntity);
        Map<String,Object> claims = new HashMap<>();
        claims.put("session",session);
        claims.put("typ","Bearer");
        String access_token = jwtUtils.generateToken(userDetails, claims, JWTUtils.Expiration.FIVE_MINUTES);
        claims.put("typ","Refresh");
        String refresh_token = jwtUtils.generateToken(userDetails, claims, JWTUtils.Expiration.TEN_MINUTES);
        return new AuthenticateResponse(access_token,refresh_token);

    }
    private void logout(String jwt){


    }

}
