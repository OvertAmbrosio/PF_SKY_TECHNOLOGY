package com.org.skytechnology.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    // Clave secreta fija para desarrollo (en producción debería ir en
    // application.properties)
    private static final String SECRET_KEY_STRING = "MySuperSecretKeyForJwtSigningThatIsLongEnoughToMeetRequirements123";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    private final int jwtExpirationMs = 86400000;

    public String generateJwtToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}