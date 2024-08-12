package com.healthconnect.baseservice.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MappingUtils {

    private final ModelMapper modelMapper;

    public MappingUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <D, T> D mapToDto(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public <D, T> T mapToEntity(final D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public <T> void map(final T source, final T destination) {
        modelMapper.map(source, destination);
    }

    public <D, T> List<D> mapToDtoList(final List<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> mapToDto(entity, outClass))
                .collect(Collectors.toList());
    }

    public <D, T> List<T> mapToEntityList(final List<D> dtoList, Class<T> entityClass) {
        return dtoList.stream()
                .map(dto -> mapToEntity(dto, entityClass))
                .collect(Collectors.toList());
    }
}
