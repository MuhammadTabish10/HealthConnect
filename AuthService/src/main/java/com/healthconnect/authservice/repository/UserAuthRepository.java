package com.healthconnect.authservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.User;
import com.healthconnect.commonmodels.repository.UserRepository;

public interface UserAuthRepository extends UserRepository, GenericRepository<User, Long> {
}
