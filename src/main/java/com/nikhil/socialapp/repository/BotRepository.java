package com.nikhil.socialapp.repository;

import com.nikhil.socialapp.entity.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {
}