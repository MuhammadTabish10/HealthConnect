package com.healthconnect.userservice.repository;

import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.commonmodels.model.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByEmailAndIsActiveIsTrue(String email);
    Optional<User> findByResetToken(String token);

    @Modifying
    @Query("UPDATE User us SET us.isActive = :status WHERE us.id = :id")
    int setStatusWhereId(@Param("id") Long id, @Param("status") Boolean isActive);
}
