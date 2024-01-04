package com.ra.service;

import com.ra.model.Role;
import com.ra.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
