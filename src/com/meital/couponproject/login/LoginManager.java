package com.meital.couponproject.login;

import com.meital.couponproject.Enum.ClientType;
import com.meital.couponproject.Enum.Error;
import com.meital.couponproject.dao.*;
import com.meital.couponproject.exceptions.ApplicationException;
import com.meital.couponproject.facade.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginManager {

    public static final LoginManager instance = new LoginManager();

    private final Set<String> authenticatedUsers = new HashSet<>();
    private final CustomerFacade customerFacade = CustomerFacade.instance;
    private final CompanyFacade companyFacade = CompanyFacade.instance;
    private final AdminFacade adminFacade = AdminFacade.instance;


    //-----------------------login method that returns facade class by client type--------------
    public UserFacade login(final String email, final String password, final ClientType clientType) {
        boolean isAuthenticated = false;
        UserFacade userFacade = null;

        switch (clientType) {
            case COMPANY:
                userFacade = companyFacade;
                break;

            case CUSTOMER:
                userFacade = customerFacade;
                break;

            case ADMIN:
                userFacade = adminFacade;
                break;
        }

        if (userFacade.login(email, password)) {
            authenticatedUsers.add(email);
            return userFacade;
        }

        return null;
    }

}
