package com.healthconnect.baseservice.service.impl;

import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.exception.EntityDeleteException;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.baseservice.exception.EntitySaveException;
import com.healthconnect.baseservice.exception.EntityUpdateException;
import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.baseservice.util.MappingUtils;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class GenericServiceImpl<T, U> implements GenericService<U> {

    private final GenericRepository<T, Long> repository;
    private final MappingUtils mappingUtils;
    private final Class<T> entityClass;
    private final Class<U> dtoClass;

    public GenericServiceImpl(GenericRepository<T, Long> repository, MappingUtils mappingUtils, Class<T> entityClass, Class<U> dtoClass) {
        this.repository = repository;
        this.mappingUtils = mappingUtils;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    @Transactional
    public U save(U dto) {
        try {
            T entity = mappingUtils.mapToEntity(dto, entityClass);
            T savedEntity = repository.save(entity);
            return mappingUtils.mapToDto(savedEntity, dtoClass);
        } catch (Exception e) {
            throw new EntitySaveException(String.format(ErrorMessages.ENTITY_SAVE_FAILED, dtoClass.getSimpleName()), e);
        }
    }

    @Override
    public List<U> getAll(Boolean isActive) {
        try {
            return repository.findAllByIsActive(isActive).stream()
                    .map(entity -> mappingUtils.mapToDto(entity, dtoClass))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_RETRIEVE_FAILED, dtoClass.getSimpleName()), e);
        }
    }

    @Override
    public U getById(Long id) {
        return repository.findById(id)
                .map(entity -> mappingUtils.mapToDto(entity, dtoClass))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, entityClass.getSimpleName(), id)));
    }

    @Override
    @Transactional
    public U update(U dto, Long id) {
        try {
            T entity = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, entityClass.getSimpleName(), id)));
            T updatedEntity = repository.save(mappingUtils.mapToEntity(dto, entityClass));
            return mappingUtils.mapToDto(updatedEntity, dtoClass);
        } catch (Exception e) {
            throw new EntityUpdateException(String.format(ErrorMessages.ENTITY_UPDATE_FAILED, dtoClass.getSimpleName(), id), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            Long affectedRows = repository.deactivateById(id);
            if (affectedRows == 0) {
                throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, entityClass.getSimpleName(), id));
            }
        } catch (Exception e) {
            throw new EntityDeleteException(String.format(ErrorMessages.ENTITY_DELETE_FAILED, entityClass.getSimpleName(), id), e);
        }
    }
}
