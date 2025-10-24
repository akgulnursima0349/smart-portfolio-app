package com.smartportfolio.service;

import com.smartportfolio.dto.UserDto;
import com.smartportfolio.exception.ResourceNotFoundException;
import com.smartportfolio.model.User;
import com.smartportfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.info("Tüm kullanıcılar getiriliyor");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Tüm kullanıcılar sayfalı olarak getiriliyor");
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.info("Kullanıcı getiriliyor - ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        log.info("Kullanıcı getiriliyor - Username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.info("Kullanıcı getiriliyor - Email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Kullanıcı siliniyor - ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
        log.info("Kullanıcı başarıyla silindi - ID: {}", id);
    }

    @Transactional
    public UserDto updateUserStatus(Long id, Boolean isActive) {
        log.info("Kullanıcı durumu güncelleniyor - ID: {}, isActive: {}", id, isActive);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setIsActive(isActive);
        User updatedUser = userRepository.save(user);
        log.info("Kullanıcı durumu başarıyla güncellendi - ID: {}", id);
        return modelMapper.map(updatedUser, UserDto.class);
    }
}


