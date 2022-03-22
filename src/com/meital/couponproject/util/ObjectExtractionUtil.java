package com.meital.couponproject.util;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class ObjectExtractionUtil {

    //-----------------------extract company object from sql table----------------------
    public static Company resultToCompany(ResultSet result) throws SQLException {
        Company company = new Company();
        company.setId(result.getLong("id"));
        company.setName(result.getString("name"));
        company.setEmail(result.getString("email"));
        company.setPassword(result.getString("password"));

        return company;
    }

    //-----------------------extract customer object from sql table----------------------
    public static Customer resultToCustomer(ResultSet result) throws SQLException {
        Customer customer = new Customer();
        customer.setId(result.getLong("id"));
        customer.setFirstName(result.getString("first_name"));
        customer.setLastName(result.getString("last_name"));
        customer.setEmail(result.getString("email"));
        customer.setPassword(result.getString("password"));

        return customer;
    }

    //-----------------------extract coupon object from sql table----------------------
    public static Coupon resultToCoupon(ResultSet result) throws SQLException, ParseException {
        Coupon coupon = new Coupon();
        coupon.setId(result.getLong("id"));
        coupon.setCompanyId(result.getLong("company_id"));
        coupon.setCategory(Category.valueOf(result.getString("category")));
        coupon.setTitle(result.getString("title"));
        coupon.setDescription(result.getString("description"));
        coupon.setStartDate(result.getString("start_date"));
        coupon.setEndDate(result.getString("end_date"));
        coupon.setAmount(result.getInt("amount"));
        coupon.setPrice(result.getDouble("price"));
        coupon.setImage(result.getString("image"));

        return coupon;
    }


}
