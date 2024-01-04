package com.ra.config;

import com.ra.security.jwt.JwtEntryPoint;
import com.ra.security.jwt.JwtTokenFilter;
import com.ra.security.userprincal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;


    // Bean để cung cấp PasswordEncoder cho việc mã hóa mật khẩu
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Bean để cung cấp AuthenticationProvider để sử dụng UserDetails và PasswordEncoder
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Cấu hình CORS (Cross-Origin Resource Sharing)
                .cors(auth -> auth.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));// Chấp nhận tất cả các nguồn gốc (origin)
                    config.setAllowedMethods(List.of("*"));// Chấp nhận tất cả các phương thức HTTP
                    config.setAllowCredentials(true);
                    return config;
                }))
                // Tắt CSRF (Cross-Site Request Forgery) protection
                .csrf(AbstractHttpConfigurer::disable)
                // Cung cấp cấu hình xác thực (authentication) và ủy quyền (authorization)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests((auth) ->
                        // Cho phép tất cả các yêu cầu đến đường dẫn "/auth/**" mà không cần xác thực
                        auth.requestMatchers("/v1/auth/**","/v1/categories/**","/v1/products/**").permitAll()
                                .requestMatchers("/v1/user/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/v1/admin/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/v1/manager/**").hasAuthority("ROLE_MANAGER")
                                .anyRequest().authenticated())
                // Xử lý các ngoại lệ xác thực và từ chối truy cập
                .exceptionHandling((auth) -> auth.authenticationEntryPoint(jwtEntryPoint))
                // Quản lý quản lý phiên (session management) và chọn cách tạo phiên (STATELESS: không lưu trạng thái)
                .sessionManagement((auth) -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Thêm một bộ lọc trước bộ lọc xác thực mật khẩu để xử lý mã JWT
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
