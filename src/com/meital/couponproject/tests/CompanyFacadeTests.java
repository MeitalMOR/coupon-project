package com.meital.couponproject.tests;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.dao.CompanyDAO;
import com.meital.couponproject.dao.CouponDAO;
import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.facade.CompanyFacade;
import com.meital.couponproject.facade.CustomerFacade;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.tests.config.CompanyConfig;
import com.meital.couponproject.tests.config.CustomerConfig;
import com.meital.couponproject.util.DataBaseInitializer;

import java.text.ParseException;
import java.util.ArrayList;


public class CompanyFacadeTests {
    public static CompanyFacadeTests instance = new CompanyFacadeTests();

    private final CompanyFacade companyFacade = CompanyFacade.instance;
    private final CustomerFacade customerFacade = CustomerFacade.instance;
    private final CustomerDAO customerDAO = CustomerDAO.instance;
    private final CompanyDAO companyDAO = CompanyDAO.instance;
    private final CouponDAO couponDAO = CouponDAO.instance;

    //--------------------------------Test company login
    private void companyLoginTest() throws CrudException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        companyDAO.create(company);

        if (companyFacade.login(company.getEmail(), company.getPassword())) {

            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test add new coupon to company
    private void addNewCouponTest() throws CrudException, ParseException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        companyFacade.addNewCoupon(coupon);

        if (couponDAO.isExists(coupon.getTitle())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test update company coupon
    private void updateCouponTest() throws CrudException, ParseException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon couponToUpdate = new Coupon(couponId, companyId, Category.SPA_AND_BEAUTY, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 100.0, "www.fox");

        companyFacade.updateCoupon(couponToUpdate);

        if (couponDAO.read(couponId).getCategory().equals(Category.SPA_AND_BEAUTY)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test delete company coupon
    private void deleteCouponTest() throws CrudException, ParseException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        customerFacade.purchaseCoupon(customerId, couponDAO.read(couponId));

        companyFacade.deleteCoupon(couponId);
        if (!couponDAO.isExists(companyId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all company coupons
    private void getCompanyCouponsTest() throws CrudException, ParseException {

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.HOME_DECOR, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        ArrayList<Coupon> coupons = companyFacade.getCompanyCoupons(companyId);
        if (coupons.contains(couponDAO.read(couponId)) && coupons.contains(couponDAO.read(couponId2))) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all company coupons by category
    private void getCompanyCouponsByCategoryTest() throws CrudException, ParseException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.ELECTRICITY, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        ArrayList<Coupon> coupons = companyFacade.getCompanyCouponsByCategory(companyId, Category.ELECTRICITY);
        if (coupons.contains(couponDAO.read(couponId2)) && (!coupons.contains(couponDAO.read(couponId)))) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all company coupons by category
    private void getCompanyCouponsByMaxPriceTest() throws CrudException, ParseException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.ELECTRICITY, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        ArrayList<Coupon> coupons = companyFacade.getCompanyCouponsByMaxPrice(companyId, 200.0);
        if (coupons.contains(couponDAO.read(couponId)) && (!coupons.contains(couponDAO.read(couponId2)))) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get company details
    private void getCompanyDetailsTest() throws CrudException, ParseException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        Coupon coupon2 = new Coupon(companyId, Category.ELECTRICITY, "tv", "75 ench tv",
                "26-03-2022", "01-06-2022", 6, 7000.0, "www.sony");
        Long couponId2 = couponDAO.create(coupon2);

        Company c = companyFacade.getCompanyDetails(companyId, true);

        if (c.getCoupons().contains(couponDAO.read(couponId)) && c.getId().equals(companyId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //---------------------------------Test All company facade methods
    public void companyFacadeTestAll() throws CrudException, ParseException {

        System.out.println("Start testing Company Facade-----------------------------------");
        System.out.println("****Company login Test****");
        CompanyFacadeTests.instance.companyLoginTest();
        System.out.println("\n" + "****Add new coupon to Company Test****");
        CompanyFacadeTests.instance.addNewCouponTest();
        System.out.println("\n" + "****Update Company coupon Test****");
        CompanyFacadeTests.instance.updateCouponTest();
        System.out.println("\n" + "****Delete Company coupon Test****");
        CompanyFacadeTests.instance.deleteCouponTest();
        System.out.println("\n" + "****All Company coupons Test****");
        CompanyFacadeTests.instance.getCompanyCouponsTest();
        System.out.println("\n" + "****All Company coupons by category Test****");
        CompanyFacadeTests.instance.getCompanyCouponsByCategoryTest();
        System.out.println("\n" + "****All Company coupons by max price Test****");
        CompanyFacadeTests.instance.getCompanyCouponsByMaxPriceTest();
        System.out.println("\n" + "****Get Company details Test****");
        CompanyFacadeTests.instance.getCompanyDetailsTest();
    }

}
