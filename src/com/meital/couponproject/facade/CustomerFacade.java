package com.meital.couponproject.facade;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.dao.CouponDAO;
import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.ApplicationException;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.util.InputValidationUtil;


import java.util.ArrayList;
import java.util.Date;

public class CustomerFacade extends UserFacade {
    private final static int OUT_OF_STOCK = 0;
    private final static int ONE_COUPON = 1;

    public static final CustomerFacade instance = new CustomerFacade();

    private final CustomerDAO customerDAO = CustomerDAO.instance;
    private final CouponDAO couponDAO = CouponDAO.instance;

    public CustomerFacade() {

    }

    //----------------------customer login -------------------------------------
    @Override
    public boolean login(String email, String password) {

        //check if email is valid by regex
        if (!InputValidationUtil.isEmailValid(email)) {
            throw new ApplicationException(Error.NOT_VALID_EMAIL, "Email is not valid");
        }

        //check if password is valid by regex
        if (!InputValidationUtil.isPasswordValid(password)) {
            throw new ApplicationException(Error.NOT_VALID_PASSWORD, "Email is not valid");
        }

        //log in by using isExists method in customerDAO
        if (!customerDAO.isExists(email, password)) {
            throw new ApplicationException(Error.LOG_IN, "Failed to log in");
        }

        return true;
    }

    //--------------------------------purchase coupon---------------------------
    public void purchaseCoupon(Long customerId, Coupon coupon) throws CrudException {

        //check if coupon doesn't exist, and throw exception
        if (!couponDAO.isExists(coupon.getId())) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The coupon doesn't exists");
        }

        //check if coupon is out of stock
        if (couponDAO.read(coupon.getId()).getAmount() == OUT_OF_STOCK) {
            throw new ApplicationException(Error.OUT_OF_STOCK, "Coupon out of stock");
        }

        //check if coupon has expired
        if (coupon.getEndDate().before(new Date())) {
            throw new ApplicationException(Error.EXPIRED, "Coupon has expired");
        }

        //check if customer doesn't have  the same coupon
        if (!couponDAO.isCouponExistToCustomer(customerId, coupon.getId())) {

            //decrease the amount of coupon
            coupon.setAmount(coupon.getAmount() - ONE_COUPON);

            couponDAO.addCouponPurchase(customerId, coupon.getId());
            couponDAO.update(coupon);
            System.out.println("Purchase succeeded");

        } else {
            throw new ApplicationException(Error.ENTITY_EXIST, "You already purchased this coupon");
        }
    }

    //------------------get all coupon for customer that logged in--------------
    public ArrayList<Coupon> getCustomerCoupons(Long customerId) throws CrudException {

        //check if customer  doesn't exist, and throw exception
        if (!customerDAO.isExists(customerId)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The customer doesn't exist");
        }

        ArrayList<Coupon> couponsForCustomer = couponDAO.couponsForCustomer(customerId);
        if (couponsForCustomer == null) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "There are no coupon for this customer");
        }

        //list of coupons for the customer
        return couponsForCustomer;
    }


    //---------get all coupon for specific category for customer that logged in------------
    public ArrayList<Coupon> getCustomerCouponsByCategory(Long customerId, Category category) throws CrudException {

        //check if customer  doesn't exist, and throw exception
        if (!customerDAO.isExists(customerId)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The customer doesn't exist");
        }

        ArrayList<Coupon> customerCoupon = couponDAO.couponsForCustomer(customerId);
        ArrayList<Coupon> categoryCoupon = new ArrayList<>();

        //add coupons to new array by the chosen category
        for (Coupon coupon : customerCoupon) {
            if (coupon.getCategory().equals(category)) {
                categoryCoupon.add(coupon);
            }
        }

        //check if the new category array is empty
        if (categoryCoupon.isEmpty()) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "There are no coupon for this customer");
        }

        return categoryCoupon;
    }

    //------get all coupon with lower price from maximum price of the customer that logged in--------
    public ArrayList<Coupon> getCustomerCouponsByMaxPrice(Long customerId, double maxPrice) throws CrudException {

        //check if customer  doesn't exist, and throw exception
        if (!customerDAO.isExists(customerId)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The customer doesn't exist");
        }

        //new arraylist for max price coupons
        ArrayList<Coupon> couponsForCustomer = couponDAO.couponsForCustomer(customerId);

        ArrayList<Coupon> couponByMaxPrice = new ArrayList<>();

        //add to new coupon list coupons that their price under max price
        for (Coupon coupon : couponsForCustomer) {
            if (coupon.getPrice() <= maxPrice) {
                couponByMaxPrice.add(coupon);
            }
        }

        //check if the new category array is empty
        if (couponByMaxPrice.isEmpty()) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "There are no coupon for this customer");
        }

        return couponByMaxPrice;
    }

    //-------------------------get all customer details ----------------------------------
    public Customer getCustomerDetails(Long customerId, boolean withCoupon) throws CrudException {

        //check if customer  doesn't exist, and throw exception
        if (customerDAO.isExists(customerId)) {
            Customer customer = customerDAO.read(customerId);
            //if with coupons true - add coupons to customer
            if (withCoupon) {
                addCouponToCustomer(customer);
            }
            return customer;

        } else {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The customer doesn't exist");
        }
    }

    //add coupons to customer method - belongs to  read operation
    public void addCouponToCustomer(Customer customer) throws CrudException {
        final ArrayList<Coupon> coupons = couponDAO.couponsForCustomer(customer.getId());
        customer.setCoupons(coupons);
    }

}
