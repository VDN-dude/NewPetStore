package com.example.newpetstore.config;

import com.example.newpetstore.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JWTTokenProvider {

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expired}")
    private String jwtExpirationInMs;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
       jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public String  generateToken(String username, Set<Role> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getUserRoleNamesFromJWT(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserUsernameFromJWT(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    public String getUserUsernameFromJWT(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authentication");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    private List<String> getUserRoleNamesFromJWT(Set<Role> roles){
        List<String> result = new ArrayList<>();
        roles.forEach(role -> result.add(role.name()));
        return result;
    }
}
