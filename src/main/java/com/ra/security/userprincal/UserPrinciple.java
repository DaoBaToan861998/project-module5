package com.ra.security.userprincal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.Users;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
//@AllArgsConstructor
@Data
@Builder
public class UserPrinciple implements UserDetails {
    private Long id;
    private String name;
    private String userName;
    @JsonIgnore
    private String password;
    private String address;
    private String phone;
    private String email;


    private String avatar;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean locked;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public UserPrinciple(Long id, String name, String userName, String password, String address, String phone, String email, String avatar, Collection<? extends GrantedAuthority> authorities,Boolean locked) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
        this.authorities = authorities;
        this.locked=locked;
    }

    public static UserPrinciple build(Users users){
        List<GrantedAuthority> grantedAuthorities=users.getRoles().stream().map(role->new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
        return new UserPrinciple(
             users.getId(),
             users.getName(),
             users.getUserName(),
                users.getPassword(),
                users.getAddress(),
                 users.getPhone(),
                users.getEmail(),
            users.getAvatar(),
                 grantedAuthorities,
                users.getLocked()
        );
    }
    @Override
    public String getUsername() {
        return this.userName;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
