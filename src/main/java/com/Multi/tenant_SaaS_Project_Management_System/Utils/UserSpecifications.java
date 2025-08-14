package com.Multi.tenant_SaaS_Project_Management_System.Utils;

import com.Multi.tenant_SaaS_Project_Management_System.Entities.User;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserRole;
import com.Multi.tenant_SaaS_Project_Management_System.Enums.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecifications {

    public static Specification<User> firstNameContains(String firstName) {
        return (root, query, cb) ->
                firstName == null ? null :
                        cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> lastNameContains(String lastName) {
        return (root, query, cb) ->
                lastName == null ? null :
                        cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> fullNameContains(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null) return null;
            String pattern = "%" + fullName.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
                    cb.like(cb.lower(root.get("lastName")), pattern)
            );
        };
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) ->
                email == null ? null :
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> roleEquals(UserRole role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role);
    }

    public static Specification<User> statusEquals(UserStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<User> createdAfter(LocalDateTime after) {
        return (root, query, cb) ->
                after == null ? null : cb.greaterThan(root.get("createdAt"), after);
    }

    public static Specification<User> createdBefore(LocalDateTime before) {
        return (root, query, cb) ->
                before == null ? null : cb.lessThan(root.get("createdAt"), before);
    }
}
