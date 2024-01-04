package com.ra.repository;

import com.ra.dto.request.SignUpForm;
import com.ra.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
  Optional<Users> findByUserName(String userName);
  Boolean existsByUserName(String userName);
  Boolean existsByEmail(String email);

//  void save(SignUpForm signUpForm);
}
