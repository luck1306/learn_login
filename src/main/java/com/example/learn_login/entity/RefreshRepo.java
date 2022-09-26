package com.example.learn_login.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshRepo extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByKeyA(String keyA);
    Optional<RefreshToken> findByValueA(String valueA);
}
