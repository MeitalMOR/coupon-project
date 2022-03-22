package com.meital.couponproject.exceptions;

import com.meital.couponproject.Enum.EntityType;
import com.meital.couponproject.Enum.Error;

public class ApplicationException extends RuntimeException {

    public ApplicationException(Error error, String massage) {
        super(error + " - " + massage);
    }

}
