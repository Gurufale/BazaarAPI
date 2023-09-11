package com.apigateway.util;

import com.apigateway.exception.NotAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
@Slf4j
public class JwtUtil {
    public static final String secretString = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretString);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean validateToken(final String token, RequestPath path) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);

        Claims body = claimsJws.getBody();
        String role = (String) body.get("userRole");

        log.info("body :" + body);
        log.info(" Role : " + role);

        boolean isCart = path.toString().startsWith("/cart");

        if (role.equals("Admin") && !isCart) return true;
        else if (role.equals("User") && isCart) return  true;
        else throw new NotAuthorizedException("User is not authorized as admin");
    }

}
