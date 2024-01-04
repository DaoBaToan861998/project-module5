package com.ra.controller;


import com.ra.dto.request.ChangPasswordRequest;
import com.ra.dto.request.EditUserRequest;
import com.ra.dto.request.SignInForm;
import com.ra.dto.request.SignUpForm;

import com.ra.dto.response.ResponseMessage;

import com.ra.model.Users;
import com.ra.security.jwt.JwtProvider;

import com.ra.service.impl.RoleServiceImpl;
import com.ra.service.impl.UserServiceImpl;
import com.ra.advice.CustomException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/v1")
public class AuthController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationProvider authenticationProvider;
    @Autowired
    JwtProvider jwtProvider;
    @PostMapping("/auth/signup")
    public ResponseEntity<?> register(@RequestBody @Valid SignUpForm signUpForm){
        if(userService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("The username is existed"), HttpStatus.OK);
        }
        if(userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.OK);
        }
        userService.save(signUpForm);
        return new ResponseEntity<>(new ResponseMessage("Create success!"), HttpStatus.OK);
    }
    @PostMapping("/auth/signin")
    public ResponseEntity<?> login(@RequestBody @Valid SignInForm signInForm) throws CustomException {
        return ResponseEntity.ok(userService.login(signInForm));
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<String> changePassword(Authentication authentication, @RequestBody ChangPasswordRequest request) throws CustomException {
        userService.changePassword(authentication,request);
        return ResponseEntity.ok("Password change success");
    }
    @PutMapping("/user/edit-user")
    public  ResponseEntity<?> editUser(@RequestBody EditUserRequest editUserRequest) throws CustomException {
        userService.editUser(editUserRequest);
        return ResponseEntity.ok("Update User success");
    }

    @PostMapping("/admin/locked/{userId}")
    public ResponseEntity<String> handleLocked(@PathVariable  Long userId) throws CustomException {
        userService.handLocked(userId);
        return ResponseEntity.ok("Locked success");

    }
    @GetMapping("/admin/listUser/{offset}/{pageSize}")
    public ResponseEntity<Page<Users>> findAllUserWithPagination(@PathVariable int offset, @PathVariable int pageSize){
        return new ResponseEntity<>(userService.findAllUserWithPagination(offset,pageSize),HttpStatus.OK);
    }
    @PostMapping("/manager/addRole/{userId}/{roleId}")
    public ResponseEntity<String> addRole(@PathVariable Long userId,@PathVariable Long roleId) throws CustomException {
        userService.addRole(userId,roleId);
        return ResponseEntity.ok("add role success");
    }
    @DeleteMapping("/manager/deleteRole/{userId}/{roleId}")
    public  ResponseEntity<String>  deleteRoleByUserId(@PathVariable Long userId,@PathVariable Long roleId) throws CustomException {
        userService.deleteRoleByUseId(userId,roleId);
        return ResponseEntity.ok("delete role success");
    }

}
