package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.PermissionDetail;
import com.sonnguyen.iam.repository.PermissionDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PermissionDetailService {
    PermissionDetailRepository permissionDetailRepository;

    List<PermissionDetail> findAllByAccountId(Long accountId) {
        return permissionDetailRepository.findAllPermissionDetailByAccountId(accountId);
    }
}
