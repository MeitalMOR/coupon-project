package com.meital.couponproject.exceptions;

import com.meital.couponproject.Enum.CrudOperation;
import com.meital.couponproject.Enum.EntityType;

public class CrudException extends Exception{

    public CrudException(final EntityType entityType, CrudOperation crudOperation) {
        super("Failed to " + crudOperation + " " + entityType);
    }
}
