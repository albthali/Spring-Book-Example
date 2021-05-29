package me.abdalrahman.albthali.books.security;

import io.jsonwebtoken.Claims;
import me.abdalrahman.albthali.books.security.utils.JWTUtils;

public class JWToken {
    private final Claims claims;
    private final String jwt;
    private final JWTUtils jwtUtils;
    public JWToken(String jwt, JWTUtils jwtUtils){
        this.jwt = jwt;
        this.jwtUtils = jwtUtils;
        this.claims = jwtUtils.getClaims(jwt);

    }
    public Claims getClaims(){
        return claims;
    }
    public boolean isBearer(){
        String typ = claims.get("typ",String.class);
        return typ.equals("Bearer");
    }
    public boolean isRefresh(){
        String typ = claims.get("typ",String.class);
        return typ.equals("Refresh");
    }
    public boolean expired(){
        try{
            jwtUtils.getParser().parseClaimsJws(jwt).getBody().getExpiration();
            return false;
        }
        catch (Exception e ){
            return true;

        }

    }
    public boolean verify(){
        return jwtUtils.getParser().isSigned(jwt);
    }
    public String getSubject(){
        return claims.getSubject();
    }
    public String getSession() {
        return claims.get("session",String.class);
    }
}
