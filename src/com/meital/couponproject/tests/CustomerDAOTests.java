package com.meital.couponproject.tests;

import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.tests.config.CustomerConfig;
import com.meital.couponproject.util.DataBaseInitializer;

import java.util.ArrayList;

public class CustomerDAOTests {
    public static CustomerDAOTests instance = new CustomerDAOTests();
    private final CustomerDAO customerDAO = CustomerDAO.instance;

    //--------------------------------Test create Customer and read customer
    public void createCustomerTest() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long newCustomerId = customerDAO.create(customer);

        Customer newCustomer = customerDAO.read(newCustomerId);

        if (newCustomerId.equals(newCustomer.getId())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test update Customer
    public void updateCustomerTest() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long newCustomerId = customerDAO.create(customer);

        Customer updatedCustomer = customerDAO.read(newCustomerId);
        updatedCustomer.setFirstName("test");
        customerDAO.update(updatedCustomer);

        customerDAO.read(newCustomerId);
        if (updatedCustomer.getFirstName().equals(customerDAO.read(newCustomerId).getFirstName())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test delete Customer
    public void deleteCustomerTest() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long customerId = customerDAO.create(customer);

        customerDAO.delete(customerId);
        if (!customerDAO.isExists(customerId)) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all Companies
    public void readAllCustomersTest() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long newCustomerId = customerDAO.create(customer);

        Customer customer2 = new Customer(CustomerConfig.customer2FirstName, CustomerConfig.customer2LastName, CustomerConfig.customer2Email, CustomerConfig.customer2Password);
        Long c2 = customerDAO.create(customer2);
        ArrayList<Customer> customers = customerDAO.readAll();

        if (!customers.isEmpty()) {
            System.out.println(customers);
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //-------------------------- Test Is  customer Exist By Customer ID
    public void isCustomerExistByIdTest() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);
        Long newCustomerId = customerDAO.create(customer);

        boolean isCustomerExist = customerDAO.isExists(newCustomerId);

        if (isCustomerExist) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //-------------------------- Test Is  company Exist By email and password
    public void isCompanyExistByEmailAndPasswordTest() throws CrudException {
        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);

        customerDAO.create(customer);

        boolean isCompanyExist = customerDAO.isExists(customer.getEmail(), customer.getPassword());
        if (isCompanyExist) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

}
