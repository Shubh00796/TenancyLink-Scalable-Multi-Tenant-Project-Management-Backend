package com.Multi.tenant_SaaS_Project_Management_System.ReposiotryServices;

import com.Multi.tenant_SaaS_Project_Management_System.DTOs.UserDTO;
import com.Multi.tenant_SaaS_Project_Management_System.Entities.User;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import com.Multi.tenant_SaaS_Project_Management_System.Exceptions.ResourceNotFoundException;
import com.Multi.tenant_SaaS_Project_Management_System.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserRepositoryService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAllPagabaleUsers(Pageable pageable) {
        return userRepository.findAll(pageable);


    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFoundException("email not found" + email));
    }

    public Optional<User> findByEmailAndStatus(String email, UserStatus status) {
        return userRepository.findByEmailAndStatus(email, status);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findByRoleIn(List<UserRole> roles) {
        return userRepository.findByRoleIn(roles);
    }
    public List<User> findByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> findByRoleAndStatus(UserRole role, UserStatus status) {
        return userRepository.findByRoleAndStatus(role, status);
    }

    public long countByStatus(UserStatus status) {
        return userRepository.countByStatus(status);
    }

    public long countByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    public long countByRoleAndStatus(UserRole role, UserStatus status) {
        return userRepository.countByRoleAndStatus(role, status);
    }

    public List<User> findByFullNameContaining(String name) {
        return userRepository.findByFullNameContaining(name);
    }

    public List<User> findByRoleInAndStatus(List<UserRole> roles, UserStatus status) {
        return userRepository.findByRoleInAndStatus(roles, status);
    }

    public Page<User> findByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    public List<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName, Sort sort) {
        return userRepository.findByFirstNameContainingOrLastNameContaining(firstName, lastName, sort);
    }

    public User save(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        return userRepository.save(user);
    }

    public User update(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User getByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }
}