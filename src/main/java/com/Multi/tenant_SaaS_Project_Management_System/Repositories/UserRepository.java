package com.Multi.tenant_SaaS_Project_Management_System.Repositories;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.User;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);

    boolean existsByEmail(String email);

    List<User> findByRoleIn(List<UserRole> roles);

    List<User> findByStatus(UserStatus status);

    List<User> findByRoleAndStatus(UserRole role, UserStatus status);

    long countByStatus(UserStatus status);

    long countByRole(UserRole role);

    long countByRoleAndStatus(UserRole role, UserStatus status);

    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE %:name%")
    List<User> findByFullNameContaining(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.role IN :roles AND u.status = :status")
    List<User> findByRoleInAndStatus(@Param("roles") List<UserRole> roles, @Param("status") UserStatus status);


    Page<User> findByRole(UserRole role, Pageable pageable);

    List<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName, Sort sort);

}