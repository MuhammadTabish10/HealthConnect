package com.healthconnect.authservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByEmailAndIsActiveIsTrue(String email);
}
