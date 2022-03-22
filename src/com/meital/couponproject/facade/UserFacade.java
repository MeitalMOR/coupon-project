package com.meital.couponproject.facade;


public abstract class UserFacade {

    //-----------login abstract method for all type of users to inherit--------------
    public abstract boolean login(String email, String password);

}
