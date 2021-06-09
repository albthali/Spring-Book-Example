package me.abdalrahman.albthali.books.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.lang.Assert;
import me.abdalrahman.albthali.books.security.BooksUserDetailsService;
import me.abdalrahman.albthali.books.security.JWToken;
import me.abdalrahman.albthali.books.security.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    @Autowired
    private BooksUserDetailsService userDetailsService;
    @Autowired
    private JWTUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorization = httpServletRequest.getHeader("Authorization");
            if (authorization == null || !authorization.matches("Bearer [^\\ ].*")) {
                logger.debug("Missing bearer. Authroization:" + authorization);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Authorization header");
            }
            String jwt = authorization.split(" ")[1];
            JWToken jwToken = null;
            try{
                jwToken = new JWToken(jwt,jwtUtils);

            } catch (ExpiredJwtException e){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired Token");
            }
            if (!jwToken.verify()) {
                logger.debug("Malformed JWT ");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Malformed token");
            }
            if (!jwToken.isBearer()){
                logger.debug("token is not bearer");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is not an access token");
            }
            if(!jwtUtils.sessionExist(jwToken.getSession())){
                logger.debug("logged out session");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged out token");
            }
            String username = jwToken.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        catch (ResponseStatusException e){
            httpServletResponse.sendError(e.getRawStatusCode(),e.getReason());
            logger.debug(e);
        }

    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String uri = request.getRequestURI();
        logger.debug("Accesing request:"+uri);
        switch (uri){
            case "/auth":
            case "/users":
            case "/auth/token":
            case "/auth/logout":
                return request.getMethod().equals(HttpMethod.POST.name());
            case "/auth/key":
                return request.getMethod().equals(HttpMethod.GET.name());

        }
        if(uri.matches("^(/books).*")) return true;
        return false;
    }


}
