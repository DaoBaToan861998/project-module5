package com.ra.security.jwt;

import com.ra.security.userprincal.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    // TODO : nơi sinh ra token và lấy ra UserLogin
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret-key}")
    private String SECRET;

    @Value("${jwt.expirated}")
    private Long EXPIRED;

    // TODO : phương thức tạo ra token
    public String createToken(Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Date startdate = new Date();
        Date expired = new Date(new Date().getTime() + EXPIRED);
        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(startdate)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
    // TODO : Phương thức kiểm tra tính hợp lệ của token
    public boolean validateToken(String token) {
        try {
            // Parse và kiểm tra chữ ký của token
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);// Lưu ý chính tả
            return true;
        } catch (SignatureException e) {
            logger.error("Ivalid JWT sinature ->Message: {}", e);
        } catch (MalformedJwtException e) {
            logger.error("The token invalid format ->Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT toekn ->Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT Token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("Jwt claims string is empty -> Message {}", e);
        }
        return false;
    }
    // TODO : phương thức lấy tên người dùng từ token
    public String getUserNameFromToke(String token) {
        String userName = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }
}


//
//    public boolean validateToken(String token) {
//        try {
//            // Parse và kiểm tra chữ ký của token
//            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token); // Lưu ý chính tả
//            return true;
//        } catch (ExpiredJwtException e) {
//            // Xử lý khi token đã hết hạn
//            logger.error("Failed -> Expired Token Message {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            // Xử lý khi token không được hỗ trợ
//            logger.error("Failed -> Unsupported Token Message {}", e.getMessage());
//        } catch (MalformedJwtException e) {
//            // Xử lý khi token có định dạng không hợp lệ
//            logger.error("Failed -> Invalid Format Token Message {}", e.getMessage());
//        } catch (SignatureException e) {
//            // Xử lý khi chữ ký của token không hợp lệ
//            logger.error("Failed -> Invalid Signature Token Message {}", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            // Xử lý khi đối số truyền vào phương thức không hợp lệ
//            logger.error("Failed -> Claims Empty Token Message {}", e.getMessage());
//        }
//        return false;
//    }