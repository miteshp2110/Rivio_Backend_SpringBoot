package com.cts.rivio.modules.auth.service.impl;

import com.cts.rivio.modules.auth.dto.response.PermissionResponse;
import com.cts.rivio.modules.auth.mapper.PermissionMapper;
import com.cts.rivio.modules.auth.repository.PermissionRepository;
import com.cts.rivio.modules.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;
    private final PermissionMapper mapper;

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}