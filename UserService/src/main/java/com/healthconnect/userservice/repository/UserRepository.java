package com.healthconnect.userservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
}
