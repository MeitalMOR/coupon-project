package com.meital.couponproject.dao;

import com.meital.couponproject.exceptions.CrudException;

import java.util.List;

public interface CrudDAO<ID, Entity> {

    ID create(final Entity entity) throws CrudException;

    void update(final Entity entity) throws CrudException;

    void delete(final ID id) throws CrudException;

    List<Entity> readAll() throws CrudException;

    Entity read(final ID id) throws CrudException;
}
