package com.example.basespringboot.repository;

import com.example.basespringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String userName);

    @Modifying
    @Transactional
    void deleteUsersByUserName(String name);
}
