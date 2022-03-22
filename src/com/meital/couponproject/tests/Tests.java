package com.meital.couponproject.tests;

import com.meital.couponproject.connection.ConnectionPool;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.login.LoginManager;
import com.meital.couponproject.threads.CouponExpirationDailyJob;
import com.meital.couponproject.util.DataBaseInitializer;
import lombok.Data;

import java.sql.SQLException;
import java.text.ParseException;
@Data
public class Tests {

    public static final Tests instance = new Tests();


    private final Thread couponExpirationDailyJob;

    private Tests() {
        couponExpirationDailyJob = new Thread(new CouponExpirationDailyJob());
    }

    public void testAll() throws InterruptedException, SQLException {

        try {
            DataBaseInitializer.dataBaseRestart();
            couponExpirationDailyJob.start();
            System.out.println();
            LoginManagerTests.instance.adminLoginManagerTest();
            System.out.println();
            AdminFacadeTests.instance.adminFacadeTestAll();
            LoginManagerTests.instance.companyLoginManagerTest();
            System.out.println();
            CompanyFacadeTests.instance.companyFacadeTestAll();
            LoginManagerTests.instance.customerLoginManagerTest();
            System.out.println();
            CustomerFacadeTests.instance.customerFacadeTestAll();
            couponExpirationDailyJob.stop();
            ConnectionPool.getInstance().closeAllConnections();

        } catch (CrudException | ParseException e) {
            System.err.println("Failed to Test all ");
            DataBaseInitializer.dropAllTables();
        }
    }
}
