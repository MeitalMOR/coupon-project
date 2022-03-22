package com.meital.couponproject.facade;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.dao.CompanyDAO;
import com.meital.couponproject.dao.CouponDAO;
import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.ApplicationException;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.util.InputValidationUtil;


import java.util.ArrayList;

public class CompanyFacade extends UserFacade {
    private final static int NOT_VALID_COUPON_ID = 0;
    public static final CompanyFacade instance = new CompanyFacade();

    private final CompanyDAO companyDAO = CompanyDAO.instance;
    private final CouponDAO couponDAO = CouponDAO.instance;
    private final CustomerDAO customerDAO = CustomerDAO.instance;


    public CompanyFacade() {
    }

    //----------------------company login -------------------------------------
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
        if (!companyDAO.isExists(email, password)) {
            throw new ApplicationException(Error.LOG_IN, "Failed to log in");
        }

        return true;
    }

    //----------------add new coupon to company------------------------------
    public void addNewCoupon(Coupon coupon) throws CrudException {

        //check if title already exist in the new coupon company and throw exception if true
        if (couponDAO.isExists(coupon.getTitle())) {
            throw new ApplicationException(Error.ENTITY_EXIST, "This title of coupon is already exist in this company");
        }

        //check if company in coupon doesn't exist in the new exception if true
        if (!companyDAO.isExists(coupon.getCompanyId())) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The company you insert to the new coupon doesn't exists");
        }

        //create coupon
        long newCouponID = couponDAO.create(coupon);

        //print success massage
        if (newCouponID > NOT_VALID_COUPON_ID) {
            System.out.println("A new coupon: " + coupon.getTitle() + " added");
        }
    }

    //--------------------------update coupon----------------------------------
    public void updateCoupon(Coupon coupon) throws CrudException {

        //check if coupon to update doesn't exist, and throw exception
        if (!couponDAO.isExists(coupon.getId())) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The coupon doesn't exists");
        }

        couponDAO.update(coupon);
        System.out.println("coupon " + coupon.getTitle() + " updated");
    }

    //-------------------------------delete coupon-----------------------------------------
    public void deleteCoupon(Long couponId) throws CrudException {

        //check if coupon to update doesn't exist, and throw exception
        if (!couponDAO.isExists(couponId)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The coupon doesn't exists");
        }

        //delete coupon from purchase history
        couponDAO.deleteCouponPurchaseByCouponID(couponId);

        //delete coupon from coupons table
        couponDAO.delete(couponId);
        System.out.println("Coupon" + couponId + " deleted");
    }

    //-----------------------------get all coupon for company -----------------------------
    public ArrayList<Coupon> getCompanyCoupons(Long companyId) throws CrudException {

        //check if company doesn't exist, and throw exception
        if (!companyDAO.isExists(companyId)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The company you insert doesn't exists");
        }

        ArrayList<Coupon> companyCoupons = couponDAO.readCompanyCoupons(companyId);

        //if the company has no coupons throw exception
        if (companyCoupons == null) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The company has no coupons");
        }

        //list of coupons for the company
        return companyCoupons;
    }

    //-----------------------------get all coupon for specific category --------------------------
    public ArrayList<Coupon> getCompanyCouponsByCategory(Long companyId, Category category) throws CrudException {

        //check if company doesn't exist, and throw exception
        if (!companyDAO.isExists(companyId)) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The company you insert doesn't exists");
        }

        ArrayList<Coupon> categoryCoupon = couponDAO.readCompanyCouponsByCategory(companyId, category);

        //if the company has no coupons throw exception
        if (categoryCoupon == null) {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The company has no coupons in " + category + " category");
        }

        return categoryCoupon;
    }

    //-------------------get all coupon with lower price from maximum price ---------------------
    public ArrayList<Coupon> getCompanyCouponsByMaxPrice(Long companyId, double maxPrice) throws CrudException {

        //get all coupon for company
        ArrayList<Coupon> companyCoupons = couponDAO.readCompanyCoupons(companyId);

        //new arraylist for max price coupons
        ArrayList<Coupon> lowerThanMaxCoupons = new ArrayList<>();

        //check if the price of the coupon is lower than max
        for (Coupon coupon : companyCoupons) {
            if (coupon.getPrice() < maxPrice) {
                lowerThanMaxCoupons.add(coupon);
            }
        }

        return lowerThanMaxCoupons;
    }

    //-------------------------get all company details ----------------------------------
    public Company getCompanyDetails(Long companyId, boolean withCoupon) throws CrudException {

        //check if company doesn't exist, and throw exception
        if (companyDAO.isExists(companyId)) {
            Company company = companyDAO.read(companyId);
            //if with coupons true - add coupons to company
            if (withCoupon) {
                addCouponToCompany(company);
            }
            return company;
        } else {
            throw new ApplicationException(Error.ENTITY_NOT_FOUND, "The company you insert doesn't exists");
        }
    }

    //add coupons to company method - belongs to  read operation
    public void addCouponToCompany(Company company) throws CrudException {
        final ArrayList<Coupon> coupons = couponDAO.readCompanyCoupons(company.getId());
        company.setCoupons(coupons);
    }
}
