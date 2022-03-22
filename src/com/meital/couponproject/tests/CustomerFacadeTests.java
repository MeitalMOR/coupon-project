package com.meital.couponproject.tests;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.dao.*;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.facade.CustomerFacade;
import com.meital.couponproject.model.*;
import com.meital.couponproject.tests.config.CompanyConfig;
import com.meital.couponproject.tests.config.CustomerConfig;
import com.meital.couponproject.util.DataBaseInitializer;

import java.text.ParseException;

public class CustomerFacadeTests {
    public static CustomerFacadeTests instance = new CustomerFacadeTests();

    private final CustomerFacade customerFacade = CustomerFacade.instance;
    private final CustomerDAO customerDAO = CustomerDAO.instance;
    private final CompanyDAO companyDAO = CompanyDAO.instance;
    private final CouponDAO couponDAO = CouponDAO.instance;


    //--------------------------------Test for customer login
    private void customerLoginTest() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        customerDAO.create(customer);

        if (customerFacade.login(customer.getEmail(), customer.getPassword())) {

            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test purchase coupon by customer
    private void purchaseCouponTest() throws CrudException, ParseException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId));
        if (couponDAO.read(couponId).getAmount() == coupon.getAmount() - 1) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all customer coupons
    private void getCustomerCouponsTest() throws CrudException, ParseException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.HOME_DECOR, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId));
        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId2));

        if (!customerFacade.getCustomerCoupons(customerId).isEmpty()) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all customer coupons by category
    private void getCustomerCouponsByCategoryTest() throws CrudException, ParseException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.ELECTRICITY, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId));
        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId2));

        if (customerFacade.getCustomerCouponsByCategory(customerId, Category.HOME_DECOR).contains(couponDAO.read(couponId))) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all customer coupons by max price
    private void getCustomerCouponsByMaxPriceTest() throws CrudException, ParseException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.ELECTRICITY, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId));
        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId2));

        if (customerFacade.getCustomerCouponsByMaxPrice(customerId, 200).contains(couponDAO.read(couponId))) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all customer details
    private void getCustomerDetailsTest() throws CrudException, ParseException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.ELECTRICITY, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId));
        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId2));

        Customer c = customerFacade.getCustomerDetails(customerId, true);

        if (c.getCoupons().contains(couponDAO.read(couponId)) && c.getId().equals(customerId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //---------------------------------Test All customer facade methods
    public void customerFacadeTestAll() throws CrudException, ParseException {

        System.out.println("Start testing Customer Facade-----------------------------------");
        System.out.println("****Customer login Test****");
        CustomerFacadeTests.instance.customerLoginTest();
        System.out.println("\n" + "****Purchase coupon Test****");
        CustomerFacadeTests.instance.purchaseCouponTest();
        System.out.println("\n" + "****All Customer coupons Test****");
        CustomerFacadeTests.instance.getCustomerCouponsTest();
        System.out.println("\n" + "****All Customer coupons by category Test****");
        CustomerFacadeTests.instance.getCustomerCouponsByCategoryTest();
        System.out.println("\n" + "****All Customer coupons by max price Test****");
        CustomerFacadeTests.instance.getCustomerCouponsByMaxPriceTest();
        System.out.println("\n" + "****Get Customer details Test****");
        CustomerFacadeTests.instance.getCustomerDetailsTest();
    }


}
