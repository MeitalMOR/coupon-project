package com.meital.couponproject.util;

import com.meital.couponproject.Enum.Category;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.connection.ConnectionPool;
import com.meital.couponproject.exceptions.ApplicationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseInitializer {

    public static final DataBaseInitializer instance = new DataBaseInitializer();
    private static ConnectionPool connectionPool;

    private DataBaseInitializer() {
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            throw new ApplicationException(Error.CONNECTION_ERROR, "Failed to establish connection pool with database");
        }
    }

    public static void dropAllTables() throws InterruptedException, SQLException {
        try {
            String query = "DROP TABLE `coupon_project`.`customers`,`coupon_project`.`companies`,`coupon_project`.`coupons`, `coupon_project`.`coupons_to_customers`, `coupon_project`.`categories` ";
            ConnectionPool.getInstance().getConnection().prepareStatement(query).execute();

        } catch (final SQLException e) {
            System.err.println("Failed to drop schema");
        } finally {
            connectionPool.returnConnection(ConnectionPool.getInstance().getConnection());
        }
    }

    //--------------------create new companies table in database----------------------
    private static String createCompaniesTable() {
        return "CREATE TABLE `coupon_project`.`companies` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  `email` VARCHAR(45) NOT NULL,\n" +
                "  `password` INT NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);\n";
    }

    //--------------------create new customers table in database----------------------
    private static String createCustomersTable() {
        return "CREATE TABLE `coupon_project`.`customers` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `first_name` VARCHAR(45) NOT NULL,\n" +
                "  `last_name` VARCHAR(45) NOT NULL,\n" +
                "  `email` VARCHAR(45) NOT NULL,\n" +
                "  `password` INT NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);";

    }

    private static String createCategoriesTable() {
        return "CREATE TABLE `coupon_project`.`categories` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);";
    }

    private static void createCategories() throws SQLException, InterruptedException {
        String[] categories = {"FOOD", "ELECTRICITY", "RESTAURANT", "VACATION", "SPA_AND_BEAUTY", "THINGS_TO_DO_CHILDREN", "HOME_DECOR"};

        for (String categoryName : categories) {
            String insertQuery = "INSERT INTO coupon_project.categories (name)" + " VALUES ('" + categoryName + "')";
            connectionPool.getConnection().prepareStatement(insertQuery).executeUpdate();
        }
    }

    private static String createCouponsTable() {
        return "CREATE TABLE `coupon_project`.`coupons` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `company_id` BIGINT NOT NULL,\n" +
                "  `category` VARCHAR(45) NOT NULL,\n" +
                "  `title` VARCHAR(45) NOT NULL,\n" +
                "  `description` VARCHAR(45) NULL,\n" +
                "  `start_date` VARCHAR(45) NOT NULL,\n" +
                "  `end_date` VARCHAR(45) NOT NULL,\n" +
                "  `amount` INT NOT NULL,\n" +
                "  `price` DOUBLE NOT NULL,\n" +
                "  `image` VARCHAR(45) NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE,\n" +
                "  INDEX `company_id_idx` (`company_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `company_id`\n" +
                "    FOREIGN KEY (`company_id`)\n" +
                "    REFERENCES `coupon_project`.`companies` (`id`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";
    }

    private static String createCouponsToCustomersTable() {
        return "CREATE TABLE `coupon_project`.`coupons_to_customers` (\n" +
                "  `customer_id` BIGINT NULL,\n" +
                "  `coupon_id` BIGINT NULL,\n" +
                "  INDEX `customer_id_idx` (`customer_id` ASC) VISIBLE,\n" +
                "  INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `customer_id`\n" +
                "    FOREIGN KEY (`customer_id`)\n" +
                "    REFERENCES `coupon_project`.`customers` (`id`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `coupon_id`\n" +
                "    FOREIGN KEY (`coupon_id`)\n" +
                "    REFERENCES `coupon_project`.`coupons` (`id`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";
    }

    public static void createTables() {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();

            Statement statement = connection.createStatement();
            String categoriesTable = createCategoriesTable();
            String companiesTable = createCompaniesTable();
            String couponsTable = createCouponsTable();
            String customersTable = createCustomersTable();
            String couponsToCustomersTable = createCouponsToCustomersTable();

            statement.executeUpdate(categoriesTable);
            statement.executeUpdate(companiesTable);
            statement.executeUpdate(couponsTable);
            statement.executeUpdate(customersTable);
            statement.executeUpdate(couponsToCustomersTable);
            //createCategories();
            //System.out.println("Table created");
        } catch (SQLException | InterruptedException e) {
            throw new ApplicationException(Error.CONNECTION_ERROR, "Failed to create tables");
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    public static void dataBaseRestart() {
        try {
            DataBaseInitializer.dropAllTables();
            DataBaseInitializer.createTables();
        } catch (InterruptedException | SQLException e) {
            System.err.println("Failed to restart schema");
        }
    }
}
