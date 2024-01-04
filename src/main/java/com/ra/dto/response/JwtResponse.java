package com.ra.dto.response;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Builder
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
//    private Collection<? extends GrantedAuthority> roles;

    private Set<String> roles;


    public JwtResponse() {
    }

    public JwtResponse(String token, String type, String username, Set<String> roles) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.roles = roles;
    }

    public JwtResponse(String token, String username, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    //    public Collection<? extends GrantedAuthority> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Collection<? extends GrantedAuthority> roles) {
//        this.roles = roles;
//    }
}
