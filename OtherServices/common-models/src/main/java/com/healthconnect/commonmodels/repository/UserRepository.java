package com.healthconnect.commonmodels.repository;

import com.healthconnect.commonmodels.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndIsActiveIsTrue(String email);
}
