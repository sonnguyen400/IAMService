package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.Permission;
import com.sonnguyen.iam.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PermissionService {
    PermissionRepository permissionRepository;

    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByName(name);
    }

}
