package com.meital.couponproject.threads;

import com.meital.couponproject.dao.CouponDAO;
import com.meital.couponproject.model.Coupon;
import lombok.SneakyThrows;

import java.util.Date;

public class CouponExpirationDailyJob implements Runnable {
    public final static int THREE_HOURS = 10800000;

    private final CouponDAO couponDAO;
    private boolean quit;
    Date date = new Date();

    public CouponExpirationDailyJob() {
        this.couponDAO = CouponDAO.instance;
    }


    @SneakyThrows
    @Override
    public void run() {

        while (!quit) {
            System.out.println("Daily job started");
            try {
                Thread.sleep(THREE_HOURS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Coupon coupon : couponDAO.readAll()) {
                //search in coupon table, coupons that expired
                if (coupon.getEndDate().before(date)){

                    //delete from purchase history
                    couponDAO.deleteCouponPurchaseByCouponID(coupon.getId());

                    //delete coupon
                    couponDAO.delete(coupon.getId());
                    System.out.println("Delete expired Coupon" + coupon.getTitle());
                }
            }
        }
    }

    public boolean stop() {
        return quit = true;
    }
}
