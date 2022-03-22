package com.meital.couponproject.tests;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.dao.CompanyDAO;
import com.meital.couponproject.dao.CouponDAO;
import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.facade.AdminFacade;
import com.meital.couponproject.facade.CustomerFacade;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.tests.config.AdminConfig;
import com.meital.couponproject.tests.config.CompanyConfig;
import com.meital.couponproject.tests.config.CustomerConfig;
import com.meital.couponproject.util.DataBaseInitializer;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminFacadeTests {

    public static AdminFacadeTests instance = new AdminFacadeTests();

    private final AdminFacade adminFacade = AdminFacade.instance;
    private final CustomerDAO customerDAO = CustomerDAO.instance;
    private final CompanyDAO companyDAO = CompanyDAO.instance;
    private final CouponDAO couponDAO = CouponDAO.instance;

    //--------------------------------Test for Admin login
    private void adminLoginTest() throws CrudException, SQLException, InterruptedException {

        if (adminFacade.login(AdminConfig.ADMIN_EMAIL, AdminConfig.ADMIN_PASSWORD)) {

            System.out.println("Test succeeded");
        }
        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test for create new company
    private void createNewCompanyTest() throws CrudException, SQLException, InterruptedException {

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = adminFacade.addCompany(company);

        Company newCompany = companyDAO.read(companyId);

        if (companyId.equals(newCompany.getId())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test for update company
    private void updateCompanyTest() throws CrudException {

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = adminFacade.addCompany(company);

        Company updatedCompany = new Company(companyId, company.getName(), "companyco@gmail.com", company.getPassword());
        adminFacade.updateCompany(updatedCompany);

        companyDAO.read(companyId);
        if (updatedCompany.getEmail().equals(companyDAO.read(companyId).getEmail())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test for delete company
    private void deleteCompanyTest() throws CrudException, ParseException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        couponDAO.addCouponPurchase(customerId, couponId);
        adminFacade.deleteCompany(companyId);

        if (!companyDAO.isExists(companyId) && !couponDAO.isExists(couponId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test for read all companies
    private void getAllCompanies() throws CrudException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Company company1 = new Company(CompanyConfig.company2Name, CompanyConfig.company2Email, CompanyConfig.company2Password);
        Long companyId1 = companyDAO.create(company1);

        ArrayList<Company> companies = adminFacade.getAllCompanies();
        if (companies.contains(companyDAO.read(companyId)) && companies.contains(companyDAO.read(companyId1))) {
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

        Company c = adminFacade.getOneCompany(companyId, true);

        if (c.getCoupons().contains(couponDAO.read(couponId)) && c.getId().equals(companyId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test add new customer
    private void addNewCustomer() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);

        Long customerId = adminFacade.addNewCustomer(customer);
        Customer newCustomer = customerDAO.read(customerId);

        if (customerId.equals(newCustomer.getId())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test for update customer
    private void updateCustomerTest() throws CrudException {

        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);

        Long customerId = adminFacade.addNewCustomer(customer);

        Customer updatedCustomer = new Customer(customerId, customer.getFirstName(), customer.getLastName(), "customer11@gmail.com", customer.getPassword());
        adminFacade.updateCustomer(updatedCustomer);

        customerDAO.read(customerId);
        if (updatedCustomer.getEmail().equals(customerDAO.read(customerId).getEmail())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test for delete customer
    private void deleteCustomerTest() throws CrudException, ParseException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Coupon coupon = new Coupon(companyId, Category.HOME_DECOR, "table", "6 seats table",
                "26-03-2022", "01-04-2022", 12, 125.3, "www.fox");
        Long couponId = couponDAO.create(coupon);

        couponDAO.addCouponPurchase(customerId, couponId);
        adminFacade.deleteCustomer(customerId);

        if (!customerDAO.isExists(customerId) && couponDAO.isExists(couponId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test for read all customers
    private void getAllCustomers() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        Customer customer2 = new Customer(CustomerConfig.customer2FirstName, CustomerConfig.customer2LastName, CustomerConfig.customer2Email, CustomerConfig.customer2Password);
        Long customer2Id = customerDAO.create(customer2);

        ArrayList<Customer> customers = adminFacade.getAllCustomers();
        if (customers.contains(customerDAO.read(customerId)) && customers.contains(customerDAO.read(customer2Id))) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test customer details
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

        couponDAO.addCouponPurchase(customerId, couponId);
        couponDAO.addCouponPurchase(customerId, couponId2);

        Customer c = adminFacade.getOneCustomer(customerId, true);

        if (c.getCoupons().contains(couponDAO.read(couponId)) && c.getId().equals(customerId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //---------------------------------Test All Admin facade methods
    public void adminFacadeTestAll() throws CrudException, ParseException, SQLException, InterruptedException {

        System.out.println("Start testing Admin Facade-----------------------------------");
        System.out.println("****Admin login Test****");
        AdminFacadeTests.instance.adminLoginTest();
        System.out.println("\n" + "****Create new Company Test****");
        AdminFacadeTests.instance.createNewCompanyTest();
        System.out.println("\n" + "****Update Company Test****");
        AdminFacadeTests.instance.updateCompanyTest();
        System.out.println("\n" + "****Delete Company Test****");
        AdminFacadeTests.instance.deleteCompanyTest();
        System.out.println("\n" + "****Get All Companies Test****");
        AdminFacadeTests.instance.getAllCompanies();
        System.out.println("\n" + "****Get Company Details Test****");
        AdminFacadeTests.instance.getCompanyDetailsTest();
        System.out.println("\n" + "****Create new Customer Test****");
        AdminFacadeTests.instance.addNewCustomer();
        System.out.println("\n" + "****Update Customer Test****");
        AdminFacadeTests.instance.updateCustomerTest();
        System.out.println("\n" + "****Delete Customer Test****");
        AdminFacadeTests.instance.deleteCustomerTest();
        System.out.println("\n" + "****Get All Customers Test****");
        AdminFacadeTests.instance.getAllCustomers();
        System.out.println("\n" + "****Get Company Details Test****");
        AdminFacadeTests.instance.getCustomerDetailsTest();
    }


}
