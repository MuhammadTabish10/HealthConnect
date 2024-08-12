package com.healthconnect.baseservice.service;

import java.util.List;

public interface GenericService<U> {
    U save(U dto);
    List<U> getAll(Boolean isActive);
    U getById(Long id);
    U update(U dto, Long id);
    void delete(Long id);
}
