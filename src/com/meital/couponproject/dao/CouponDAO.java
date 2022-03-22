package com.meital.couponproject.dao;

import com.meital.couponproject.Enum.*;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.connection.ConnectionPool;
import com.meital.couponproject.exceptions.ApplicationException;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Coupon;
import com.meital.couponproject.util.ObjectExtractionUtil;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO implements CrudDAO<Long, Coupon> {

    public static final CouponDAO instance = new CouponDAO();
    private final ConnectionPool connectionPool;

    //----------private constructor and establish connection by using connection pool-----------
    private CouponDAO() {
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            throw new ApplicationException(Error.CONNECTION_ERROR, "Failed to establish connection with database");
        }
    }

    //------------------------Check if a coupon exists by his title--------------------------
    public boolean isExists(String title) {
        Connection connection = null;
        try {
            //sql query to search coupons by  coupon title
            final String sqlStatement = "SELECT * FROM coupons WHERE title = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setString(1, title);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return false;
            }

            //return true if coupon exist
            return true;

        } catch (final Exception e) {
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find company");

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //-----------------------Check if a coupon exists by his ID--------------------------
    public boolean isExists(Long id) {
        Connection connection = null;
        try {
            //sql query to search coupons from by coupon id
            final String sqlStatement = "SELECT * FROM coupons WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return false;
            }

            //return true if coupon exist
            return true;

        } catch (final Exception e) {
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find company");

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------create new coupon ----------------------------
    @Override
    public Long create(Coupon coupon) throws CrudException {
        Connection connection = null;
        try {
            //sql query to insert coupons properties to a new coupon
            final String sqlStatement = "INSERT INTO coupons (company_id, category, title, description, start_date," +
                    " end_date, amount, price, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            //format and parse date to a specific pattern
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, coupon.getCompanyId());
            preparedStatement.setString(2, coupon.getCategory().toString());
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setString(5, dateFormat.format(coupon.getStartDate()));
            preparedStatement.setString(6, dateFormat.format(coupon.getEndDate()));
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new ApplicationException(Error.AUTO_INCREMENT_ERROR, "Failed to retrieve auto-incremented id");
            }
            //return new coupon that created id
            return generatedKeysResult.getLong(1);

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.CREATE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------update coupon -----------------------------------
    @Override
    public void update(Coupon coupon) throws CrudException {
        Connection connection = null;
        try {
            //sql query to update coupon by insert  new coupon properties
            final String sqlStatement = "UPDATE coupons SET  category = ?, title = ?, description = ?," +
                    " start_date = ?, end_date = ?, amount = ?, price = ?, image = ? WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            //format and parse date to a specific pattern
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setString(1, coupon.getCategory().toString());
            preparedStatement.setString(2, coupon.getTitle());
            preparedStatement.setString(3, coupon.getDescription());
            preparedStatement.setString(4, dateFormat.format(coupon.getStartDate()));
            preparedStatement.setString(5, dateFormat.format(coupon.getEndDate()));
            preparedStatement.setInt(6, coupon.getAmount());
            preparedStatement.setDouble(7, coupon.getPrice());
            preparedStatement.setString(8, coupon.getImage());
            preparedStatement.setLong(9, coupon.getId());
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.UPDATE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------delete coupon -------------------------------------
    @Override
    public void delete(Long id) throws CrudException {
        Connection connection = null;
        try {
            //sql query to delete coupon by insert  coupon id
            final String sqlStatement = "DELETE  FROM coupons WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.DELETE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------list of all coupons----------------------------------
    @Override
    public List<Coupon> readAll() throws CrudException {
        Connection connection = null;
        try {
            //sql query to read all coupons in mySQL database
            final String sqlStatement = "SELECT * FROM coupons";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            List<Coupon> coupons = new ArrayList<>();

            while (result.next()) {
                //add coupons to coupons array list by using object extraction util
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;

        } catch (Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.READ_ALL);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------list of coupons by company id ----------------------------------
    public ArrayList<Coupon> readCompanyCoupons(Long companyId) throws CrudException {
        Connection connection = null;
        try {
            //sql query to read all coupons by company id in mySQL database
            final String sqlStatement = "SELECT * FROM coupons WHERE company_id = ? ";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, companyId);
            ResultSet result = preparedStatement.executeQuery();

            ArrayList<Coupon> companyCoupons = new ArrayList<>();
            while (result.next()) {
                //add coupons to company coupons array list by using object extraction util
                companyCoupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return companyCoupons;

        } catch (Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.READ_ALL);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }


    //--------------------------------list of coupons for company  by category ----------------------------------
    public ArrayList<Coupon> readCompanyCouponsByCategory(Long companyId, Category category) throws CrudException {
        Connection connection = null;
        try {
            //sql query to read all coupons by company id  and category in mySQL database
            final String sqlStatement = "SELECT * FROM coupons WHERE company_id = ? AND category = ? ";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, companyId);
            preparedStatement.setString(2, category.toString());
            ResultSet result = preparedStatement.executeQuery();

            ArrayList<Coupon> categoryCoupons = new ArrayList<>();

            while (result.next()) {
                //add coupons to company category  coupons array list by using object extraction util
                categoryCoupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return categoryCoupons;

        } catch (Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.READ_ALL);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------list of coupons for customer by ID -------------------------------
    public ArrayList<Coupon> couponsForCustomer(Long customerId) throws CrudException {
        Connection connection = null;
        try {
            //sql query to read all customer coupons by customer id in mySQL database
            final String sqlStatement = "SELECT * FROM coupons JOIN coupons_to_customers ON customer_id = ? AND coupon_id =id ";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, customerId);
            ResultSet result = preparedStatement.executeQuery();

            ArrayList<Coupon> customerCoupons = new ArrayList<>();
            while (result.next()) {
                //add coupons to customer coupons array list by using object extraction util
                customerCoupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return customerCoupons;

        } catch (Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.READ_ALL);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //------------------------read coupon from table by coupon ID ---------------------------------
    @Override
    public Coupon read(Long id) throws CrudException {
        Connection connection = null;
        try {
            //sql query to read coupon by coupon id in mySQL database
            final String sqlStatement = "SELECT * FROM coupons WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                //return coupon object by using object extraction util
                return ObjectExtractionUtil.resultToCoupon(result);
            }
            throw new ApplicationException(Error.SEARCH_ERROR, "Coupon doesn't exists");

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.READ);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------add coupon purchase to database ----------------------
    public void addCouponPurchase(Long customerId, Long couponId) throws CrudException {
        Connection connection = null;
        try {
            //sql query to add coupon purchase by coupon id and customer id in mySQL database
            final String sqlStatement = "INSERT INTO coupons_to_customers (customer_id, coupon_id ) VALUES (?, ?) ";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, couponId);
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.CREATE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------delete coupon purchase by customer ID ----------------------
    public void deleteCouponPurchase(Long customerId) throws CrudException {
        Connection connection = null;
        try {
            //sql query to delete coupon purchase by customer id in mySQL database
            final String sqlStatement = "DELETE  FROM coupons_to_customers WHERE customer_id = ?  ";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, customerId);
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.DELETE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //------------------------------delete coupon purchase by coupon ID --------------------------------
    public void deleteCouponPurchaseByCouponID(Long couponId) throws CrudException {
        Connection connection = null;
        try {
            //sql query to delete coupon purchase by coupon id in mySQL database
            final String sqlStatement = "DELETE FROM coupons_to_customers WHERE coupon_id = ?  ";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, couponId);
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COUPON, CrudOperation.DELETE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------check if coupon exist to customer----------------------
    public boolean isCouponExistToCustomer(Long customerId, Long couponId) {
        Connection connection = null;
        try {
            //sql query to search company from companies table, by email and password
            final String sqlStatement = "SELECT * FROM coupons_to_customers WHERE customer_id = ? AND coupon_id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, couponId);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return false;
            }

            //return true if company exist
            return true;

        } catch (final Exception e) {
            e.printStackTrace();
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find company");

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }
}
