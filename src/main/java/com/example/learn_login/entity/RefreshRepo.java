package com.example.learn_login.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshRepo extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByKeyA(String keyA);
}
