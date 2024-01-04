package com.ra.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    // TODO :  điểm cấp lỗi

    // Ghi log lỗi khi xác thực không thành công
   private  static  final Logger logger= LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Un Authorized error Massage {}",authException.getMessage());


        // Tạo đối tượng ResponseEntity với thông báo lỗi và mã trạng thái HTTP 401 (UNAUTHORIZED)
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Un Authentication", HttpStatus.UNAUTHORIZED);
        // Thiết lập mã trạng thái của response và ghi thông báo lỗi vào body của response
        response.sendError(HttpStatus.UNAUTHORIZED.value(),"Error -> Un Authorized");
    }
}
