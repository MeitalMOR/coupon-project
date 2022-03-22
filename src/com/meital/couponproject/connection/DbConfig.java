package com.meital.couponproject.connection;

public class DbConfig {


    //url of database that used at connection pool
    public static String sqlUrl = "jdbc:mysql://localhost:3306/coupon_project?useSSL=false&serverTimezone=UTC";

    //user that used at connection pool
    public static String sqlUser = "root";

    //password that used at connection pool
    public static String sqlPassword = System.getenv("SQL_PASSWORD");


}
