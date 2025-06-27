package com.tuda.controller;

import com.tuda.entity.AbstractEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@RequiredArgsConstructor
public class EntityController<E extends AbstractEntity> {

    protected final ModelMapper modelMapper;

    protected <T> T serialize(E entity, Class<T> toClass) {
        return modelMapper.map(entity, toClass);
    }

    protected <T> List<T> serialize(List<E> entities, Class<T> toClass) {
        return entities.stream().map(it -> serialize(it, toClass)).toList();
    }
}
