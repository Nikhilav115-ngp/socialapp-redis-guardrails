package com.nikhil.socialapp.repository;

import com.nikhil.socialapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}