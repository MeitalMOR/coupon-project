package com.meital.couponproject.tests;

import com.meital.couponproject.Enum.ClientType;
import com.meital.couponproject.dao.CompanyDAO;
import com.meital.couponproject.dao.CustomerDAO;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.facade.AdminFacade;
import com.meital.couponproject.facade.CompanyFacade;
import com.meital.couponproject.facade.CustomerFacade;
import com.meital.couponproject.facade.UserFacade;
import com.meital.couponproject.login.LoginManager;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.model.Customer;
import com.meital.couponproject.tests.config.AdminConfig;
import com.meital.couponproject.tests.config.CompanyConfig;
import com.meital.couponproject.tests.config.CustomerConfig;
import com.meital.couponproject.util.DataBaseInitializer;

public class LoginManagerTests {
    public static LoginManagerTests instance = new LoginManagerTests();

    private final LoginManager loginManager = LoginManager.instance;
    private final AdminFacade adminFacade = AdminFacade.instance;
    private final CompanyFacade companyFacade = CompanyFacade.instance;
    private final CustomerFacade customerFacade = CustomerFacade.instance;
    private final CustomerDAO customerDAO = CustomerDAO.instance;
    private final CompanyDAO companyDAO = CompanyDAO.instance;


    //--------------------------------------Test admin login
    public void adminLoginManagerTest() {

        UserFacade userFacade = LoginManager.instance.login(AdminConfig.ADMIN_EMAIL, AdminConfig.ADMIN_PASSWORD, ClientType.ADMIN);
        if (userFacade.equals(adminFacade)) {
            System.out.println("*********Admin login succeeded**********");
        }
        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------------Test customer login
    public void customerLoginManagerTest() throws CrudException {

        Customer customer = new Customer(CustomerConfig.customer1FirstName, CustomerConfig.customer1LastName,
                CustomerConfig.customer1Email, CustomerConfig.customer1Password);

        Long id = customerDAO.create(customer);

        UserFacade userFacade = LoginManager.instance.login(customer.getEmail(), customer.getPassword(), ClientType.CUSTOMER);
        if (userFacade.equals(customerFacade)) {
            System.out.println("*********Customer login succeeded**********");
        }
        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------------Test company login
    public void companyLoginManagerTest() throws CrudException {

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        UserFacade userFacade = LoginManager.instance.login(company.getEmail(), company.getPassword(), ClientType.COMPANY);
        if (userFacade.equals(companyFacade)) {
            System.out.println("*********Company login succeeded**********");
        }
        DataBaseInitializer.dataBaseRestart();
    }
}
