package com.meital.couponproject.model;

import lombok.*;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Company {

    private Long id;
    private String name;
    private String email;
    private String password;
    private ArrayList<Coupon> coupons;

    //------------------constructor for Object extraction util------------------------------

    /**
     * Company constructor for object extraction util
     * @param id company id
     * @param name company name
     * @param email company email
     * @param password company password
     */
    public Company(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //------------------constructor for update method at companyDAO------------------------------
    private Company(Long id, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //------------------constructor for new object------------------------------------------
    public Company(String name, String email, String password) {
        this.id = null;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //---------------------------company toString method-----------------------------------
    @Override
    public String toString() {
        return "Company- " + "Id: " + id + ", Name: " + name + ' ' + ", Email: " +
                email + ' ' + ", Password: " + password ;
    }
}
