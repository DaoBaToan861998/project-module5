package com.ra.service.impl;

import com.ra.model.Role;
import com.ra.model.RoleName;
import com.ra.repository.RoleRepository;
import com.ra.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Optional<Role> findByName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
