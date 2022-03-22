package com.meital.couponproject.facade;

import com.meital.couponproject.Enum.CrudOperation;
import com.meital.couponproject.Enum.EntityType;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.dao.CompanyDAO;
import com.meital.couponproject.dao.CouponDAO;
import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.ApplicationException;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.util.InputValidationUtil;

import java.util.ArrayList;

public class AdminFacade extends UserFacade {
    private final static String ADMIN_EMAIL = "admin@admin.com";
    private final static String ADMIN_PASSWORD = "admin";
    public final static int NOT_VALID_ID = 0;

    public static final AdminFacade instance = new AdminFacade();

    private final CustomerDAO customerDAO = CustomerDAO.instance;
    private final CouponDAO couponDAO = CouponDAO.instance;
    private final CompanyDAO companyDAO = CompanyDAO.instance;

    public AdminFacade() {
    }

    //----------------------------admin login -------------------------------------
    @Override
    public boolean login(String email, String password) {
        //check if email is valid by regex
        if (!InputValidationUtil.isEmailValid(email)) {
            throw new ApplicationException(Error.NOT_VALID_EMAIL, "Email is not valid");
        }

        //check if password is valid by regex
        if (!InputValidationUtil.isPasswordValid(password)) {
            throw new ApplicationException(Error.NOT_VALID_PASSWORD, "Password is not valid");
        }

        //log in by using isExists method in companyDAO
        if (!email.equals(ADMIN_EMAIL) || !password.equals(ADMIN_PASSWORD)) {
            throw new ApplicationException(Error.LOG_IN, "Failed to log in");
        }

        return true;
    }

    //--------------------------------create new company ----------------------------
    public Long addCompany(Company company) throws CrudException {

        //check if company exists by email
        if (companyDAO.isExists(company.getEmail(), company.getPassword())) {
            throw new ApplicationException(Error.ENTITY_EXIST, "Company with this email is already exists");
        }

        //check if company exists by name
        if (companyDAO.isExists(company.getName())) {
            throw new ApplicationException(Error.ENTITY_EXIST, "Company with this name is already exists");
        }

        //check if email is valid by regex
        if (!InputValidationUtil.isEmailValid(company.getEmail())) {
            throw new ApplicationException(Error.NOT_VALID_EMAIL, "Email is not valid");
        }

        //check if password is valid by regex
        if (!InputValidationUtil.isPasswordValid(company.getPassword())) {
            throw new ApplicationException(Error.NOT_VALID_PASSWORD, "Password is not valid");
        }

        return companyDAO.create(company);
    }

    //--------------------------------update company --------------------------------
    public void updateCompany(Company company) throws CrudException {

        //check if the company doesn't exist
        if (!companyDAO.isExists(company.getId())) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "Company doesn't exist");
        }

        Company companyUpdate = companyDAO.read(company.getId());
        //make sure the company name is not changed
        if (!company.getName().equals(companyUpdate.getName())) {
            throw new CrudException(EntityType.COMPANY, CrudOperation.UPDATE);
        }

        companyDAO.update(company);
        System.out.println("Company " + company.getName() + " was updated");
    }

    //----------------------------delete company ------------------------------------
    public void deleteCompany(Long companyID) throws CrudException {

        //check if company  doesn't exist throw exception
        if (!companyDAO.isExists(companyID)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "Company doesn't exist");
        }

        //delete coupons of the company from companies table and purchase history
        ArrayList<Coupon> companyCoupons = couponDAO.readCompanyCoupons(companyID);
        for (Coupon coupon : companyCoupons) {
            couponDAO.deleteCouponPurchaseByCouponID(coupon.getId());
            couponDAO.delete(coupon.getId());
        }

        //delete company from companies table
        companyDAO.delete(companyID);

        System.out.println("Company deleted");
    }

    //-----------------------------get all companies ---------------------------------
    public ArrayList<Company> getAllCompanies() throws CrudException {

        //check if there are no companies
        if (companyDAO.readAll() == null) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "There are no companies at database");
        }

        return companyDAO.readAll();
    }

    //-------------------------------get one company ---------------------------------
    public Company getOneCompany(Long companyID, boolean withCoupons) throws CrudException {

        //check if company doesn't exist, and throw exception
        if (companyDAO.isExists(companyID)) {
            Company company = companyDAO.read(companyID);
            //if with coupons true - add coupons to customer
            if (withCoupons) {
                addCouponToCompany(company);
            }
            return company;
        } else {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The company you insert doesn't exists");
        }
    }

    //add coupons to company method  - belongs to  read operation
    public void addCouponToCompany(Company company) throws CrudException {
        final ArrayList<Coupon> coupons = couponDAO.readCompanyCoupons(company.getId());
        company.setCoupons(coupons);
    }

    //-----------------------------add new customer-----------------------------------
    public Long addNewCustomer(Customer customer) throws CrudException {

        //check if customer exists by email
        if (companyDAO.isExists(customer.getEmail(), customer.getPassword())) {
            throw new ApplicationException(Error.ENTITY_EXIST, "Customer is already exists");
        }

        //check if email is valid by regex
        if (!InputValidationUtil.isEmailValid(customer.getEmail())) {
            throw new ApplicationException(Error.NOT_VALID_EMAIL, "Email is not valid");
        }

        //check if password is valid by regex
        if (!InputValidationUtil.isPasswordValid(customer.getPassword())) {
            throw new ApplicationException(Error.NOT_VALID_PASSWORD, "password is not valid");
        }

        //create new customer and save id new id
        Long newCustomerId = customerDAO.create(customer);

        //check if new id larger than 0, means the system success to give new id to the customer
        if (newCustomerId > NOT_VALID_ID) {
            System.out.println("New customer created successfully");
        }

        return newCustomerId;
    }

    //--------------------------------update customer --------------------------------
    public void updateCustomer(Customer customer) throws CrudException {

        //check if the customer exist
        if (!customerDAO.isExists(customer.getId())) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "Customer doesn't exist");
        }

        customerDAO.update(customer);
        System.out.println("Customer " + customer.getFirstName() + " " + customer.getLastName() + " was updated");
    }

    //--------------------------delete customer ---------------------------------------
    public void deleteCustomer(Long customerID) throws CrudException {

        //check if the customer exist
        if (!customerDAO.isExists(customerID)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "Customer doesn't exist");
        }

        //delete coupon purchases for the customer
        couponDAO.deleteCouponPurchase(customerID);

        //delete customer
        customerDAO.delete(customerID);
        System.out.println("Succeeded to delete customer");
    }

    //---------------------------get all customers ----------------------------
    public ArrayList<Customer> getAllCustomers() throws CrudException {

        //check if there are no customers
        if (customerDAO.readAll() == null) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "No customers exists");
        }

        return customerDAO.readAll();
    }

    //--------------------------get one customer by ID ------------------------
    public Customer getOneCustomer(Long customerID, boolean withCoupons) throws CrudException {

        //check if the customer exist
        if (customerDAO.isExists(customerID)) {
            Customer customer = customerDAO.read(customerID);
            //if with coupons true - add coupons to customer
            if (withCoupons) {
                addCouponToCustomer(customer);
            }
            return customer;
        } else {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The customer you insert doesn't exists");
        }
    }

    //add coupons to customer method  - belongs to  read operation
    public void addCouponToCustomer(Customer customer) throws CrudException {
        final ArrayList<Coupon> coupons = couponDAO.couponsForCustomer(customer.getId());
        customer.setCoupons(coupons);
    }

}


