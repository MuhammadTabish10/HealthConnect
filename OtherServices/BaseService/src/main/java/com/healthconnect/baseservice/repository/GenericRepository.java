package com.healthconnect.baseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface GenericRepository<T, ID> extends JpaRepository<T, ID> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.isActive = :isActive")
    List<T> findAllByIsActive(@Param("isActive") Boolean isActive);

    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isActive = false WHERE e.id = :id")
    Long deactivateById(@Param("id") ID id);
}
