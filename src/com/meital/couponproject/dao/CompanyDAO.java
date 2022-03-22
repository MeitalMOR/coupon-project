package com.meital.couponproject.dao;

import com.meital.couponproject.Enum.CrudOperation;
import com.meital.couponproject.Enum.EntityType;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.connection.ConnectionPool;
import com.meital.couponproject.exceptions.ApplicationException;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.util.*;

import java.sql.*;
import java.util.ArrayList;

public class CompanyDAO extends UserDAO<Long, Company> {

    public static final CompanyDAO instance = new CompanyDAO();
    private final ConnectionPool connectionPool;


    //---------private constructor and establish connection by using Connection Pool-----------
    private CompanyDAO() {
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            throw new ApplicationException(Error.CONNECTION_ERROR, "Failed to establish connection pool with database");
        }
    }

    //---------------Check if a company exists by email and password--------------------------
    public boolean isExists(String email, String password) {
        Connection connection = null;
        try {
            //sql query to search company from companies table, by email and password
            final String sqlStatement = "SELECT * FROM companies WHERE email = ? AND password = ?";

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

            //return true if company exist
            return true;

        } catch (final Exception e) {
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find company");

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //---------------Check if a company exists by ID--------------------------
    public boolean isExists(Long companyId) {
        Connection connection = null;
        try {
            //sql query to search company from companies table, by email and password
            final String sqlStatement = "SELECT * FROM companies WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setLong(1, companyId);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return false;
            }

            //return true if company exist
            return true;

        } catch (final Exception e) {
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find company");

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //---------------Check if a company exists by company name--------------------------
    public boolean isExists(String companyName) {
        Connection connection = null;
        try {
            //sql query to search company from companies table, by email and password
            final String sqlStatement = "SELECT * FROM companies WHERE name = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            //insert properties to sql statement
            preparedStatement.setString(1, companyName);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return false;
            }

            //return true if company exist
            return true;

        } catch (final Exception e) {
            throw new ApplicationException(Error.SEARCH_ERROR, "Failed to find company");

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //------------------------------------Create new company -------------------------------
    @Override
    public Long create(Company company) throws CrudException {
        Connection connection = null;
        try {
            //sql query to create company by insert company properties
            final String sqlStatement = "INSERT INTO companies (name, email, password) VALUES(?, ?, ?)";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, String.valueOf(company.getPassword().hashCode()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new ApplicationException(Error.AUTO_INCREMENT_ERROR, "Failed to retrieve auto-incremented id");
            }

            // create new company at database and return the id of the company
            return generatedKeysResult.getLong(1);

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COMPANY, CrudOperation.CREATE);

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //-----------------------------------update company ------------------------------------
    @Override
    public void update(Company company) throws CrudException {
        Connection connection = null;
        try {
            //sql query to update company by insert  new company properties
            final String sqlStatement = "UPDATE companies SET  email = ?, password = ? WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, String.valueOf(company.getPassword().hashCode()));
            preparedStatement.setLong(3, company.getId());
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COMPANY, CrudOperation.UPDATE);

        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------delete company -------------------------------------
    @Override
    public void delete(Long id) throws CrudException {
        Connection connection = null;
        try {
            //sql query to delete company by insert  company id
            final String sqlStatement = "DELETE FROM companies WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COMPANY, CrudOperation.DELETE);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------list of companies----------------------------------
    @Override
    public ArrayList<Company> readAll() throws CrudException {
        Connection connection = null;
        try {
            //sql query to read all companies in mySQL database
            final String sqlStatement = "SELECT * FROM companies";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Company> companies = new ArrayList<>();

            while (result.next()) {
                //add companies to companies array list by using object extraction util
                companies.add(ObjectExtractionUtil.resultToCompany(result));
            }

            return companies;
        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COMPANY, CrudOperation.READ_ALL);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------return company from table by ID --------------------
    @Override
    public Company read(Long id) throws CrudException {
        Connection connection = null;
        try {
            //sql query to read one company in mySQL database
            final String sqlStatement = "SELECT * FROM companies WHERE id = ?";

            //get connection to database using connection pool
            connection = ConnectionPool.getInstance().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            //insert properties to sql statement
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                //return company object by using object extraction util
                return ObjectExtractionUtil.resultToCompany(result);
            }
            throw new ApplicationException(Error.SEARCH_ERROR, "Company doesn't exists");
        } catch (final Exception e) {
            //throw crud exception if the crud operation didn't succeed
            throw new CrudException(EntityType.COMPANY, CrudOperation.READ);
        } finally {
            //return connection to connection pool
            connectionPool.returnConnection(connection);
        }
    }
}
