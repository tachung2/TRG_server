package xyz.tachung.server.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey = "secretKey";  // 실제 환경에서는 외부 설정에서 받아오거나 보다 안전한 방법으로 관리

    // Access Token 생성
    public String generateToken(String email) {
        return generateTokenWithExpiry(email, 1000 * 60 * 60 * 10);  // 10시간
    }

    // Refresh Token 생성
    public String generateRefreshToken(String email) {
        return generateTokenWithExpiry(email, 1000 * 60 * 60 * 24 * 7);  // 7일
    }

    private String generateTokenWithExpiry(String email, long expiryDuration) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryDuration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Token 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}