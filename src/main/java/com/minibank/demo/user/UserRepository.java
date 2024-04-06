package com.minibank.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findUserByUserName(String username);
    @Query("SELECT u FROM User u WHERE u.deletedAt <> null ")
    Optional<User> findActiveUsers();
    @Query("SELECT u FROM User u WHERE u.id = :userid AND u.isAdmin = :isadmin")
    Optional<User> findAdminUsers(
            @Param("userid") Long id,
            @Param("isadmin") boolean isAdmin
    );
}
