package com.ra.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    // TODO : từ chối quyền truy cập

    private final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Ghi log lỗi khi từ chối quyền truy cập
        logger.error("Un Permission {}", accessDeniedException.getMessage());

        // Tạo đối tượng ResponseEntity với thông báo lỗi và mã trạng thái HTTP 403 (FORBIDDEN)
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Un Permission", HttpStatus.FORBIDDEN);

        // Thiết lập mã trạng thái của response và ghi thông báo lỗi vào body của response
        response.setStatus(responseEntity.getStatusCodeValue());
        response.getWriter().write(Objects.requireNonNull(responseEntity.getBody()));
    }
}
