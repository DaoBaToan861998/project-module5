package com.ra.service.impl;

import com.ra.dto.request.ChangPasswordRequest;
import com.ra.dto.request.EditUserRequest;
import com.ra.dto.request.SignInForm;
import com.ra.dto.request.SignUpForm;
import com.ra.dto.response.JwtResponse;
import com.ra.model.Role;
import com.ra.model.RoleName;
import com.ra.model.Users;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.security.jwt.JwtProvider;
import com.ra.security.userprincal.UserPrinciple;
import com.ra.service.IUserService;
import com.ra.advice.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
    public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    AuthenticationProvider authenticationProvider;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromMail;
    @Override
    public Optional<Users> findByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Iterable<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Users save(Users users) {
        return userRepository.save(users);
    }


    @Override
    public void deleteById(Long id) {
         userRepository.deleteById(id);
    }

    @Override
    public Users save(SignUpForm signUpForm) {
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role ->{
            switch (role){
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ROLE_ADMIN).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(adminRole);
                    break;
                case "manager":
                    Role pmRole = roleService.findByName(RoleName.ROLE_MANAGER).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.ROLE_USER).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(userRole);
            }
        });
        Users users = Users.builder()
                .name(signUpForm.getName())
                .userName(signUpForm.getUsername())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .address(signUpForm.getAddress())
                .phone(signUpForm.getPhone())
                .email(signUpForm.getEmail())
                .roles(roles)
                .locked(true)
                .build();

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("Thông báo đăng ký tài khoản");
        simpleMailMessage.setText("Chúc mừng bạn đã đăng ký thành công tài khoản : "+signUpForm.getUsername());
        simpleMailMessage.setTo(signUpForm.getEmail());
        javaMailSender.send(simpleMailMessage);
       return    userRepository.save(users);
    }

    @Override
    public JwtResponse login(SignInForm signInForm) throws CustomException {
        Authentication authentication;
        try {
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword()));
        } catch (AuthenticationException ex) {
            throw new RuntimeException("username or password invalid");
        }
        UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();
        if(userPrinciple.getLocked()==false){
            throw new CustomException("user is locked");
        }
        String token = jwtProvider.createToken(authentication);
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .username(userPrinciple.getName())
                .roles(userPrinciple.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toSet()))
                .build();
        return jwtResponse;
    }


    @Override
    public void changePassword(Authentication authentication, ChangPasswordRequest changPasswordRequest) throws CustomException {
        // Lấy thông tin người dùng từ Principal
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Users users = userRepository.findById(userPrinciple.getId()).orElseThrow(() -> new CustomException("user not found"));
//         kiểm tra 2 mật khẩu nhập vào có khớp nhau không
        if (!changPasswordRequest.getNewPassword().equals(changPasswordRequest.getConfirmNewPassword())) {
            throw new CustomException("New password and confirm password do not match");
        }
        // Kiểm tra mật khẩu cũ có khớp hay không
        if (passwordEncoder.matches(changPasswordRequest.getOldPassword(), users.getPassword())) {
            // Nếu khớp, cập nhật mật khẩu mới và trả về thông tin người dùng
            users.setPassword(passwordEncoder.encode(changPasswordRequest.getNewPassword()));
        } else {
            // Nếu không khớp, ném CustomException
            throw new CustomException("old password not matches");
        }

        userRepository.save(users);
    }
    @Override
    public void handLocked(Long userId) throws CustomException {
        Users users=userRepository.findById(userId).orElseThrow(()-> new CustomException("user not foun"));
        users.setLocked(false);
        userRepository.save(users);
    }
    @Override
    public Page<Users> findAllUserWithPagination(int offset, int pageSize) {
        return userRepository.findAll(PageRequest.of(offset,pageSize));
    }

    @Override
    public void addRole(Long userId,Long roleId) throws CustomException {
        Role role=roleRepository.findById(roleId).orElseThrow(()->new CustomException("role not found"));
        Users users=userRepository.findById(userId).orElseThrow(()-> new CustomException("user not found"));
        if(users!=null){
            users.getRoles().add(role);
            userRepository.save(users);
        }
    }

    @Override
    public void deleteRoleByUseId(Long userId, Long roleId) throws CustomException {
       Role role=roleRepository.findById(roleId).orElseThrow(()->new CustomException("role not found"));
       Users users=userRepository.findById(userId).orElseThrow(()->new CustomException("user not found"));

       if(users.getRoles().contains(role)){
           users.getRoles().remove(role);
           userRepository.save(users);
       }else {
           throw  new CustomException("User does not have this role");
       }
    }

//     this.id = id;
//        this.name = name;
//        this.userName = userName;
//        this.password = password;
//        this.address = address;
//        this.phone = phone;
//        this.email = email;
//        this.avatar = avatar;
//        this.locked = locked;
//        this.roles = roles;
    @Override
    public Users editUser(EditUserRequest editUserRequest) throws CustomException {
         Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
         UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();
         Users oldUser=userRepository.findById(userPrinciple.getId()).orElseThrow(()-> new  CustomException("user nout found"));
         Users users=Users.builder()
                 .id(oldUser.getId())
                 .name(editUserRequest.getName())
                 .userName(oldUser.getUserName())
                 .password(oldUser.getPassword())
                 .address(editUserRequest.getAddress())
                 .phone(editUserRequest.getPhone())
                 .email(editUserRequest.getEmail())
                 .avatar(oldUser.getAvatar())
                 .locked(userPrinciple.getLocked())
                 .roles(oldUser.getRoles())
                 .build();
       return   userRepository.save(users);
    }
}
