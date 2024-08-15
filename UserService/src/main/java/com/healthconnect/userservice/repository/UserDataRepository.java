package com.healthconnect.userservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.User;
import com.healthconnect.commonmodels.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends UserRepository, GenericRepository<User, Long> {
}
