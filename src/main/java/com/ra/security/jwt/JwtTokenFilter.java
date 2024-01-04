package com.ra.security.jwt;

import com.ra.security.userprincal.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    // TODO : loc quyen thong qua URI
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Lấy token từ request và xác thực nếu hợp lệ
            String token = getJwt(request);
            if (token != null && jwtProvider.validateToken(token)) {
                // Lấy thông tin người dùng từ token và xác thực
                String userName = jwtProvider.getUserNameFromToke(token);
                UserDetails userDetails = userDetailService.loadUserByUsername(userName);
                // Tạo đối tượng xác thực và đặt nó vào SecurityContextHolder
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e){
            // Xử lý lỗi xác thực và ghi log
         logger.error("Can't set user authentication -> Message: {}",e.getMessage());
        }
        // Cho phép request tiếp theo trong chuỗi lọc
        filterChain.doFilter(request,response);
    }

    public String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
//            return authHeader.replace("Bearer", "");
        }
        return null;
    }

}
