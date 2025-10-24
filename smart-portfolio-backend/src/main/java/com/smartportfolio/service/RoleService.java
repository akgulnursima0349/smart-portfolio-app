package com.smartportfolio.service;

import com.smartportfolio.dto.RoleDto;
import com.smartportfolio.exception.ResourceNotFoundException;
import com.smartportfolio.model.Role;
import com.smartportfolio.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles() {
        log.info("Tüm roller getiriliyor");
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleDto getRoleById(Long id) {
        log.info("Rol getiriliyor - ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return modelMapper.map(role, RoleDto.class);
    }

    @Transactional(readOnly = true)
    public RoleDto getRoleByName(String name) {
        log.info("Rol getiriliyor - Name: {}", name);
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
        return modelMapper.map(role, RoleDto.class);
    }

    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        log.info("Yeni rol oluşturuluyor: {}", roleDto.getName());
        Role role = modelMapper.map(roleDto, Role.class);
        Role savedRole = roleRepository.save(role);
        log.info("Rol başarıyla oluşturuldu: {}", savedRole.getName());
        return modelMapper.map(savedRole, RoleDto.class);
    }

    @Transactional
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        log.info("Rol güncelleniyor - ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        
        Role updatedRole = roleRepository.save(role);
        log.info("Rol başarıyla güncellendi - ID: {}", id);
        return modelMapper.map(updatedRole, RoleDto.class);
    }

    @Transactional
    public void deleteRole(Long id) {
        log.info("Rol siliniyor - ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        roleRepository.delete(role);
        log.info("Rol başarıyla silindi - ID: {}", id);
    }
}


