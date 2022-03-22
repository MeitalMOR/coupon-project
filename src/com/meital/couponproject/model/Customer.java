package com.meital.couponproject.model;

import lombok.*;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@ToString

public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ArrayList<Coupon> coupons;


    //------------------constructor for Object extraction util------------------------------
    public Customer(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    //------------------constructor for new object------------------------------------------
    public Customer(String firstName, String lastName, String email, String password) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }





}
