package com.f1soft.authservice.utility;

import com.f1soft.authservice.exceptionHandler.UnauthorisedException;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.Objects;

public class JWTTokenUtility {

    private static final String secret = "QnuQblQWn8H9ggiwfGbCxpPA3gdY1oAe";

    public static String resolveToken(RequestContext req) {
        String bearerToken = req.getRequest().getHeader("Authorization");

        if (!Objects.isNull(bearerToken) && bearerToken.startsWith("Bearer"))
            return bearerToken.substring(7, bearerToken.length());

        return null;
    }

    public static boolean validateToken(String token) {
        try {
//            String username = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();

            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date()))
                return false;

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorisedException("Request not authorized, please contact system administrator.",
                    "Expired or invalid JWT token");
        }
    }
}
