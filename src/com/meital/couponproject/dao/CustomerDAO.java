package com.meital.couponproject.dao;

import com.meital.couponproject.Enum.CrudOperation;
import com.meital.couponproject.Enum.EntityType;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.connection.ConnectionPool;
import com.meital.couponproject.exceptions.ApplicationException;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.util.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO extends UserDAO<Long, Customer> {

    public static final CustomerDAO instance = new CustomerDAO();
    private final ConnectionPool connectionPool;

    //----------private constructor and establish connection by using JDBCUtil-----------
    private CustomerDAO() throws ApplicationException {
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            throw new ApplicationException(Error.CONNECTION_ERROR, "Failed to establish connection with database");
        }
    }

    //---------------Check if a customer exists by email and password------------------
    public boolean isExists(String email, String password) {
        Connection connection = null;
        try {
            //sql query to search customer from customers table, by email and password
            final String sqlStatement = "SELECT * FROM customers WHERE email = ? AND password = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, String.valueOf(password.hashCode()));
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find customer");
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //------------------------Check if a customer exists by ID-------------------------
    public boolean isExists(Long id) {
        Connection connection = null;
        try {
            final String sqlStatement = "SELECT * FROM customers WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);

            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find customer");
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Create new customer ---------------------------------
    @Override
    public Long create(Customer customer) throws CrudException {
        Connection connection = null;
        try {
            final String sqlStatement = "INSERT INTO customers (first_name, last_name, email, password) VALUES(?, ?, ?, ?)";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, String.valueOf(customer.getPassword().hashCode()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new ApplicationException(Error.AUTO_INCREMENT_ERROR, "Failed to retrieve auto-incremented id");
            }
            return generatedKeysResult.getLong(1);
        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.CREATE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------------update customer ---------------------------------------
    @Override
    public void update(Customer customer) throws CrudException {
        Connection connection = null;
        try {
            final String sqlStatement = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, String.valueOf(customer.getPassword().hashCode()));
            preparedStatement.setLong(5, customer.getId());
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.UPDATE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------delete customer -------------------------------------
    @Override
    public void delete(Long id) throws CrudException {
        Connection connection = null;
        try {
            final String sqlStatement = "DELETE FROM customers WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.DELETE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------list of customers----------------------------------
    @Override
    public ArrayList<Customer> readAll() throws CrudException {
        Connection connection = null;
        try {
            final String sqlStatement = "SELECT * FROM customers";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Customer> customers = new ArrayList<>();

            while (result.next()) {
                //add customers to customers array list by using object extraction util
                customers.add(ObjectExtractionUtil.resultToCustomer(result));
            }
            return customers;
        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.READ_ALL);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //------------------------------return customer by ID ---------------------------------
    @Override
    public Customer read(Long id) throws CrudException {
        Connection connection = null;
        try {
            final String sqlStatement = "SELECT * FROM customers WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                //return customer object by using object extraction util
                return ObjectExtractionUtil.resultToCustomer(result);
            }
            throw new ApplicationException(Error.SEARCH_ERROR, "Customer doesn't exists");
        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.READ);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }
}
