package com.meital.couponproject;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.dao.CompanyDAO;
import com.meital.couponproject.dao.CouponDAO;
import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.facade.AdminFacade;
import com.meital.couponproject.facade.CompanyFacade;
import com.meital.couponproject.facade.CustomerFacade;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.tests.*;
import com.meital.couponproject.util.DataBaseInitializer;


import java.sql.SQLException;
import java.text.ParseException;


public class Program {
    public static void main(String[] args) throws CrudException, ParseException, SQLException, InterruptedException {


        Tests.instance.testAll();


    }


}
