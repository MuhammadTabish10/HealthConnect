package com.healthconnect.baseservice.service.impl;

import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.constant.LogMessages;
import com.healthconnect.baseservice.exception.EntityDeleteException;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.baseservice.exception.EntitySaveException;
import com.healthconnect.baseservice.exception.EntityUpdateException;
import com.healthconnect.baseservice.repository.GenericRepository;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.baseservice.util.MappingUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GenericServiceImpl<T, U> implements GenericService<U> {

    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);

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
        logger.info(LogMessages.ENTITY_SAVE_START, dtoClass.getSimpleName());
        try {
            T entity = mappingUtils.mapToEntity(dto, entityClass);
            T savedEntity = repository.save(entity);
            logger.info(LogMessages.ENTITY_SAVE_SUCCESS, dtoClass.getSimpleName(), savedEntity);
            return mappingUtils.mapToDto(savedEntity, dtoClass);
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_SAVE_ERROR, dtoClass.getSimpleName(), e.getMessage(), e);
            throw new EntitySaveException(String.format(ErrorMessages.ENTITY_SAVE_FAILED, dtoClass.getSimpleName()), e);
        }
    }

    @Override
    public List<U> getAll(Boolean isActive) {
        logger.info(LogMessages.ENTITY_FETCH_ALL_START, dtoClass.getSimpleName(), isActive);
        try {
            List<U> dtos = repository.findAllByIsActive(isActive).stream()
                    .map(entity -> mappingUtils.mapToDto(entity, dtoClass))
                    .toList();
            logger.info(LogMessages.ENTITY_FETCH_ALL_SUCCESS, dtos.size(), dtoClass.getSimpleName());
            return dtos;
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_FETCH_ALL_ERROR, dtoClass.getSimpleName(), e.getMessage(), e);
            throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_RETRIEVE_FAILED, dtoClass.getSimpleName()), e);
        }
    }

    @Override
    public U getById(Long id) {
        logger.info(LogMessages.ENTITY_FETCH_START, dtoClass.getSimpleName(), id);
        return repository.findById(id)
                .map(entity -> {
                    logger.info(LogMessages.ENTITY_FETCH_SUCCESS, dtoClass.getSimpleName(), id);
                    return mappingUtils.mapToDto(entity, dtoClass);
                })
                .orElseThrow(() -> {
                    logger.error(LogMessages.ENTITY_FETCH_ERROR, dtoClass.getSimpleName(), id);
                    return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, entityClass.getSimpleName(), id));
                });
    }

    @Override
    @Transactional
    public U update(U dto, Long id) {
        logger.info(LogMessages.ENTITY_UPDATE_START, dtoClass.getSimpleName(), id);
        try {
            T existingEntity = repository.findById(id)
                    .orElseThrow(() -> {
                        logger.error(LogMessages.ENTITY_FETCH_ERROR, dtoClass.getSimpleName(), id);
                        return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, entityClass.getSimpleName(), id));
                    });
            T updatedEntity = repository.save(existingEntity);
            logger.info(LogMessages.ENTITY_UPDATE_SUCCESS, dtoClass.getSimpleName(), id);
            return mappingUtils.mapToDto(updatedEntity, dtoClass);
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_UPDATE_ERROR, dtoClass.getSimpleName(), id, e.getMessage(), e);
            throw new EntityUpdateException(String.format(ErrorMessages.ENTITY_UPDATE_FAILED, dtoClass.getSimpleName(), id), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.info(LogMessages.ENTITY_DELETE_START, dtoClass.getSimpleName(), id);
        try {
            if (repository.deactivateById(id) == 0) {
                logger.error(LogMessages.ENTITY_FETCH_ERROR, dtoClass.getSimpleName(), id);
                throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, entityClass.getSimpleName(), id));
            }
            logger.info(LogMessages.ENTITY_DELETE_SUCCESS, dtoClass.getSimpleName(), id);
        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_DELETE_ERROR, dtoClass.getSimpleName(), id, e.getMessage(), e);
            throw new EntityDeleteException(String.format(ErrorMessages.ENTITY_DELETE_FAILED, entityClass.getSimpleName(), id), e);
        }
    }
}
