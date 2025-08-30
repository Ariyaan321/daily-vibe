package com.dailyvibeproj.daily_vibe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.dailyvibeproj.daily_vibe.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}