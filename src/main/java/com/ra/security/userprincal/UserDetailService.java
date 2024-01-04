package com.ra.security.userprincal;

import com.ra.model.Users;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   // sử dụng lớp UserPrincipal để tạo UserDetails
        Users users=userRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("không tìm thấy user có tên là +"+username));
        return  UserPrinciple.build(users);
    }
}
