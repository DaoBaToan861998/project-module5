package com.ra.service;

import com.ra.dto.request.ChangPasswordRequest;
import com.ra.dto.request.EditUserRequest;
import com.ra.dto.request.SignInForm;
import com.ra.dto.request.SignUpForm;
import com.ra.dto.response.JwtResponse;
import com.ra.model.Users;
import com.ra.advice.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface IUserService extends IGenericService<Users> {
    Optional<Users> findByUsername(String name);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
//    Users save(Users users);

    Users save(SignUpForm signUpForm);
    JwtResponse login(SignInForm signInForm) throws CustomException;

    void changePassword(Authentication authentication, ChangPasswordRequest changPasswordRequest) throws CustomException;
    void handLocked(Long userId) throws CustomException;
    Page<Users> findAllUserWithPagination(int offset,int pageSize);

    void addRole(Long userId, Long roleId) throws CustomException;
    void deleteRoleByUseId(Long userId,Long roleId) throws CustomException;
    Users editUser(EditUserRequest editUserRequest) throws CustomException;
}
