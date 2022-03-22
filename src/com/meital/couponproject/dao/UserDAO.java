package com.meital.couponproject.dao;

public abstract class UserDAO<ID, Entity> implements CrudDAO<ID, Entity> {

    public abstract boolean isExists(String email, String password);

}
