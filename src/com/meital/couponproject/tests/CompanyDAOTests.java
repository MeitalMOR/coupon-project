package com.meital.couponproject.tests;

import com.meital.couponproject.dao.CompanyDAO;
import com.meital.couponproject.exceptions.CrudException;
import com.meital.couponproject.model.Company;
import com.meital.couponproject.tests.config.CompanyConfig;
import com.meital.couponproject.util.DataBaseInitializer;
import java.util.ArrayList;

public class CompanyDAOTests {
    public static CompanyDAOTests instance = new CompanyDAOTests();
    private final CompanyDAO companyDAO = CompanyDAO.instance;

    //--------------------------------Test create Company and read company
    public void createCompanyTest() throws CrudException {

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long newCompanyId = companyDAO.create(company);

        Company newCompany = companyDAO.read(newCompanyId);

        if (newCompanyId.equals(newCompany.getId())) {
            System.out.println("Test succeeded");
        }

       DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test update Company
    public void updateCompanyTest() throws CrudException {

        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        Company updatedCompany = new Company(companyId, company.getName(), "companyco@gmail.com", company.getPassword());
        companyDAO.update(updatedCompany);

        companyDAO.read(companyId);
        if (updatedCompany.getEmail().equals(companyDAO.read(companyId).getEmail())) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test delete Company
    public void deleteCompanyTest() throws CrudException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        companyDAO.delete(companyId);
        if (!companyDAO.isExists(companyId)){
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //--------------------------------Test get all Companies
    public void readAllCompaniesTest() throws CrudException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Company company1 = new Company(CompanyConfig.company2Name, CompanyConfig.company2Email, CompanyConfig.company2Password);
        Long c1 = companyDAO.create(company);
        Long c2 = companyDAO.create(company1);
        ArrayList<Company> companies = companyDAO.readAll();

        if (!companies.isEmpty()) {
            System.out.println(companies);
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //-------------------------- Test Is  company Exist By email and password
    public void isCompanyExistByEmailAndPasswordTest() throws CrudException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        companyDAO.create(company);

        boolean isCompanyExist = companyDAO.isExists(company.getEmail(), company.getPassword());
        if (isCompanyExist) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //-------------------------- Test Is  company Exist By  Company name
    public void isCompanyExistByNameTest() throws CrudException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        companyDAO.create(company);

        boolean isCompanyExist = companyDAO.isExists(company.getName());
        if (isCompanyExist) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }

    //-------------------------- Test Is  company Exist By Company ID
    public void isCompanyExistByIdTest() throws CrudException {
        Company company = new Company(CompanyConfig.company1Name, CompanyConfig.company1Email, CompanyConfig.company1Password);
        Long companyId = companyDAO.create(company);

        boolean isCompanyExist = companyDAO.isExists(companyId);
        if (isCompanyExist) {
            System.out.println("Test succeeded");
        }

        DataBaseInitializer.dataBaseRestart();
    }



}
